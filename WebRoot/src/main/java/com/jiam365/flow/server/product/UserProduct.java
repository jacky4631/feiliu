// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.product;

import com.jiam365.modules.telco.Telco;
import org.mongodb.morphia.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "flow_userproduct", noClassnameStored = true)
public class UserProduct implements Serializable
{
    private static final long serialVersionUID = -9172207565417861953L;
    @Id
    @NotBlank
    private String id;
    @Indexed
    private String username;
    @Indexed
    private String productId;
    private double discount;
    @Indexed
    private Telco provider;
    @Indexed
    private String scope;
    private String name;
    private String shortName;
    private int size;
    private double price;
    
    public UserProduct() {
        this.discount = 0.99;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
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
    
    public double getDiscount() {
        return this.discount;
    }
    
    public void setDiscount(final double discount) {
        this.discount = discount;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getShortName() {
        return this.shortName;
    }
    
    public void setShortName(final String shortName) {
        this.shortName = shortName;
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
}
