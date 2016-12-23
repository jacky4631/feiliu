// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.dto;

import com.jiam365.flow.server.product.FlowPackage;

public class TFlowPackage
{
    private String id;
    private String name;
    private String origiProductId;
    private double discount;
    private boolean enabled;
    private int priority;
    private String channel;
    private boolean channelStatus;
    
    public TFlowPackage() {
    }
    
    public TFlowPackage(final FlowPackage flowPackage) {
        this.setName(flowPackage.getTitle());
        this.setId(flowPackage.getId());
        this.setDiscount(flowPackage.getDiscount());
        this.setEnabled(flowPackage.isEnabled());
        this.setOrigiProductId(flowPackage.getOrigiProductId());
        this.setPriority(flowPackage.getPriority());
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getChannel() {
        return this.channel;
    }
    
    public void setChannel(final String channel) {
        this.channel = channel;
    }
    
    public String getOrigiProductId() {
        return this.origiProductId;
    }
    
    public void setOrigiProductId(final String origiProductId) {
        this.origiProductId = origiProductId;
    }
    
    public double getDiscount() {
        return this.discount;
    }
    
    public void setDiscount(final double discount) {
        this.discount = discount;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public void setPriority(final int priority) {
        this.priority = priority;
    }
    
    public boolean isChannelStatus() {
        return this.channelStatus;
    }
    
    public void setChannelStatus(final boolean channelStatus) {
        this.channelStatus = channelStatus;
    }
}
