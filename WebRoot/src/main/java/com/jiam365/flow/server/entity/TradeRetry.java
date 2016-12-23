// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.modules.utils.Identities;
import java.util.Date;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "trade_retry", noClassnameStored = true)
public class TradeRetry implements Serializable
{
    private static final long serialVersionUID = 2789872714659909225L;
    @Id
    private String id;
    @Indexed
    private String tradeId;
    private Long channelId;
    private String channelName;
    private String executeProductId;
    private Date finishDate;
    private String failReason;
    
    public TradeRetry() {
        this.finishDate = new Date();
        this.id = Identities.uuid2();
    }
    
    public String getFullChannelProductGroupId() {
        final String groupCode = ProductIDHelper.productGroup(this.executeProductId);
        return this.channelId + "-" + groupCode;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getTradeId() {
        return this.tradeId;
    }
    
    public void setTradeId(final String tradeId) {
        this.tradeId = tradeId;
    }
    
    public Long getChannelId() {
        return this.channelId;
    }
    
    public void setChannelId(final Long channelId) {
        this.channelId = channelId;
    }
    
    public String getChannelName() {
        return this.channelName;
    }
    
    public void setChannelName(final String channelName) {
        this.channelName = channelName;
    }
    
    public Date getFinishDate() {
        return this.finishDate;
    }
    
    public void setFinishDate(final Date finishDate) {
        this.finishDate = finishDate;
    }
    
    public String getFailReason() {
        return this.failReason;
    }
    
    public void setFailReason(final String failReason) {
        this.failReason = failReason;
    }
    
    public String getExecuteProductId() {
        return this.executeProductId;
    }
    
    public void setExecuteProductId(final String executeProductId) {
        this.executeProductId = executeProductId;
    }
}
