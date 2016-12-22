package com.jiam365.modules.mapper;

import com.jiam365.modules.utils.ClassPathUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.dozer.DozerBeanMapper;

public class BeanMapper
{
    private static DozerBeanMapper dozer;

    static
    {
        String defaultDozerMappingFile = "dozer_config.xml";
        if (ClassPathUtils.locateFromClasspath("dozer_config.xml") != null)
        {
            List<String> dozerMappingFiles = new ArrayList();
            dozerMappingFiles.add("dozer_config.xml");
            dozer = new DozerBeanMapper(dozerMappingFiles);
        }
        else
        {
            dozer = new DozerBeanMapper();
        }
    }

    public static <T> T map(Object source, Class<T> destinationClass)
    {
        return (T)dozer.map(source, destinationClass);
    }

    public static <T> List<T> mapList(Collection<?> sourceList, Class<T> destinationClass)
    {
        List<T> destinationList = new ArrayList();
        for (Object sourceObject : sourceList)
        {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    public static void copy(Object source, Object destinationObject)
    {
        dozer.map(source, destinationObject);
    }
}
