// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.usercallback;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_report", noClassnameStored = true)
public class UserReport implements Serializable
{
    private static final long serialVersionUID = -1686505266362478252L;
    @Id
    private String tradeId;
    private String userReqNo;
    private String message;
    private Boolean isSuccess;
    private String username;
    private String mobile;
    private int size;
    private String productId;
    private String callbackUrl;
    @Indexed
    private int retryTimes;
    @Indexed
    private long lastTimestamp;
    private String pushFailReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    
    public UserReport() {
        this.createDate = new Date();
    }
    
    public String getTradeId() {
        return this.tradeId;
    }
    
    public void setTradeId(final String tradeId) {
        this.tradeId = tradeId;
    }
    
    public String getUserReqNo() {
        return this.userReqNo;
    }
    
    public void setUserReqNo(final String userReqNo) {
        this.userReqNo = userReqNo;
    }
    
    public int getRetryTimes() {
        return this.retryTimes;
    }
    
    public void setRetryTimes(final int retryTimes) {
        this.retryTimes = retryTimes;
    }
    
    public String getCallbackUrl() {
        return this.callbackUrl;
    }
    
    public void setCallbackUrl(final String callbackUrl) {
        this.callbackUrl = callbackUrl;
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
    
    public int getSize() {
        return this.size;
    }
    
    public void setSize(final int size) {
        this.size = size;
    }
    
    public String getProductId() {
        return this.productId;
    }
    
    public void setProductId(final String productId) {
        this.productId = productId;
    }
    
    public long getLastTimestamp() {
        return this.lastTimestamp;
    }
    
    public void setLastTimestamp(final long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }
    
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public String getMessage() {
        return this.isSuccess ? "\u5145\u503c\u6210\u529f" : SafeReportMessage.process(this.message);
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public Boolean getIsSuccess() {
        return this.isSuccess;
    }
    
    public void setIsSuccess(final Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    
    public String getPushFailReason() {
        return this.pushFailReason;
    }
    
    public void setPushFailReason(final String pushFailReason) {
        this.pushFailReason = pushFailReason;
    }
}
