// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dto;

import com.jiam365.modules.telco.Telco;
import java.io.Serializable;

public class TUserProduct implements Serializable
{
    private static final long serialVersionUID = -8912101577406836558L;
    private String productId;
    private Telco provider;
    private String scope;
    private String name;
    private int size;
    private double price;
    private double discount;
    
    public TUserProduct() {
        this.discount = 0.99;
    }
    
    public String getProductId() {
        return this.productId;
    }
    
    public void setProductId(final String productId) {
        this.productId = productId;
    }
    
    public double getDiscount() {
        return this.discount;
    }
    
    public void setDiscount(final double discount) {
        this.discount = discount;
    }
    
    public Telco getProvider() {
        return this.provider;
    }
    
    public void setProvider(final Telco provider) {
        this.provider = provider;
    }
    
    public String getScope() {
        return this.scope;
    }
    
    public void setScope(final String scope) {
        this.scope = scope;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public void setSize(final int size) {
        this.size = size;
    }
    
    public double getPrice() {
        return this.price;
    }
    
    public void setPrice(final double price) {
        this.price = price;
    }
}
