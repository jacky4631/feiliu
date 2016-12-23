// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SystemStartup implements ServletContextListener
{
    public void contextInitialized(final ServletContextEvent sce) {
        try {
            System.setProperty("java.net.preferIPv4Stack", "true");
            System.setProperty("java.net.preferIPv6Addresses", "false");
        }
        catch (Exception ex) {}
    }
    
    public void contextDestroyed(final ServletContextEvent sce) {
    }
}
