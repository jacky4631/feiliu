// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.pretreatment;

import com.jiam365.flow.server.rest.RestException;
import com.jiam365.modules.telco.Telco;
import com.jiam365.flow.sdk.MobileInfo;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.server.engine.TradeException;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.flow.sdk.RechargeRequest;

public class ProductCompatibilityPretreatment implements Pretreatment
{
    @Override
    public void check(final RechargeRequest request) throws RestException {
        boolean compatibility = true;
        final String message = "\u624b\u673a\u53f7\u7801\u548c\u5145\u503c\u5305\u4e0d\u517c\u5bb9,\u65e0\u6cd5\u5145\u503c";
        final MobileInfo info = request.getMobileInfo();
        final String productId = request.getProductId();
        final Telco telco = ProductIDHelper.telco(productId);
        if (telco == null) {
            throw new TradeException("\u624b\u673a\u53f7\u7801\u548c\u5145\u503c\u5305\u4e0d\u517c\u5bb9,\u65e0\u6cd5\u5145\u503c");
        }
        if (!telco.equals((Object)info.getProvider())) {
            compatibility = false;
        }
        else {
            final String productStateCode = ProductIDHelper.stateCode(productId);
            final String mobileStateCode = info.getStateCode();
            if (!"NA".equals(productStateCode) && StringUtils.isNotBlank((CharSequence)mobileStateCode) && !productStateCode.equals(mobileStateCode)) {
                compatibility = false;
            }
        }
        if (!compatibility) {
            throw new TradeException("\u624b\u673a\u53f7\u7801\u548c\u5145\u503c\u5305\u4e0d\u517c\u5bb9,\u65e0\u6cd5\u5145\u503c");
        }
    }
}
