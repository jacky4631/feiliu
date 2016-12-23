//
// Decompiled by Procyon v0.5.30
//

package com.jiam365.flow.agent.dto;

import java.text.NumberFormat;
import com.jiam365.flow.server.product.UserProduct;
import com.jiam365.flow.server.product.FlowProduct;
import java.io.Serializable;

public class TFlowProduct implements Serializable
{
    private static final long serialVersionUID = 8321529542012800377L;
    private String id;
    private String name;
    private int size;
    private double price;
    private boolean available;
    private double discount;

    public TFlowProduct() {
        this.discount = 1.0;
    }

    public TFlowProduct(final FlowProduct flowProduct) {
        this.discount = 1.0;
        this.name = flowProduct.getShortName();
        this.id = flowProduct.getId();
        this.size = flowProduct.getSize();
        this.price = flowProduct.getPrice();
    }

    public TFlowProduct(final UserProduct userProduct) {
        this.discount = 1.0;
        this.name = userProduct.getShortName();
        this.id = userProduct.getProductId();
        this.size = userProduct.getSize();
        this.price = userProduct.getPrice();
        this.discount = userProduct.getDiscount();
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
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

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(final boolean available) {
        this.available = available;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getDiscount() {
        return this.discount;
    }

    public String getDiscountPercent() {
        final NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(1);
        return nf.format(this.discount);
    }

    public void setDiscount(final double discount) {
        this.discount = discount;
    }
}
