// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import com.jiam365.flow.server.entity.FlowInterceptor;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.sdk.RechargeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.FlowInterceptorManager;

public class RechargeInterceptor
{
    @Autowired
    private FlowInterceptorManager flowInterceptorManager;
    
    public boolean intercept(final RechargeRequest request) {
        final FlowInterceptor interceptor = this.flowInterceptorManager.get();
        if (interceptor == null) {
            return false;
        }
        if (interceptor.getUsers() != null && interceptor.getUsers().contains(request.getUsername())) {
            return true;
        }
        final String productId = request.getProductId();
        final String productIdPrefix = interceptor.getProductIdPrefix();
        if (StringUtils.isNotBlank((CharSequence)productIdPrefix)) {
            final String[] split;
            final String[] prefixArray = split = productIdPrefix.split(",");
            for (String prefix : split) {
                prefix = prefix.trim().replace("*", "");
                final String[] parts = prefix.split("-");
                if (productId.startsWith(parts[0])) {
                    return parts.length == 1 || StringUtils.equals((CharSequence)parts[1], (CharSequence)request.getMobileInfo().getStateCode());
                }
            }
        }
        final String telco = ProductIDHelper.code(productId);
        final String state = request.getMobileInfo().getStateCode();
        final String s = telco;
        switch (s) {
            case "8": {
                return interceptor.getCmcc().contains(state) || interceptor.getCmcc().contains("NA");
            }
            case "7": {
                return interceptor.getTelecom().contains(state) || interceptor.getTelecom().contains("NA");
            }
            case "9": {
                return interceptor.getUnicom().contains(state) || interceptor.getUnicom().contains("NA");
            }
            default: {
                return false;
            }
        }
    }
}
