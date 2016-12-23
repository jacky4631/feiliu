// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;

@Entity(value = "flow_channel_account", noClassnameStored = true)
public class ChannelAccount
{
    @Id
    private long channleId;
    private double balance;
    
    public long getChannleId() {
        return this.channleId;
    }
    
    public void setChannleId(final long channleId) {
        this.channleId = channleId;
    }
    
    public double getBalance() {
        return this.balance;
    }
    
    public void setBalance(final double balance) {
        this.balance = balance;
    }
}
