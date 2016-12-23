// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "trade_smlog", noClassnameStored = true)
public class SmLog implements Serializable
{
    private static final long serialVersionUID = 2955106427228938402L;
    @Id
    private String tradeId;
    @Indexed
    private String username;
    @Indexed
    private String mobile;
    private double billAmount;
    private boolean paid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
    
    public SmLog() {
        this.billAmount = 5.0;
        this.paid = false;
        this.created = new Date();
    }
    
    public String getTradeId() {
        return this.tradeId;
    }
    
    public void setTradeId(final String tradeId) {
        this.tradeId = tradeId;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }
    
    public boolean isPaid() {
        return this.paid;
    }
    
    public void setPaid(final boolean paid) {
        this.paid = paid;
    }
    
    public Date getCreated() {
        return this.created;
    }
    
    public void setCreated(final Date created) {
        this.created = created;
    }
    
    public double getBillAmount() {
        return this.billAmount;
    }
    
    public void setBillAmount(final double billAmount) {
        this.billAmount = billAmount;
    }
}
