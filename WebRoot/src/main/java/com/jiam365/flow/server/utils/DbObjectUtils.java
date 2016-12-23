// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.utils;

import java.lang.reflect.InvocationTargetException;
import com.jiam365.modules.utils.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.Date;
import java.lang.reflect.Modifier;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DbObjectUtils
{
    public static <T> DBObject bean2DBObject(final T bean) throws IllegalArgumentException, IllegalAccessException {
        if (bean == null) {
            return null;
        }
        final DBObject dbObject = (DBObject)new BasicDBObject();
        final Field[] declaredFields;
        final Field[] fields = declaredFields = bean.getClass().getDeclaredFields();
        for (final Field field : declaredFields) {
            final boolean isStatic = Modifier.isStatic(field.getModifiers());
            if (!isStatic) {
                final String fieldName = field.getName();
                final boolean accessFlag = field.isAccessible();
                if (!accessFlag) {
                    field.setAccessible(true);
                }
                final Object fieldValue = field.get(bean);
                if (fieldValue != null) {
                    if (fieldValue instanceof Integer) {
                        final int value = (int)fieldValue;
                        dbObject.put(fieldName, (Object)value);
                    }
                    else if (fieldValue instanceof String) {
                        final String value2 = (String)fieldValue;
                        dbObject.put(fieldName, (Object)value2);
                    }
                    else if (fieldValue instanceof Double) {
                        final double value3 = (double)fieldValue;
                        dbObject.put(fieldName, (Object)value3);
                    }
                    else if (fieldValue instanceof Float) {
                        final float value4 = (float)fieldValue;
                        dbObject.put(fieldName, (Object)value4);
                    }
                    else if (fieldValue instanceof Long) {
                        final long value5 = (long)fieldValue;
                        dbObject.put(fieldName, (Object)value5);
                    }
                    else if (fieldValue instanceof Boolean) {
                        final boolean value6 = (boolean)fieldValue;
                        dbObject.put(fieldName, (Object)value6);
                    }
                    else if (fieldValue instanceof Date) {
                        final Date value7 = (Date)fieldValue;
                        dbObject.put(fieldName, (Object)value7);
                    }
                    else {
                        final String value2 = fieldValue.toString();
                        dbObject.put(fieldName, (Object)value2);
                    }
                    field.setAccessible(accessFlag);
                }
            }
        }
        return dbObject;
    }
    
    public static <T> T dbObject2Bean(final DBObject dbObject, final T bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean == null) {
            return null;
        }
        final Field[] declaredFields;
        final Field[] fields = declaredFields = bean.getClass().getDeclaredFields();
        for (final Field field : declaredFields) {
            final String varName = field.getName();
            final Object object = dbObject.get(varName);
            if (object != null) {
                ReflectionUtils.setFieldValue((Object)bean, varName, object);
            }
        }
        return bean;
    }
}
