//
// Decompiled by Procyon v0.5.30
//

package com.jiam365.flow.agent.dto;

import com.jiam365.modules.json.FullJsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import com.jiam365.modules.telco.Telco;

public class TTradeLog
{
    private String id;
    private String requestNo;
    private String userRequestNo;
    private Telco provider;
    private String mobileInfo;
    private String username;
    private String displayUsername;
    private String mobile;
    private String productId;
    private String productName;
    @JsonSerialize(using = FullJsonDateSerializer.class)
    private Date startDate;
    @JsonSerialize(using = FullJsonDateSerializer.class)
    private Date finishDate;
    private int timeConsuming;
    private double price;
    private int size;
    private int result;
    private String message;
    private double billDiscount;
    private double billAmount;

    public TTradeLog() {
        this.mobileInfo = "";
        this.displayUsername = "";
        this.billDiscount = 1.0;
        this.billAmount = 0.0;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
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

    public Telco getProvider() {
        return this.provider;
    }

    public void setProvider(final Telco provider) {
        this.provider = provider;
    }

    public String getMobileInfo() {
        return this.mobileInfo;
    }

    public void setMobileInfo(final String mobileInfo) {
        this.mobileInfo = mobileInfo;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getDisplayUsername() {
        return this.displayUsername;
    }

    public void setDisplayUsername(final String displayUsername) {
        this.displayUsername = displayUsername;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(final String mobile) {
        this.mobile = mobile;
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

    public int getTimeConsuming() {
        return this.timeConsuming;
    }

    public void setTimeConsuming(final int timeConsuming) {
        this.timeConsuming = timeConsuming;
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

    public void setResult(final int result) {
        this.result = result;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
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
}
