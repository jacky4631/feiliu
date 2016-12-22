package com.jiam365.flow.agent.dto;

import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.modules.telco.Telco;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Map;

public class TDashboard
{
    private double balance;
    Map<Telco, Double> billAmountMap;
    private Map<Telco, Long> successTotal;
    private Map<Telco, Long> failTotal;
    private Map<Telco, Long> pendingTotal;
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
    private Date time = new Date();
    private double cmccAmount = 0.0D;
    private double telecomAmount = 0.0D;
    private double unicomAmount = 0.0D;

    public Long getTotalCount()
    {
        long count = 0L;
        for (Telco provider : this.successTotal.keySet()) {
            count += ((Long)this.successTotal.get(provider)).longValue();
        }
        for (Telco provider : this.failTotal.keySet()) {
            count += ((Long)this.failTotal.get(provider)).longValue();
        }
        for (Telco provider : this.pendingTotal.keySet()) {
            count += ((Long)this.pendingTotal.get(provider)).longValue();
        }
        return Long.valueOf(count);
    }

    public String getFinishPercent()
    {
        long totalCount = getTotalCount().longValue();
        if (totalCount == 0L) {
            return "0%";
        }
        double percent = (this.unicomSuccess + this.cmccSuccess + this.telecomSuccess) / totalCount;
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMinimumFractionDigits(1);
        return format.format(percent);
    }

    public double getBalance()
    {
        return this.balance;
    }

    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    public Map<Telco, Long> getSuccessTotal()
    {
        return this.successTotal;
    }

    public void setSuccessTotal(Map<Telco, Long> successTotal)
    {
        this.successTotal = successTotal;
        this.cmccSuccess = safeLong((Long)successTotal.get(Telco.CMCC));
        this.telecomSuccess = safeLong((Long)successTotal.get(Telco.TELECOM));
        this.unicomSuccess = safeLong((Long)successTotal.get(Telco.UNICOM));
    }

    public Map<Telco, Long> getFailTotal()
    {
        return this.failTotal;
    }

    public void setFailTotal(Map<Telco, Long> failTotal)
    {
        this.failTotal = failTotal;
        this.cmccFail = safeLong((Long)failTotal.get(Telco.CMCC));
        this.telecomFail = safeLong((Long)failTotal.get(Telco.TELECOM));
        this.unicomFail = safeLong((Long)failTotal.get(Telco.UNICOM));
    }

    public Map<Telco, Long> getPendingTotal()
    {
        return this.pendingTotal;
    }

    public void setPendingTotal(Map<Telco, Long> pendingTotal)
    {
        this.pendingTotal = pendingTotal;
        this.cmccPending = safeLong((Long)pendingTotal.get(Telco.CMCC));
        this.telecomPending = safeLong((Long)pendingTotal.get(Telco.TELECOM));
        this.unicomPending = safeLong((Long)pendingTotal.get(Telco.UNICOM));
    }

    public void setBillAmountMap(Map<Telco, Double> billAmountMap)
    {
        this.billAmountMap = billAmountMap;
        double amount = 0.0D;
        for (Telco provider : billAmountMap.keySet())
        {
            double current = ((Double)billAmountMap.get(provider)).doubleValue();
            amount = DoubleUtils.add(amount, current);
            switch (provider)
            {
                case CMCC:
                    this.cmccAmount = DoubleUtils.round(current, 2);
                    break;
                case TELECOM:
                    this.telecomAmount = DoubleUtils.round(current, 2);
                    break;
                case UNICOM:
                    this.unicomAmount = DoubleUtils.round(current, 2);
            }
        }
        this.billAmount = DoubleUtils.round(amount, 2);
    }

    public Double getBillAmount()
    {
        return Double.valueOf(this.billAmount);
    }

    public long getCmccSuccess()
    {
        return this.cmccSuccess;
    }

    public long getCmccFail()
    {
        return this.cmccFail;
    }

    public long getCmccPending()
    {
        return this.cmccPending;
    }

    public long getUnicomSuccess()
    {
        return this.unicomSuccess;
    }

    public long getUnicomFail()
    {
        return this.unicomFail;
    }

    public long getUnicomPending()
    {
        return this.unicomPending;
    }

    public long getTelecomSuccess()
    {
        return this.telecomSuccess;
    }

    public long getTelecomFail()
    {
        return this.telecomFail;
    }

    public long getTelecomPending()
    {
        return this.telecomPending;
    }

    private long safeLong(Long value)
    {
        return value == null ? 0L : value.longValue();
    }

    public double getCmccAmount()
    {
        return this.cmccAmount;
    }

    public double getTelecomAmount()
    {
        return this.telecomAmount;
    }

    public double getUnicomAmount()
    {
        return this.unicomAmount;
    }

    public Date getTime()
    {
        return this.time;
    }
}
