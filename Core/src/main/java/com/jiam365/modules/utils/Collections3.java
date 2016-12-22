package com.jiam365.modules.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

public class Collections3
{
    public static <T> List<T> subtract(Collection<T> a, Collection<T> b)
    {
        ArrayList<T> list = new ArrayList(a);
        for (Object element : b) {
            list.remove(element);
        }
        return list;
    }

    public static Map extractToMap(Collection collection, String keyPropertyName, String valuePropertyName)
    {
        Map map = new HashMap(collection.size());
        try
        {
            for (Object obj : collection) {
                map.put(PropertyUtils.getProperty(obj, keyPropertyName),
                        PropertyUtils.getProperty(obj, valuePropertyName));
            }
        }
        catch (Exception e)
        {
            throw Exceptions.unchecked(e);
        }
        return map;
    }

    public static List extractToList(Collection collection, String propertyName)
    {
        List list = new ArrayList(collection.size());
        try
        {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        }
        catch (Exception e)
        {
            throw Exceptions.unchecked(e);
        }
        return list;
    }

    public static String extractToString(Collection collection, String propertyName, String separator)
    {
        List list = extractToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    public static String convertToString(Collection collection, String separator)
    {
        return StringUtils.join(collection, separator);
    }

    public static String convertToString(Collection collection, String prefix, String postfix)
    {
        StringBuilder builder = new StringBuilder();
        for (Object o : collection) {
            builder.append(prefix).append(o).append(postfix);
        }
        return builder.toString();
    }
}
