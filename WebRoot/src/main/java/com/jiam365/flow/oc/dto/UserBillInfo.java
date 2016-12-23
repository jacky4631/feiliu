// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.dto;

import com.jiam365.flow.server.utils.DoubleUtils;

public class UserBillInfo
{
    private String username;
    private String displayName;
    private String company;
    private String linkman;
    private double billAmount;
    private double balance;
    
    public UserBillInfo(final String username, final String displayName, final Double billAmount) {
        this.username = username;
        this.displayName = displayName;
        this.billAmount = billAmount;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public Double getBillAmount() {
        return DoubleUtils.round(this.billAmount, 2);
    }
    
    public void setBillAmount(final Double billAmount) {
        this.billAmount = billAmount;
    }
    
    public String getCompany() {
        return (this.company == null) ? "" : this.company;
    }
    
    public void setCompany(final String company) {
        this.company = company;
    }
    
    public String getLinkman() {
        return (this.linkman == null) ? "" : this.linkman;
    }
    
    public void setLinkman(final String linkman) {
        this.linkman = linkman;
    }
    
    public Double getBalance() {
        return DoubleUtils.round(this.balance, 2);
    }
    
    public void setBalance(final Double balance) {
        this.balance = balance;
    }
}
