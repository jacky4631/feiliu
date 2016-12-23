package com.jiam365.flow.sdk;

import com.jiam365.modules.telco.Telco;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class RechargeRequest
        implements Serializable
{
    private static final long serialVersionUID = 2226280849557258579L;
    private String username;
    private String mobile;
    private String userReqNo;
    private Telco provider;
    private MobileInfo mobileInfo;
    private String productId;
    private String productName;
    private int size;
    private double price;
    private double billDiscount;
    private String executeProductId;
    private double executeProductPrice;
    private double origiDiscount;
    private String origiProductId;
    private long submitTime;
    private Date startTime;
    private String tradeId;

    public RechargeRequest() {}

    public RechargeRequest(String username, String mobile, String productId, String userReqNo)
    {
        setMobile(mobile);
        setUsername(username);
        setProductId(productId);
        setUserReqNo(userReqNo);
    }

    public String getMobile()
    {
        return this.mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public String getProductName()
    {
        return this.productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getOrigiProductId()
    {
        return this.origiProductId;
    }

    public void setOrigiProductId(String origiProductId)
    {
        this.origiProductId = origiProductId;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public int getSize()
    {
        return this.size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public double getPrice()
    {
        return this.price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public MobileInfo getMobileInfo()
    {
        return this.mobileInfo;
    }

    public void setMobileInfo(MobileInfo mobileInfo)
    {
        this.mobileInfo = mobileInfo;
    }

    public Telco getProvider()
    {
        return this.provider;
    }

    public void setProvider(Telco provider)
    {
        this.provider = provider;
    }

    public boolean isReplaceNaWithState()
    {
        return !StringUtils.equals(this.executeProductId, this.productId);
    }

    public String getExecuteProductId()
    {
        return this.executeProductId;
    }

    public void setExecuteProductId(String executeProductId)
    {
        this.executeProductId = executeProductId;
    }

    public double getOrigiDiscount()
    {
        return this.origiDiscount;
    }

    public void setOrigiDiscount(double origiDiscount)
    {
        this.origiDiscount = origiDiscount;
    }

    public double getBillDiscount()
    {
        return this.billDiscount;
    }

    public void setBillDiscount(double billDiscount)
    {
        this.billDiscount = billDiscount;
    }

    public double getExecuteProductPrice()
    {
        return this.executeProductPrice;
    }

    public void setExecuteProductPrice(double executeProductPrice)
    {
        this.executeProductPrice = executeProductPrice;
    }

    public String getUserReqNo()
    {
        return this.userReqNo;
    }

    public void setUserReqNo(String userReqNo)
    {
        this.userReqNo = userReqNo;
    }

    public long getSubmitTime()
    {
        return this.submitTime;
    }

    public void setSubmitTime(long submitTime)
    {
        this.submitTime = submitTime;
    }

    public Date getStartTime()
    {
        return this.startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public String getTradeId()
    {
        return this.tradeId;
    }

    public void setTradeId(String tradeId)
    {
        this.tradeId = tradeId;
    }

    public String toString()
    {
        return "RechargeRequest{mobile='" + this.mobile + '\'' + ", mobileInfo=" + this.mobileInfo + ", productId='" + this.productId + '\'' + ", username='" + this.username + '\'' + ", size=" + this.size + ", price=" + this.price + '}';
    }
}
