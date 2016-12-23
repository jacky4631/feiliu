// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RechareApplication
{
    @NotNull
    @Mobile
    private String mobile;
    private String username;
    @NotNull
    private String productId;
    private String userReqNo;
    
    public RechareApplication() {
        this.userReqNo = "";
    }
    
    public RechareApplication(final String username, final String mobile, final String productId) {
        this.userReqNo = "";
        this.username = username;
        this.mobile = mobile;
        this.productId = productId;
    }
    
    public void username(final String username) {
        this.username = username;
    }
    
    public String username() {
        return this.username;
    }
    
    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getProductId() {
        return this.productId;
    }
    
    public void setProductId(final String productId) {
        this.productId = productId;
    }
    
    public String getUserReqNo() {
        return this.userReqNo;
    }
    
    public void setUserReqNo(final String userReqNo) {
        this.userReqNo = userReqNo;
    }
    
    @Override
    public String toString() {
        return "RechareApplication{mobile='" + this.mobile + '\'' + ", username='" + this.username + '\'' + ", productId='" + this.productId + '\'' + '}';
    }
}
