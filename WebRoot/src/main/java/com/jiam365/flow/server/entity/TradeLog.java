// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import org.apache.commons.lang3.time.DateFormatUtils;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.server.channel.ChannelConnection;
import com.jiam365.flow.server.engine.Trade;
import com.jiam365.modules.json.FullJsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import com.jiam365.modules.telco.Telco;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "trade_log", noClassnameStored = true)
public class TradeLog implements Serializable
{
    private static final long serialVersionUID = -3850412676536274538L;
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAIL = -1;
    public static final int RESULT_TRADING = 9;
    @Id
    private String id;
    private String requestNo;
    @Indexed
    private String userRequestNo;
    @Indexed
    private Long channelId;
    private Telco provider;
    @Indexed
    private String stateCode;
    private String mobileInfo;
    private String channelName;
    @Indexed
    private String username;
    private String displayUsername;
    @Indexed
    private String mobile;
    @Indexed
    private String productId;
    private String productName;
    @Indexed
    private String origiProductId;
    private String executeProductId;
    private double costDiscount;
    private double costAmount;
    @Indexed
    @JsonSerialize(using = FullJsonDateSerializer.class)
    private Date startDate;
    @Indexed
    @JsonSerialize(using = FullJsonDateSerializer.class)
    private Date finishDate;
    @Indexed
    private int timeConsuming;
    private Date submitTime;
    @JsonSerialize(using = FullJsonDateSerializer.class)
    private Date channelFinishDate;
    @Indexed
    private int channelResult;
    private String channelMessage;
    private double price;
    private double executeProductPrice;
    private int size;
    @Indexed
    private int result;
    private String message;
    private double billDiscount;
    private double billAmount;
    private String remark;
    
    public TradeLog() {
        this.mobileInfo = "";
        this.displayUsername = "";
        this.channelResult = 9;
        this.result = 9;
        this.billDiscount = 1.0;
        this.billAmount = 0.0;
        this.remark = "";
    }
    
    public TradeLog(final Trade trade) {
        this.mobileInfo = "";
        this.displayUsername = "";
        this.channelResult = 9;
        this.result = 9;
        this.billDiscount = 1.0;
        this.billAmount = 0.0;
        this.remark = "";
        this.init(trade);
    }
    
    public TradeLog(final boolean isSuccess, final Trade trade) {
        this.mobileInfo = "";
        this.displayUsername = "";
        this.channelResult = 9;
        this.result = 9;
        this.billDiscount = 1.0;
        this.billAmount = 0.0;
        this.remark = "";
        this.init(trade);
        this.result = (isSuccess ? 0 : -1);
        this.channelResult = this.result;
    }
    
    private void init(final Trade trade) {
        this.id = trade.getTradeId();
        this.requestNo = trade.getRequestNo();
        this.userRequestNo = trade.getRequest().getUserReqNo();
        this.provider = trade.getRequest().getProvider();
        final ChannelConnection connection = trade.getConnection();
        if (connection != null) {
            this.channelId = connection.channelId();
            this.channelName = connection.getChannel().getName();
        }
        final RechargeRequest request = trade.getRequest();
        this.username = request.getUsername();
        this.mobile = request.getMobile();
        this.stateCode = request.getMobileInfo().getStateCode();
        this.mobileInfo = request.getMobileInfo().getMobileDetail();
        this.productId = request.getProductId();
        if (this.productId.endsWith("$")) {
            this.productName = request.getProductName() + "$";
        }
        else {
            this.productName = request.getProductName();
        }
        this.origiProductId = request.getOrigiProductId();
        this.executeProductId = request.getExecuteProductId();
        this.costDiscount = request.getOrigiDiscount();
        this.costAmount = ((-1 != this.channelResult) ? trade.getCost() : 0.0);
        this.price = request.getPrice();
        this.executeProductPrice = request.getExecuteProductPrice();
        this.size = request.getSize();
        this.startDate = trade.getStartTime();
        this.billDiscount = trade.getBillDiscount();
        this.billAmount = trade.getBillAmount();
        if (trade.isFinished()) {
            this.finishDate = new Date();
            this.message = trade.getLastMessage();
            this.timeConsuming = (int)(this.finishDate.getTime() - this.startDate.getTime()) / 1000;
        }
    }
    
