package com.jiam365.modules.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

public class ReflectionUtils
{
    private static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    public static Object getFieldValue(Object object, String fieldName)
    {
        String curFieldName = fieldName;
        int pos = fieldName.indexOf('.');
        String destFieldName = "";
        if (pos > -1)
        {
            curFieldName = fieldName.substring(0, pos);
            destFieldName = fieldName.substring(pos + 1);
        }
        Field field = getDeclaredField(object, curFieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);

        Object result = null;
        try
        {
            result = field.get(object);
        }
        catch (IllegalAccessException e)
        {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
        if (pos > -1) {
            return getFieldValue(result, destFieldName);
        }
        return result;
    }

    public static Object getProperty(Object object, String fieldName)
    {
        try
        {
            return PropertyUtils.getProperty(object, fieldName);
        }
        catch (Exception e)
        {
            logger.debug("获取层次结构对象的属性值失败，{}", e.getMessage());
        }
        return "";
    }

    public static void setFieldValue(Object object, String fieldName, Object value)
    {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        try
        {
            field.set(object, value);
        }
        catch (IllegalAccessException e)
        {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
    }

    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters)
            throws InvocationTargetException
    {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }
        method.setAccessible(true);
        try
        {
            return method.invoke(object, parameters);
        }
        catch (IllegalAccessException e)
        {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
        return null;
    }

    protected static Field getDeclaredField(Object object, String fieldName)
    {
        Assert.notNull(object, "object不能为空");
        Assert.hasText(fieldName, "fieldName");
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try
            {
                return superClass.getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException localNoSuchFieldException) {}
        }
        return null;
    }

    protected static void makeAccessible(Field field)
    {
        if ((!Modifier.isPublic(field.getModifiers())) || (!Modifier.isPublic(field.getDeclaringClass().getModifiers()))) {
            field.setAccessible(true);
        }
    }

    protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes)
    {
        Assert.notNull(object, "object不能为空");
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try
            {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            }
            catch (NoSuchMethodException localNoSuchMethodException) {}
        }
        return null;
    }

    public static <T> Class<T> getSuperClassGenricType(Class clazz)
    {
        return getSuperClassGenricType(clazz, 0);
    }

    public static Class getSuperClassGenricType(Class clazz, int index)
    {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType))
        {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        if ((index >= params.length) || (index < 0))
        {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);

            return Object.class;
        }
        if (!(params[index] instanceof Class))
        {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class)params[index];
    }

    public static Class getPropertyArgumentsType(Class<?> clazz, String propertyName)
    {
        return getPropertyArgumentsType(clazz, propertyName, 0);
    }

    public static Class getPropertyArgumentsType(Class<?> clazz, String propertyName, int index)
    {
        try
        {
            Field field = clazz.getDeclaredField(propertyName);
            Type genericType = field.getGenericType();
            if (!(genericType instanceof ParameterizedType))
            {
                logger.warn(clazz.getSimpleName() + "'s property {} is not ParameterizedType", propertyName);
                return Object.class;
            }
            Type[] params = ((ParameterizedType)genericType).getActualTypeArguments();
            if ((index >= params.length) || (index < 0))
            {
                logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s property {} Parameterized Type: {} ", propertyName,
                        Integer.valueOf(params.length));
                return Object.class;
            }
            if (!(params[index] instanceof Class))
            {
                logger.warn(clazz.getSimpleName() + "'s property {} not set the actual class on property type generic parameter", propertyName);

                return Object.class;
            }
            return (Class)params[index];
        }
        catch (SecurityException e)
        {
            logger.error("Security Error in getPropertyArgumentsType", e);
        }
        catch (NoSuchFieldException e)
        {
            logger.error("Filed {} not found in {}", propertyName, clazz.getSimpleName());
        }
        return Object.class;
    }

    public static List fetchElementPropertyToList(Collection collection, String propertyName)
    {
        List list = new ArrayList();
        try
        {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        }
        catch (Exception e)
        {
            throw convertToUncheckedException(e);
        }
        return list;
    }

    public static String fetchElementPropertyToString(Collection collection, String propertyName, String separator)
    {
        List list = fetchElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    public static Object convertValue(Object value, Class<?> clazz, String propertyName)
    {
        try
        {
            if (StringUtils.contains(propertyName, "."))
            {
                String destPropertyName = StringUtils.substringAfter(propertyName, ".");
                String destObjectName = StringUtils.substringBefore(propertyName, ".");
                Class<?> objectType = BeanUtils.getPropertyDescriptor(clazz, destObjectName).getPropertyType();
                if (Collection.class.isAssignableFrom(objectType)) {
                    objectType = getPropertyArgumentsType(clazz, destObjectName, 0);
                }
                return convertValue(value, objectType, destPropertyName);
            }
            Class<?> toType = BeanUtils.getPropertyDescriptor(clazz, propertyName).getPropertyType();
            DateConverter dc = new DateConverter();
            dc.setUseLocaleFormat(true);
            dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" });
            ConvertUtils.register(dc, Date.class);
            return ConvertUtils.convert(value, toType);
        }
        catch (Exception e)
        {
            throw convertToUncheckedException(e);
        }
    }

    public static Class<?> getUserClass(Object instance)
    {
        Assert.notNull(instance, "Instance must not be null");
        Class<?> clazz = instance.getClass();
        if ((clazz != null) && (clazz.getName().contains("$$")))
        {
            Class<?> superClass = clazz.getSuperclass();
            if ((superClass != null) && (!Object.class.equals(superClass))) {
                return superClass;
            }
        }
        return clazz;
    }

    public static IllegalArgumentException convertToUncheckedException(Exception e)
    {
        if (((e instanceof IllegalAccessException)) || ((e instanceof IllegalArgumentException)) || ((e instanceof NoSuchMethodException))) {
            return new IllegalArgumentException("Refelction Exception.", e);
        }
        return new IllegalArgumentException(e);
    }
}
