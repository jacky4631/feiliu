// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import java.util.ArrayList;
import java.util.List;

public class ChooseRestrict
{
    private boolean priceProtected;
    private double userDiscount;
    private List<String> exceptChannels;
    
    public ChooseRestrict() {
        this.priceProtected = false;
        this.exceptChannels = new ArrayList<String>();
    }
    
    public int retryTimes() {
        return this.exceptChannels.size();
    }
    
    public void addExceptChannel(final String fullChannelProductGroupId) {
        this.exceptChannels.add(fullChannelProductGroupId);
    }
    
    public boolean isPriceProtected() {
        return this.priceProtected;
    }
    
    public void setPriceProtected(final boolean priceProtected) {
        this.priceProtected = priceProtected;
    }
    
    public double getUserDiscount() {
        return this.userDiscount;
    }
    
    public void setUserDiscount(final double userDiscount) {
        this.userDiscount = userDiscount;
    }
    
    public List<String> getExceptChannels() {
        return this.exceptChannels;
    }
    
    public void setExceptChannels(final List<String> exceptChannels) {
        this.exceptChannels = exceptChannels;
    }
}
