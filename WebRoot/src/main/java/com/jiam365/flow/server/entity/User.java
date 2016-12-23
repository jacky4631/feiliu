// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ArrayList;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.mongodb.morphia.annotations.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import org.mongodb.morphia.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_user", noClassnameStored = true)
public class User implements Serializable
{
    private static final long serialVersionUID = 9032487479052512892L;
    @Id
    private Long id;
    @NotBlank
    @Indexed(unique = true)
    private String username;
    private String displayName;
    private String callbackUrl;
    private String allowIps;
    private String orgcode;
    private String company;
    private String linkman;
    private String mobile;
    private boolean sendSm;
    private double smPrice;
    private boolean foreSkipPriceProtected;
    private String email;
    @JsonIgnore
    private List<String> roles;
    @JsonIgnore
    private List<String> allowChannelProducts;
    @JsonIgnore
    private List<String> grantedSpecialChannelProducts;
    @JsonIgnore
    private String allowChannelGroup;
    @JsonIgnore
    private String password;
    @Transient
    @JsonIgnore
    private String plainPassword;
    private String authToken;
    @JsonIgnore
    private String salt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerDate;
    private boolean sendBillFile;
    @JsonIgnore
    private BillFileAccount billFileAccount;
    private int status;
    public static final int STATUS_ENABLED = 0;
    public static final int STATUS_DISABLED = -1;
    public static final int TYPE_AGENT = 0;
    public static final int TYPE_OPERATOR = 1;
    private int userType;
    
    public User() {
        this.callbackUrl = "";
        this.allowIps = "";
        this.smPrice = 5.0;
        this.roles = new ArrayList<String>();
        this.allowChannelProducts = new LinkedList<String>();
        this.grantedSpecialChannelProducts = new LinkedList<String>();
        this.userType = 0;
    }
    
    public boolean isNew() {
        return this.id == null;
    }
    
    public boolean isEnabled() {
        return this.status == 0;
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
    
    public String getCallbackUrl() {
        return this.callbackUrl;
    }
    
    public void setCallbackUrl(final String callbackUrl) {
        if (callbackUrl != null) {
            this.callbackUrl = callbackUrl.trim();
        }
    }
    
    public String getOrgcode() {
        return this.orgcode;
    }
    
    public void setOrgcode(final String orgcode) {
        this.orgcode = orgcode;
    }
    
    public String getCompany() {
        return (this.company == null) ? "" : this.company;
    }
    
    public void setCompany(final String company) {
        this.company = company;
    }
    
    public List<String> getRoles() {
        final List<String> ret = Collections.emptyList();
        return (this.roles == null) ? ret : this.roles;
    }
    
    public void setRoles(final List<String> roles) {
        this.roles = roles;
    }
    
    public void addRole(final String roleCode) {
        if (this.roles == null) {
            this.roles = new ArrayList<String>();
        }
        if (!this.roles.contains(roleCode)) {
            this.roles.add(roleCode);
        }
    }
    
    public boolean hasRole(final String roleCode) {
        final List<String> roles = this.getRoles();
        return roles.contains(roleCode);
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public String getPlainPassword() {
        return this.plainPassword;
    }
    
    public void setPlainPassword(final String plainPassword) {
        this.plainPassword = plainPassword;
    }
    
    public String getSalt() {
        return this.salt;
    }
    
    public void setSalt(final String salt) {
        this.salt = salt;
    }
    
    public String getAuthToken() {
        return this.authToken;
    }
    
    public void setAuthToken(final String authToken) {
        this.authToken = authToken;
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
    
    public boolean isSendSm() {
        return this.sendSm;
    }
    
    public void setSendSm(final boolean sendSm) {
        this.sendSm = sendSm;
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
    
    public String getAllowIps() {
        return this.allowIps;
    }
    
    public void setAllowIps(final String allowIps) {
        this.allowIps = allowIps;
    }
    
    public String getAllowChannelGroup() {
        return this.allowChannelGroup;
    }
    
    public void setAllowChannelGroup(final String allowChannelGroup) {
        this.allowChannelGroup = allowChannelGroup;
    }
    
    public double getSmPrice() {
        return this.smPrice;
    }
    
    public void setSmPrice(final double smPrice) {
        this.smPrice = smPrice;
    }
    
    public int getUserType() {
        return this.userType;
    }
    
    public void setUserType(final int userType) {
        this.userType = userType;
    }
    
    public boolean isSendBillFile() {
        return this.sendBillFile;
    }
    
    public void setSendBillFile(final boolean sendBillFile) {
        this.sendBillFile = sendBillFile;
    }
    
    public BillFileAccount getBillFileAccount() {
        return this.billFileAccount;
    }
    
    public void setBillFileAccount(final BillFileAccount billFileAccount) {
        this.billFileAccount = billFileAccount;
    }
    
    public String[] getAllowChannelProducts() {
        if (this.allowChannelProducts.size() > 0) {
            final String[] array = new String[this.allowChannelProducts.size()];
            return this.allowChannelProducts.toArray(array);
        }
        return new String[0];
    }
    
    public List<String> getAllowChannelProductList() {
        return this.allowChannelProducts;
    }
    
    public void setAllowChannelProducts(final List<String> allowChannelProducts) {
        this.allowChannelProducts = allowChannelProducts;
    }
    
    public List<String> getGrantedSpecialChannelProducts() {
        return this.grantedSpecialChannelProducts;
    }
    
    public void setGrantedSpecialChannelProducts(final List<String> grantedSpecialChannelProducts) {
        this.grantedSpecialChannelProducts = grantedSpecialChannelProducts;
    }
    
    public boolean isForeSkipPriceProtected() {
        return this.foreSkipPriceProtected;
    }
    
    public void setForeSkipPriceProtected(final boolean foreSkipPriceProtected) {
        this.foreSkipPriceProtected = foreSkipPriceProtected;
    }
}
