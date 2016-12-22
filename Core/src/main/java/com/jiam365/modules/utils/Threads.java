package com.jiam365.modules.utils;

import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Threads
{
    public static void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException localInterruptedException) {}
    }

    public static void sleep(long duration, TimeUnit unit)
    {
        try
        {
            Thread.sleep(unit.toMillis(duration));
        }
        catch (InterruptedException localInterruptedException) {}
    }

    public static void gracefulShutdown(ExecutorService pool, int shutdownTimeout, int shutdownNowTimeout, TimeUnit timeUnit)
    {
        pool.shutdown();
        try
        {
            if (!pool.awaitTermination(shutdownTimeout, timeUnit))
            {
                pool.shutdownNow();
                if (!pool.awaitTermination(shutdownNowTimeout, timeUnit)) {
                    System.err.println("Pool did not terminated");
                }
            }
        }
        catch (InterruptedException ie)
        {
            pool.shutdownNow();

            Thread.currentThread().interrupt();
        }
    }

    public static void normalShutdown(ExecutorService pool, int timeout, TimeUnit timeUnit)
    {
        try
        {
            pool.shutdownNow();
            if (!pool.awaitTermination(timeout, timeUnit)) {
                System.err.println("Pool did not terminated");
            }
        }
        catch (InterruptedException ie)
        {
            Thread.currentThread().interrupt();
        }
    }
}
