package com.jiam365.modules.mapper;

import com.jiam365.modules.utils.Exceptions;
import com.jiam365.modules.utils.ReflectionUtils;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class JaxbMapper
{
    private static ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap();

    public static String toXml(Object root)
    {
        Class<?> clazz = ReflectionUtils.getUserClass(root);
        return toXml(root, clazz, null);
    }

    public static String toXml(Object root, String encoding)
    {
        Class<?> clazz = ReflectionUtils.getUserClass(root);
        return toXml(root, clazz, encoding);
    }

    public static String toXml(Object root, Class<?> clazz, String encoding)
    {
        try
        {
            StringWriter writer = new StringWriter();
            createMarshaller(clazz, encoding).marshal(root, writer);
            return writer.toString();
        }
        catch (JAXBException e)
        {
            throw Exceptions.unchecked(e);
        }
    }

    public static String toXml(Collection<?> root, String rootName, Class<?> clazz)
    {
        return toXml(root, rootName, clazz, null);
    }

    public static String toXml(Collection<?> root, String rootName, Class<?> clazz, String encoding)
    {
        try
        {
            CollectionWrapper wrapper = new CollectionWrapper();
            wrapper.collection = root;

            JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement(new QName(rootName), CollectionWrapper.class, wrapper);

            StringWriter writer = new StringWriter();
            createMarshaller(clazz, encoding).marshal(wrapperElement, writer);

            return writer.toString();
        }
        catch (JAXBException e)
        {
            throw Exceptions.unchecked(e);
        }
    }

    public static <T> T fromXml(String xml, Class<T> clazz)
    {
        try
        {
            StringReader reader = new StringReader(xml);
            return (T)createUnmarshaller(clazz).unmarshal(reader);
        }
        catch (JAXBException e)
        {
            throw Exceptions.unchecked(e);
        }
    }

    public static Marshaller createMarshaller(Class<?> clazz, String encoding)
    {
        try
        {
            JAXBContext jaxbContext = getJaxbContext(clazz);

            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
            if (StringUtils.isNotBlank(encoding)) {
                marshaller.setProperty("jaxb.encoding", encoding);
            }
            return marshaller;
        }
        catch (JAXBException e)
        {
            throw Exceptions.unchecked(e);
        }
    }

    public static Unmarshaller createUnmarshaller(Class<?> clazz)
    {
        try
        {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            return jaxbContext.createUnmarshaller();
        }
        catch (JAXBException e)
        {
            throw Exceptions.unchecked(e);
        }
    }

    protected static JAXBContext getJaxbContext(Class<?> clazz)
    {
        Assert.notNull(clazz, "'clazz' must not be null");
        JAXBContext jaxbContext = (JAXBContext)jaxbContexts.get(clazz);
        if (jaxbContext == null) {
            try
            {
                jaxbContext = JAXBContext.newInstance(new Class[] { clazz, CollectionWrapper.class });
                jaxbContexts.putIfAbsent(clazz, jaxbContext);
            }
            catch (JAXBException ex)
            {
                throw new RuntimeException("Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
            }
        }
        return jaxbContext;
    }

    public static class CollectionWrapper
    {
        @XmlAnyElement
        protected Collection<?> collection;
    }
}
