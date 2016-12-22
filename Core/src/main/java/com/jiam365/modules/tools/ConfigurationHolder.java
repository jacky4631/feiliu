package com.jiam365.modules.tools;

import com.jiam365.modules.utils.PropertiesLoader;

public class ConfigurationHolder
{
    private PropertiesLoader p;
    public static synchronized void reload() {}

    private static class SingletonHolder
    {
        private static ConfigurationHolder uniqueInstance;
        private static String APP_PROPERTIES_FILE_NAME = "application.properties";
        private static String APP_PROPERTIES_FILE_NAME_DEV = "application.dev.properties";

        static
        {
            uniqueInstance = new ConfigurationHolder();
            loadProperties();
        }

        static void loadProperties()
        {
            String activeProfile = System.getProperty("spring.profiles.active");
            if ("development".equalsIgnoreCase(activeProfile)) {
                uniqueInstance.p = new PropertiesLoader(new String[] { APP_PROPERTIES_FILE_NAME, APP_PROPERTIES_FILE_NAME_DEV });
            } else {
                uniqueInstance.p = new PropertiesLoader(new String[] { APP_PROPERTIES_FILE_NAME });
            }
        }
    }

    public static PropertiesLoader properties()
    {
        return SingletonHolder.uniqueInstance.p;
    }
}
