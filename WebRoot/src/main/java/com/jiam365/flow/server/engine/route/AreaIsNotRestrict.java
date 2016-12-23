// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.route;

import java.util.List;
import com.jiam365.flow.server.entity.ChannelProductGroupProfile;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.server.engine.ChooseRestrict;
import com.jiam365.flow.server.dto.ChoosedProduct;
import com.jiam365.flow.sdk.RechargeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.FlowPackageManager;

public class AreaIsNotRestrict implements Condition
{
    @Autowired
    private FlowPackageManager flowPackageManager;
    
    @Override
    public boolean pass(final RechargeRequest request, final ChoosedProduct choosedProduct, final ChooseRestrict... restricts) {
        final String profileId = choosedProduct.getChannelProductProfileId();
        final ChannelProductGroupProfile profile = this.flowPackageManager.loadGroupProfile(profileId);
        final List<String> restrictStates = profile.getRestrictStates();
        final String stateCode = request.getMobileInfo().getStateCode();
        return StringUtils.isBlank((CharSequence)stateCode) || !restrictStates.contains(stateCode);
    }
}
