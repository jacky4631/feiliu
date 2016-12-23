// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import com.jiam365.flow.server.channel.ChannelConnection;
import com.jiam365.flow.server.channel.ChannelConnectionManager;
import com.jiam365.flow.server.engine.TradeCenter;
import com.jiam365.flow.server.engine.Trade;
import com.jiam365.modules.telco.Telco;
import java.util.Date;
import com.jiam365.flow.sdk.RechargeRequest;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "trade_pending", noClassnameStored = true)
public class PendingTrade implements Serializable
{
    private static final long serialVersionUID = -3954475951400253901L;
    @Id
    private String tradeId;
    private String requestNo;
    private RechargeRequest rechargeRequest;
    private Date startTime;
    private Telco provider;
    private Long channelId;
    private int status;
    private String lastMessage;
    private Double billDiscount;
    private Double billAmount;
    
    public PendingTrade() {
        this.lastMessage = "";
        this.billDiscount = 1.0;
        this.billAmount = 0.0;
    }
    
    public PendingTrade(final Trade trade) {
        this.lastMessage = "";
        this.billDiscount = 1.0;
        this.billAmount = 0.0;
        this.startTime = trade.getStartTime();
        this.tradeId = trade.getTradeId();
        this.requestNo = trade.getRequestNo();
        this.rechargeRequest = trade.getRequest();
        if (trade.getConnection() != null) {
            this.channelId = trade.getConnection().channelId();
        }
        this.provider = trade.getRequest().getProvider();
        this.status = trade.getStatus();
        this.lastMessage = trade.getLastMessage();
        this.billDiscount = trade.getBillDiscount();
        this.billAmount = trade.getBillAmount();
    }
    
    public Trade toTrade(final TradeCenter central, final ChannelConnectionManager manager) {
        ChannelConnection connection = null;
        if (this.channelId != null) {
            connection = manager.getConnection(this.channelId);
        }
        return Trade.restoreTrade(central, connection, this);
    }
    
    public Date getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }
    
    public String getTradeId() {
        return this.tradeId;
    }
    
    public void setTradeId(final String tradeId) {
        this.tradeId = tradeId;
    }
    
    public String getRequestNo() {
        return this.requestNo;
    }
    
    public void setRequestNo(final String requestNo) {
        this.requestNo = requestNo;
    }
    
    public RechargeRequest getRechargeRequest() {
        return this.rechargeRequest;
    }
    
    public void setRechargeRequest(final RechargeRequest rechargeRequest) {
        this.rechargeRequest = rechargeRequest;
    }
    
    public Long getChannelId() {
        return this.channelId;
    }
    
    public void setChannelId(final Long channelId) {
        this.channelId = channelId;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(final int status) {
        this.status = status;
    }
    
    public String getLastMessage() {
        return this.lastMessage;
    }
    
    public void setLastMessage(final String lastMessage) {
        this.lastMessage = lastMessage;
    }
    
    public Telco getProvider() {
        return this.provider;
    }
    
    public void setProvider(final Telco provider) {
        this.provider = provider;
    }
    
    public Double getBillDiscount() {
        return this.billDiscount;
    }
    
    public void setBillDiscount(final Double billDiscount) {
        this.billDiscount = billDiscount;
    }
    
    public Double getBillAmount() {
        return this.billAmount;
    }
    
    public void setBillAmount(final Double billAmount) {
        this.billAmount = billAmount;
    }
}
