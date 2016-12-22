package com.jiam365.modules.utils;

import java.net.URL;

public class ClassPathUtils
{
    public static URL locateFromClasspath(String resourceName)
    {
        URL url = null;

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            url = loader.getResource(resourceName);
        }
        if (url == null) {
            url = ClassLoader.getSystemResource(resourceName);
        }
        return url;
    }
}