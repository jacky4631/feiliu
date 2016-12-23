// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.route;

import com.jiam365.flow.server.product.FlowPackage;
import com.jiam365.flow.server.engine.ChooseRestrict;
import com.jiam365.flow.server.dto.ChoosedProduct;
import com.jiam365.flow.sdk.RechargeRequest;

public class IgnoreUsed implements Condition
{
    @Override
    public boolean pass(final RechargeRequest request, final ChoosedProduct choosedProduct, final ChooseRestrict... restricts) {
        if (restricts.length > 0) {
            final ChooseRestrict restrict = restricts[0];
            final FlowPackage flowPackage = choosedProduct.flowPackage;
            return !restrict.getExceptChannels().contains(flowPackage.getFullChannelProductGroupId());
        }
        return true;
    }
}
