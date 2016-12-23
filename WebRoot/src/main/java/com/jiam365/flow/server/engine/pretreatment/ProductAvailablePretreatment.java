// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.pretreatment;

import com.jiam365.flow.server.rest.RestException;
import com.jiam365.flow.server.product.FlowProduct;
import com.jiam365.flow.server.product.UserProduct;
import com.jiam365.flow.sdk.MobileInfo;
import com.jiam365.flow.server.utils.RechargeRequests;
import com.jiam365.flow.server.engine.TradeException;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.server.product.FlowProductManager;
import com.jiam365.flow.server.product.UserProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.MobileService;

public class ProductAvailablePretreatment implements Pretreatment
{
    @Autowired
    private MobileService mobileService;
    @Autowired
    private UserProductManager userProductManager;
    @Autowired
    private FlowProductManager flowProductManager;
    
    @Override
    public void check(final RechargeRequest request) throws RestException {
        final MobileInfo info = this.mobileService.mobileInfo(request.getMobile());
        final String productId = request.getProductId();
        final UserProduct userProduct = this.userProductManager.getUserProduct(productId, request.getUsername());
        final String baseProductId = ProductIDHelper.baseProductId(productId);
        final FlowProduct flowProduct = this.flowProductManager.get(baseProductId);
        if (userProduct == null || flowProduct == null || !flowProduct.isEnabled()) {
            throw new TradeException(productId + "\u4e0d\u5b58\u5728\u6216\u672a\u6388\u6743\u4f7f\u7528");
        }
        RechargeRequests.mobileAndProduct(request, info, flowProduct, userProduct);
    }
}
