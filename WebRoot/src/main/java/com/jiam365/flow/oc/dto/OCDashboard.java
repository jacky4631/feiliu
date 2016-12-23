// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.dto;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import com.jiam365.flow.server.utils.DoubleUtils;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Collections;
import java.util.Date;
import com.jiam365.flow.server.dao.TradeLogDao;
import com.jiam365.flow.server.dto.TradeCountByChannelName;
import java.util.List;
import com.jiam365.modules.telco.Telco;
import java.util.Map;

public class OCDashboard
{
    private Map<Telco, Long> successTotal;
    private Map<Telco, Long> failTotal;
    private Map<Telco, Long> pendingTotal;
    private List<TradeCountByChannelName> tradeCountByChannels;
    private List<TradeLogDao.TradeCountByScope> notFinishTradeCountByScope;
    private List<Double> profits;
    private long cmccSuccess;
    private long cmccFail;
    private long cmccPending;
    private long unicomSuccess;
    private long unicomFail;
    private long unicomPending;
    private long telecomSuccess;
    private long telecomFail;
    private long telecomPending;
    private double billAmount;
    private double costAmount;
    private Date time;
    private double cmccAmount;
    private double telecomAmount;
    private double unicomAmount;
    
    public OCDashboard() {
        this.notFinishTradeCountByScope = Collections.emptyList();
        this.time = new Date();
        this.cmccAmount = 0.0;
        this.telecomAmount = 0.0;
        this.unicomAmount = 0.0;
    }
    
    public Long getCmccCount() {
        return this.cmccPending + this.cmccSuccess;
    }
    
    public Long getTelecomCount() {
        return this.telecomPending + this.telecomSuccess;
    }
    
    public Long getUnicomCount() {
        return this.unicomPending + this.unicomSuccess;
    }
    
    public String getProfitsStr() {
        final StringBuilder sb = new StringBuilder(200);
        if (this.profits != null) {
            for (final Double d : this.profits) {
                sb.append(",").append(d);
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(0);
            }
        }
        return sb.toString();
    }
    
    public Long getTotalCount() {
        long count = 0L;
        for (final Telco provider : this.successTotal.keySet()) {
            count += this.successTotal.get(provider);
        }
        for (final Telco provider : this.failTotal.keySet()) {
            count += this.failTotal.get(provider);
        }
        for (final Telco provider : this.pendingTotal.keySet()) {
            count += this.pendingTotal.get(provider);
        }
        return count;
    }
    
    public Long getPendingCount() {
        return this.cmccPending + this.unicomPending + this.telecomPending;
    }
    
    public String getSuccessPercent() {
        final long totalCount = this.getTotalCount();
        if (totalCount == 0L) {
            return "0%";
        }
        final double percent = (this.unicomSuccess + this.cmccSuccess + this.telecomSuccess) / totalCount;
        final NumberFormat format = NumberFormat.getPercentInstance();
        format.setMinimumFractionDigits(1);
        return format.format(percent);
    }
    
    public String getFinishPercent() {
        final long totalCount = this.getTotalCount();
        if (totalCount == 0L) {
            return "0%";
        }
        final double percent = (this.unicomSuccess + this.cmccSuccess + this.telecomSuccess + this.unicomFail + this.cmccFail + this.telecomFail) / totalCount;
        final NumberFormat format = NumberFormat.getPercentInstance();
        format.setMinimumFractionDigits(1);
        return format.format(percent);
    }
    
    public String getPendingPercent() {
        final long totalCount = this.getTotalCount();
        if (totalCount == 0L) {
            return "0%";
        }
        final double percent = (this.cmccPending + this.unicomPending + this.telecomPending) / totalCount;
        final NumberFormat format = NumberFormat.getPercentInstance();
        format.setMinimumFractionDigits(1);
        return format.format(percent);
    }
    
    public Map<Telco, Long> getSuccessTotal() {
        return this.successTotal;
    }
    
