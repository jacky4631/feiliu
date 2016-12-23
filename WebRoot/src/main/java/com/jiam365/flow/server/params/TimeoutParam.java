// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.params;

public class TimeoutParam
{
    private boolean autoProcess;
    private int timeout;
    private String timeOutStrategy;
    
    public TimeoutParam() {
        this.autoProcess = true;
        this.timeout = 24;
        this.timeOutStrategy = "success";
    }
    
    public boolean isAutoProcess() {
        return this.autoProcess;
    }
    
    public void setAutoProcess(final boolean autoProcess) {
        this.autoProcess = autoProcess;
    }
    
    public int getTimeout() {
        return this.timeout;
    }
    
    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }
    
    public String getTimeOutStrategy() {
        return this.timeOutStrategy;
    }
    
    public void setTimeOutStrategy(final String timeOutStrategy) {
        this.timeOutStrategy = timeOutStrategy;
    }
}
