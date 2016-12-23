// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dto;

import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.modules.telco.Telco;

public class TProfitByTecol
{
    private Telco telco;
    private double cost;
    private double amount;
    
    public TProfitByTecol() {
    }
    
    public TProfitByTecol(final Telco telco, final double cost, final double amount) {
        this.telco = telco;
        this.setCost(cost);
        this.setAmount(amount);
    }
    
    public Telco getTelco() {
        return this.telco;
    }
    
    public void setTelco(final Telco telco) {
        this.telco = telco;
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
