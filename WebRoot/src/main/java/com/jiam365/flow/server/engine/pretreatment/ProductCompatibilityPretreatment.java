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
        final String message = "手机号码和充值包不兼容,无法充值";
        final MobileInfo info = request.getMobileInfo();
        final String productId = request.getProductId();
        final Telco telco = ProductIDHelper.telco(productId);
        if (telco == null) {
            throw new TradeException("手机号码和充值包不兼容,无法充值");
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
            throw new TradeException("手机号码和充值包不兼容,无法充值");
        }
    }
}
