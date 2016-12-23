// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.params;

public class UserReportParam
{
    private int retryTimes;
    private int delayMinutes;
    
    public UserReportParam() {
        this.retryTimes = 3;
        this.delayMinutes = 2;
    }
    
    public int getRetryTimes() {
        return this.retryTimes;
    }
    
    public void setRetryTimes(final int retryTimes) {
        this.retryTimes = retryTimes;
    }
    
    public int getDelayMinutes() {
        return this.delayMinutes;
    }
    
    public void setDelayMinutes(final int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }
}
