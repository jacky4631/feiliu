// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dto;

public class UnProcessedTradeCount
{
    private long unProcessedRecharge;
    private long unProcessedCallback;
    
    public UnProcessedTradeCount() {
        this.unProcessedRecharge = 0L;
        this.unProcessedCallback = 0L;
    }
    
    public UnProcessedTradeCount(final long unProcessedRecharge, final long unProcessedCallback) {
        this.unProcessedRecharge = 0L;
        this.unProcessedCallback = 0L;
        this.unProcessedRecharge = unProcessedRecharge;
        this.unProcessedCallback = unProcessedCallback;
    }
    
    public long getUnProcessedRecharge() {
        return this.unProcessedRecharge;
    }
    
    public void setUnProcessedRecharge(final long unProcessedRecharge) {
        this.unProcessedRecharge = unProcessedRecharge;
    }
    
    public long getUnProcessedCallback() {
        return this.unProcessedCallback;
    }
    
    public void setUnProcessedCallback(final long unProcessedCallback) {
        this.unProcessedCallback = unProcessedCallback;
    }
}
