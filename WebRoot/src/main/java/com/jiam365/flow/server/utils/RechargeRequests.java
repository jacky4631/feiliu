// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.utils;

import com.jiam365.flow.server.dto.ChoosedProduct;
import com.jiam365.flow.server.product.UserProduct;
import com.jiam365.flow.server.product.FlowProduct;
import com.jiam365.flow.sdk.MobileInfo;
import com.jiam365.flow.sdk.RechargeRequest;

public class RechargeRequests
{
    public static void mobileAndProduct(final RechargeRequest request, final MobileInfo mobileInfo, final FlowProduct flowProduct, final UserProduct userProduct) {
        request.setMobileInfo(mobileInfo);
        request.setPrice(flowProduct.getPrice());
        request.setSize(flowProduct.getSize());
        request.setProvider(flowProduct.getProvider());
        request.setProductName(flowProduct.getShortName());
        request.setBillDiscount(userProduct.getDiscount());
    }
    
    public static void bindChannelPackage(final RechargeRequest request, final ChoosedProduct choosedProduct) {
        request.setOrigiProductId(choosedProduct.getOrigiProductId());
        request.setExecuteProductId(choosedProduct.getProductId());
        request.setOrigiDiscount(choosedProduct.getCostDiscount());
        request.setExecuteProductPrice(choosedProduct.getCostPrice());
    }
}
