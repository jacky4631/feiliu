package com.jiam365.modules.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Exceptions
{
    public static RuntimeException unchecked(Exception e)
    {
        if ((e instanceof RuntimeException)) {
            return (RuntimeException)e;
        }
        return new RuntimeException(e);
    }

    public static String getStackTraceAsString(Exception e)
    {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses)
    {
        Throwable cause = ex.getCause();
        while (cause != null)
        {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }
}
