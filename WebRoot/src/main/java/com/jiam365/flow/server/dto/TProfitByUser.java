// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dto;

import com.jiam365.flow.server.utils.DoubleUtils;

public class TProfitByUser
{
    private String username;
    private String displayName;
    private String company;
    private double cost;
    private double amount;
    
    public TProfitByUser() {
    }
    
    public TProfitByUser(final String username, final double cost, final double amount) {
        this.username = username;
        this.setCost(cost);
        this.setAmount(amount);
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
    
    public String getCompany() {
        return this.company;
    }
    
    public void setCompany(final String company) {
        this.company = company;
    }
}
