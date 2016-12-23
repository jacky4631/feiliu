// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dto;

import com.jiam365.flow.server.utils.DoubleUtils;

public class TProfitByChannel
{
    private Long channelId;
    private String channelName;
    private double cost;
    private double amount;
    
    public TProfitByChannel() {
    }
    
    public TProfitByChannel(final Long channelId, final double cost, final double amount) {
        this.channelId = channelId;
        this.setCost(cost);
        this.setAmount(amount);
    }
    
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
    
    public double getCost() {
        return this.cost;
    }
    
    public void setCost(final double cost) {
        this.cost = DoubleUtils.round(cost, 2);
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(final double amount) {
        this.amount = DoubleUtils.round(amount, 2);
    }
    
    public double getProfit() {
        return DoubleUtils.sub(this.amount, this.cost);
    }
}
