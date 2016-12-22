package com.jiam365.modules.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class PropertiesLoader
{
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();
    private final Properties properties;

    public PropertiesLoader(String... resourcesPaths)
    {
        this.properties = loadProperties(resourcesPaths);
    }

    public Properties getProperties()
    {
        return this.properties;
    }

    private String getValue(String key)
    {
        String systemProperty = System.getProperty(key);
        String value = systemProperty != null ? systemProperty : this.properties.getProperty(key);
        return value == null ? null : fixValue(value);
    }

    private String fixValue(String value)
    {
        String regex = "\\$\\{[^}]*\\}";
        Pattern patternForTag = Pattern.compile(regex);

        Matcher matcher = patternForTag.matcher(value);
        boolean result = matcher.find();
        StringBuffer sb = new StringBuffer();
        while (result)
        {
            StringBuilder key = new StringBuilder(matcher.group(0));
            key.delete(0, 2);
            key.delete(key.length() - 1, key.length());
            String v = getValue(key.toString().trim());
            matcher.appendReplacement(sb, v);
            result = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public String getProperty(String key)
    {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    public String getProperty(String key, String defaultValue)
    {
        String value = getValue(key);
        return value != null ? value : defaultValue;
    }

    public Integer getInteger(String key)
    {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Integer.valueOf(value);
    }

    public Integer getInteger(String key, Integer defaultValue)
    {
        String value = getValue(key);
        return value != null ? Integer.valueOf(value) : defaultValue;
    }

    public Long getLong(String key, Long defaultValue)
    {
        String value = getValue(key);
        return value != null ? Long.valueOf(value) : defaultValue;
    }

    public Long getLong(String key)
    {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Long.valueOf(value);
    }

    public Double getDouble(String key)
    {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Double.valueOf(value);
    }

    public Double getDouble(String key, Integer defaultValue)
    {
        String value = getValue(key);
        return Double.valueOf(value != null ? Double.valueOf(value).doubleValue() : defaultValue.intValue());
    }

    public Boolean getBoolean(String key)
    {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Boolean.valueOf(value);
    }

    public Boolean getBoolean(String key, boolean defaultValue)
    {
        String value = getValue(key);
        return Boolean.valueOf(value != null ? Boolean.valueOf(value).booleanValue() : defaultValue);
    }

    private Properties loadProperties(String... resourcesPaths)
    {
        Properties props = new Properties();
        for (String location : resourcesPaths)
        {
            InputStream is = null;
            try
            {
                Resource resource = resourceLoader.getResource(location);
                is = resource.getInputStream();
                props.load(is);
            }
            catch (IOException ex)
            {
                System.out.println("Warnning: Loadding Properties error��" + ex.getLocalizedMessage());
            }
            finally
            {
                IOUtils.closeQuietly(is);
            }
        }
        return props;
    }
}
