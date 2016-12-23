// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.params;

public class FunctionLimitParam
{
    private boolean enableAutoLimit;
    private int lastDays;
    private int rechargeTimesLimit;
    private int limitScopeSeconds;
    public static final String PRD_SHOWMODE_ALL = "all";
    public static final String PRD_SHOWMODE_AUTHED = "authed";
    private String productShowMode;
    
    public FunctionLimitParam() {
        this.enableAutoLimit = true;
        this.lastDays = 2;
        this.rechargeTimesLimit = 10;
        this.limitScopeSeconds = 60;
        this.productShowMode = "all";
    }
    
    public boolean showAllProducts() {
        return !"authed".equals(this.productShowMode);
    }
    
    public boolean isEnableAutoLimit() {
        return this.enableAutoLimit;
    }
    
    public void setEnableAutoLimit(final boolean enableAutoLimit) {
        this.enableAutoLimit = enableAutoLimit;
    }
    
    public int getLastDays() {
        return this.lastDays;
    }
    
    public void setLastDays(final int lastDays) {
        this.lastDays = lastDays;
    }
    
    public int getRechargeTimesLimit() {
        return this.rechargeTimesLimit;
    }
    
    public void setRechargeTimesLimit(final int rechargeTimesLimit) {
        this.rechargeTimesLimit = rechargeTimesLimit;
    }
    
    public int getLimitScopeSeconds() {
        return this.limitScopeSeconds;
    }
    
    public void setLimitScopeSeconds(final int limitScopeSeconds) {
        this.limitScopeSeconds = limitScopeSeconds;
    }
    
    public String getProductShowMode() {
        return this.productShowMode;
    }
    
    public void setProductShowMode(final String productShowMode) {
        this.productShowMode = productShowMode;
    }
}
