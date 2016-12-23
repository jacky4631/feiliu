// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class UserInfo
{
    private Long id;
    private String username;
    private String displayName;
    private String company;
    private String orgcode;
    private String linkman;
    private String mobile;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date registerDate;
    private int status;
    private Double balance;
    private Double creditLine;
    
    public UserInfo() {
        this.balance = 0.0;
        this.creditLine = 0.0;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public String getCompany() {
        return this.company;
    }
    
    public void setCompany(final String company) {
        this.company = company;
    }
    
    public String getOrgcode() {
        return this.orgcode;
    }
    
    public void setOrgcode(final String orgcode) {
        this.orgcode = orgcode;
    }
    
    public String getLinkman() {
        return this.linkman;
    }
    
    public void setLinkman(final String linkman) {
        this.linkman = linkman;
    }
    
    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public Date getRegisterDate() {
        return this.registerDate;
    }
    
    public void setRegisterDate(final Date registerDate) {
        this.registerDate = registerDate;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(final int status) {
        this.status = status;
    }
    
    public Double getBalance() {
        return this.balance;
    }
    
    public void setBalance(final Double balance) {
        this.balance = balance;
    }
    
    public Double getCreditLine() {
        return this.creditLine;
    }
    
    public void setCreditLine(final Double creditLine) {
        this.creditLine = creditLine;
    }
}
