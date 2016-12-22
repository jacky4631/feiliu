package com.jiam365.modules.tools;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

public class SpringContext
        implements ApplicationContextAware
{
    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext()
    {
        checkApplicationContext();
        return applicationContext;
    }

    public static <T> T getBean(String name)
    {
        checkApplicationContext();
        return (T)applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz)
    {
        checkApplicationContext();
        Map beans = applicationContext.getBeansOfType(clazz);
        if ((beans != null) && (!beans.isEmpty()))
        {
            Iterator localIterator = beans.keySet().iterator();
            if (localIterator.hasNext())
            {
                Object o = localIterator.next();
                return (T)beans.get(o);
            }
        }
        return null;
    }

    public static void registerBean(String className, String initMethodName, String destroyMethodName)
    {
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();

        String beanId = getBeanId(className);
        if (getBean(beanId) == null)
        {
            BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(className);
            if (StringUtils.isNotBlank(initMethodName)) {
                bdb.setInitMethodName(initMethodName);
            }
            if (StringUtils.isNotBlank(destroyMethodName)) {
                bdb.setDestroyMethodName(destroyMethodName);
            }
            bdb.getBeanDefinition().setAttribute("id", beanId);
            acf.registerBeanDefinition(beanId, bdb.getBeanDefinition());
        }
    }

    public static void removeBean(String className)
    {
        String beanId = getBeanId(className);
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        if (acf.containsBean(beanId)) {
            acf.removeBeanDefinition(beanId);
        }
    }

    public static void fireEvent(ApplicationEvent event)
    {
        checkApplicationContext();
        applicationContext.publishEvent(event);
    }

    private static String getBeanId(String className)
    {
        int pos = className.lastIndexOf('.');
        String commName = className.substring(pos + 1);
        String firstLetter = commName.substring(0, 1);

        return firstLetter.toLowerCase() + commName.substring(1);
    }

    private static void checkApplicationContext()
    {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext not set, Please define the SpringContextHolder bean in applicationContext.xml.");
        }
    }
}
