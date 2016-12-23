// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.product;

import com.jiam365.flow.sdk.utils.ProductIDHelper;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "flow_package", noClassnameStored = true)
public class FlowPackage implements Serializable
{
    private static final long serialVersionUID = 8999781273131592782L;
    @Id
    private String id;
    private String title;
    private String origiProductId;
    @Indexed
    private Long flowChannelId;
    @Indexed
    private String productId;
    private int size;
    private double price;
    private double discount;
    private double billAmount;
    private boolean enabled;
    private int priority;
    
    public String getBaseProductId() {
        return ProductIDHelper.baseProductId(this.productId);
    }
    
    public String getFullChannelProductGroupId() {
        final String groupCode = ProductIDHelper.productGroup(this.productId);
        return this.flowChannelId + "-" + groupCode;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getOrigiProductId() {
        return this.origiProductId;
    }
    
    public void setOrigiProductId(final String origiProductId) {
        this.origiProductId = origiProductId;
    }
    
    public Long getFlowChannelId() {
        return this.flowChannelId;
    }
    
    public void setFlowChannelId(final Long flowChannelId) {
        this.flowChannelId = flowChannelId;
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
    
    public int getPriority() {
        return this.priority;
    }
    
    public void setPriority(final int priority) {
        this.priority = priority;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public double getBillAmount() {
        return this.billAmount;
    }
    
    public void setBillAmount(final double billAmount) {
        this.billAmount = billAmount;
    }
}
