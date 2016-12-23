// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dto;

import com.jiam365.modules.telco.Telco;

public class TChannelProductTimeConsuming
{
    private Long channelId;
    private String channelName;
    private int timeConsuming;
    private String stateCode;
    private String stateName;
    private Telco telco;
    
    public Long getChannelId() {
        return this.channelId;
    }
    
    public void setChannelId(final Long channelId) {
        this.channelId = channelId;
    }
    
    public String getChannelName() {
        return this.channelName;
    }
    
    public void setChannelName(final String channelName) {
        this.channelName = channelName;
    }
    
    public int getTimeConsuming() {
        return this.timeConsuming;
    }
    
    public void setTimeConsuming(final int timeConsuming) {
        this.timeConsuming = timeConsuming;
    }
    
    public String getStateCode() {
        return this.stateCode;
    }
    
    public void setStateCode(final String stateCode) {
        this.stateCode = stateCode;
    }
    
    public String getStateName() {
        return this.stateName;
    }
    
    public void setStateName(final String stateName) {
        this.stateName = stateName;
    }
    
    public Telco getTelco() {
        return this.telco;
    }
    
    public void setTelco(final Telco telco) {
        this.telco = telco;
    }
}
