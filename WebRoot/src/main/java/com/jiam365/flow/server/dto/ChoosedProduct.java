// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dto;

import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.flow.server.channel.ChannelConnection;
import com.jiam365.flow.server.product.FlowPackage;

public class ChoosedProduct
{
    public final FlowPackage flowPackage;
    public final ChannelConnection connection;
    
    public ChoosedProduct(final ChannelConnection connection, final FlowPackage flowPackage) {
        this.flowPackage = flowPackage;
        this.connection = connection;
    }
    
    public boolean isNull() {
        return this.flowPackage == null || this.connection == null;
    }
    
    public String getChannelProductProfileId() {
        final Long channelId = this.flowPackage.getFlowChannelId();
        final String groupCode = ProductIDHelper.productGroup(this.flowPackage.getProductId());
        return channelId + "-" + groupCode;
    }
    
    public String getOrigiProductId() {
        return this.flowPackage.getOrigiProductId();
    }
    
    public String getProductId() {
        return this.flowPackage.getProductId();
    }
    
    public double getCostDiscount() {
        return this.flowPackage.getDiscount();
    }
    
    public double getCostPrice() {
        return this.flowPackage.getPrice();
    }
}