    public boolean isTradeFinished() {
        return this.result == -1 || this.result == 0;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getChannelName() {
        return this.channelName;
    }
    
    public void setChannelName(final String channelName) {
        this.channelName = channelName;
    }
    
    public String getRequestNo() {
        return this.requestNo;
    }
    
    public void setRequestNo(final String requestNo) {
        this.requestNo = requestNo;
    }
    
    public String getUserRequestNo() {
        return this.userRequestNo;
    }
    
    public void setUserRequestNo(final String userRequestNo) {
        this.userRequestNo = userRequestNo;
    }
    
    public Long getChannelId() {
        return this.channelId;
    }
    
    public void setChannelId(final Long channelId) {
        this.channelId = channelId;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }
    
    public String getOrigiProductId() {
        return this.origiProductId;
    }
    
    public void setOrigiProductId(final String origiProductId) {
        this.origiProductId = origiProductId;
    }
    
    public String getProductId() {
        return this.productId;
    }
    
    public void setProductId(final String productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return this.productName;
    }
    
    public void setProductName(final String productName) {
        this.productName = productName;
    }
    
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getFinishDate() {
        return this.finishDate;
    }
    
    public void setFinishDate(final Date finishDate) {
        this.finishDate = finishDate;
    }
    
    public double getPrice() {
        return this.price;
    }
    
    public void setPrice(final double price) {
        this.price = price;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public void setSize(final int size) {
        this.size = size;
    }
    
    public int getResult() {
        return this.result;
    }
    
    public String getResultDescription() {
        String ret;
        if (this.result == 9) {
            ret = "\u5145\u503c\u4e2d";
        }
        else if (this.result == 0) {
            ret = "\u6210\u529f";
        }
        else {
            ret = "\u5931\u8d25";
        }
        return ret;
    }
    
    public void setResult(final int result) {
        this.result = result;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public Telco getProvider() {
        return this.provider;
    }
    
    public void setProvider(final Telco provider) {
        this.provider = provider;
    }
    
    public double getBillDiscount() {
        return this.billDiscount;
    }
    
    public void setBillDiscount(final double billDiscount) {
        this.billDiscount = billDiscount;
    }
    
    public double getBillAmount() {
        return this.billAmount;
    }
    
    public void setBillAmount(final double billAmount) {
        this.billAmount = billAmount;
    }
    
    public String getStateCode() {
        return this.stateCode;
    }
    
    public void setStateCode(final String stateCode) {
        this.stateCode = stateCode;
    }
    
    public String getMobileInfo() {
        return this.mobileInfo;
    }
    
    public void setMobileInfo(final String mobileInfo) {
        this.mobileInfo = mobileInfo;
    }
    
    public int getTimeConsuming() {
        return this.timeConsuming;
    }
    
    public void setTimeConsuming(final int timeConsuming) {
        this.timeConsuming = timeConsuming;
    }
    
    public String getDisplayUsername() {
        return this.displayUsername;
    }
    
    public void setDisplayUsername(final String displayUsername) {
        this.displayUsername = displayUsername;
    }
    
    public Date getChannelFinishDate() {
        return this.channelFinishDate;
    }
    
    public void setChannelFinishDate(final Date channelFinishDate) {
        this.channelFinishDate = channelFinishDate;
    }
    
    public int getChannelResult() {
        return this.channelResult;
    }
    
    public void setChannelResult(final int channelResult) {
        this.channelResult = channelResult;
    }
    
    public String getChannelMessage() {
        return this.channelMessage;
    }
    
    public void setChannelMessage(final String channelMessage) {
        this.channelMessage = channelMessage;
    }
    
    public String getExecuteProductId() {
        return this.executeProductId;
    }
    
    public void setExecuteProductId(final String executeProductId) {
        this.executeProductId = executeProductId;
    }
    
    public double getCostDiscount() {
        return this.costDiscount;
    }
    
    public void setCostDiscount(final double costDiscount) {
        this.costDiscount = costDiscount;
    }
    
    public double getCostAmount() {
        return this.costAmount;
    }
    
    public void setCostAmount(final double costAmount) {
        this.costAmount = costAmount;
    }
    
    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(final String remark) {
        this.remark = remark;
    }
    
    public Date getSubmitTime() {
        return this.submitTime;
    }
    
    public void setSubmitTime(final Date submitTime) {
        this.submitTime = submitTime;
    }
    
    public double getExecuteProductPrice() {
        return (this.executeProductPrice > 0.01) ? this.executeProductPrice : this.price;
    }
    
    public void setExecuteProductPrice(final double executeProductPrice) {
        this.executeProductPrice = executeProductPrice;
    }
    
    @Override
    public String toString() {
        return "{" + this.username + "@" + this.channelName + " ==> " + this.mobile + "<" + this.productId + ">" + " " + "(" + DateFormatUtils.format(this.startDate, "HH:mm:ss") + ")}";
    }
    
    public String asString() {
        return "TradeLog{id='" + this.id + '\'' + ", requestNo='" + this.requestNo + '\'' + ", channelId=" + this.channelId + ", channelName='" + this.channelName + '\'' + ", username='" + this.username + '\'' + ", mobile='" + this.mobile + '\'' + ", productId='" + this.productId + '\'' + ", startDate=" + this.startDate + ", finishDate=" + this.finishDate + ", price=" + this.price + ", size=" + this.size + ", result=" + this.result + ", message='" + this.message + '\'' + '}';
    }
}
