// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dto;

import java.text.NumberFormat;

public class TradeCountByChannelName
{
    private String channelName;
    private Long count;
    private Long finishCount;
    
    public String getChannelName() {
        return this.channelName;
    }
    
    public void setChannelName(final String channelName) {
        this.channelName = channelName;
    }
    
    public Long getCount() {
        return this.count;
    }
    
    public void setCount(final Long count) {
        this.count = count;
    }
    
    public Long getFinishCount() {
        return this.finishCount;
    }
    
    public void setFinishCount(final Long finishCount) {
        this.finishCount = finishCount;
    }
    
    public String getFinishPercent() {
        if (this.count == 0L) {
            return "0%";
        }
        final double percent = this.finishCount / this.count;
        final NumberFormat format = NumberFormat.getPercentInstance();
        format.setMinimumFractionDigits(1);
        return format.format(percent);
    }
}
