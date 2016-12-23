// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dto;

import com.jiam365.flow.server.utils.DoubleUtils;

public class TProfitByState
{
    private String stateCode;
    private String stateName;
    private double cost;
    private double amount;
    
    public TProfitByState() {
    }
    
    public TProfitByState(final String stateCode, final double cost, final double amount) {
        this.stateCode = stateCode;
        this.setCost(cost);
        this.setAmount(amount);
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
