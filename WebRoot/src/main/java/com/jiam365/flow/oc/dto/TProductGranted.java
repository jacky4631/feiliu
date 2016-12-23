// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.dto;

import com.jiam365.flow.server.product.UserProduct;
import com.jiam365.flow.server.entity.User;

public class TProductGranted
{
    private String productId;
    private String productName;
    private String username;
    private String displayUsername;
    private String company;
    private double discount;
    private String userProductId;
    
    public TProductGranted() {
    }
    
    public TProductGranted(final User user, final UserProduct product) {
        this.productId = product.getProductId();
        this.productName = product.getShortName();
        this.username = user.getUsername();
        this.displayUsername = user.getDisplayName();
        this.company = user.getCompany();
        this.discount = product.getDiscount();
        this.userProductId = product.getId();
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
    
    public String getCompany() {
        return this.company;
    }
    
    public void setCompany(final String company) {
        this.company = company;
    }
    
    public double getDiscount() {
        return this.discount;
    }
    
    public void setDiscount(final double discount) {
        this.discount = discount;
    }
    
    public String getUserProductId() {
        return this.userProductId;
    }
    
    public void setUserProductId(final String userProductId) {
        this.userProductId = userProductId;
    }
}
