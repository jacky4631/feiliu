// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.route;

import com.jiam365.flow.server.entity.ChannelProductGroupProfile;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.flow.server.engine.ChooseRestrict;
import com.jiam365.flow.server.dto.ChoosedProduct;
import com.jiam365.flow.sdk.RechargeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.FlowPackageManager;

public class Replaceable implements Condition
{
    @Autowired
    private FlowPackageManager flowPackageManager;
    
    @Override
    public boolean pass(final RechargeRequest request, final ChoosedProduct choosedProduct, final ChooseRestrict... restricts) {
        final boolean repaceMode = ProductIDHelper.isNationProduct(request.getProductId()) && !ProductIDHelper.isNationProduct(choosedProduct.flowPackage.getProductId());
        final String profileId = choosedProduct.getChannelProductProfileId();
        final ChannelProductGroupProfile profile = this.flowPackageManager.loadGroupProfile(profileId);
        return !repaceMode || (choosedProduct.connection.getChannel().isCanReplaceNA() && profile.isCanReplaceNA());
    }
}