    public void setSuccessTotal(final Map<Telco, Long> successTotal) {
        this.successTotal = successTotal;
        this.cmccSuccess = this.safeLong(successTotal.get(Telco.CMCC));
        this.telecomSuccess = this.safeLong(successTotal.get(Telco.TELECOM));
        this.unicomSuccess = this.safeLong(successTotal.get(Telco.UNICOM));
    }
    
    public Map<Telco, Long> getFailTotal() {
        return this.failTotal;
    }
    
    public void setFailTotal(final Map<Telco, Long> failTotal) {
        this.failTotal = failTotal;
        this.cmccFail = this.safeLong(failTotal.get(Telco.CMCC));
        this.telecomFail = this.safeLong(failTotal.get(Telco.TELECOM));
        this.unicomFail = this.safeLong(failTotal.get(Telco.UNICOM));
    }
    
    public Map<Telco, Long> getPendingTotal() {
        return this.pendingTotal;
    }
    
    public void setPendingTotal(final Map<Telco, Long> pendingTotal) {
        this.pendingTotal = pendingTotal;
        this.cmccPending = this.safeLong(pendingTotal.get(Telco.CMCC));
        this.telecomPending = this.safeLong(pendingTotal.get(Telco.TELECOM));
        this.unicomPending = this.safeLong(pendingTotal.get(Telco.UNICOM));
    }
    
    public void setBillAmountMap(final Map<Telco, Double> billAmountMap) {
        double amount = 0.0;
        for (final Telco provider : billAmountMap.keySet()) {
            final double current = billAmountMap.get(provider);
            amount = DoubleUtils.add(amount, current);
            switch (provider) {
                case CMCC: {
                    this.cmccAmount = DoubleUtils.round(current, 2);
                    continue;
                }
                case TELECOM: {
                    this.telecomAmount = DoubleUtils.round(current, 2);
                    continue;
                }
                case UNICOM: {
                    this.unicomAmount = DoubleUtils.round(current, 2);
                    continue;
                }
            }
        }
        this.billAmount = DoubleUtils.round(amount, 2);
    }
    
    public Double getBillAmount() {
        return this.billAmount;
    }
    
    public long getCmccSuccess() {
        return this.cmccSuccess;
    }
    
    public long getCmccFail() {
        return this.cmccFail;
    }
    
    public long getCmccPending() {
        return this.cmccPending;
    }
    
    public long getUnicomSuccess() {
        return this.unicomSuccess;
    }
    
    public long getUnicomFail() {
        return this.unicomFail;
    }
    
    public long getUnicomPending() {
        return this.unicomPending;
    }
    
    public long getTelecomSuccess() {
        return this.telecomSuccess;
    }
    
    public long getTelecomFail() {
        return this.telecomFail;
    }
    
    public long getTelecomPending() {
        return this.telecomPending;
    }
    
    private long safeLong(final Long value) {
        return (value == null) ? 0L : value;
    }
    
    public double getCmccAmount() {
        return this.cmccAmount;
    }
    
    public double getTelecomAmount() {
        return this.telecomAmount;
    }
    
    public double getUnicomAmount() {
        return this.unicomAmount;
    }
    
    public Date getTime() {
        return this.time;
    }
    
    public List<TradeCountByChannelName> getTradeCountByChannels() {
        return this.tradeCountByChannels;
    }
    
    public void setTradeCountByChannels(final List<TradeCountByChannelName> tradeCountByChannels) {
        this.tradeCountByChannels = tradeCountByChannels;
    }
    
    public double getCostAmount() {
        return this.costAmount;
    }
    
    public void setCostAmount(final double costAmount) {
        this.costAmount = costAmount;
    }
    
    public List<Double> getProfits() {
        return this.profits;
    }
    
    public void setProfits(final List<Double> profits) {
        this.profits = profits;
    }
    
    public List<TradeLogDao.TradeCountByScope> getNotFinishTradeCountByScope() {
        return this.notFinishTradeCountByScope;
    }
    
    public void setNotFinishTradeCountByScope(final List<TradeLogDao.TradeCountByScope> notFinishTradeCountByScope) {
        this.notFinishTradeCountByScope = notFinishTradeCountByScope;
    }
}
