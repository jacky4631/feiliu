// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.route;

import java.util.List;
import com.jiam365.flow.server.entity.User;
import com.jiam365.flow.server.entity.ChannelProductGroupProfile;
import com.jiam365.flow.server.engine.ChooseRestrict;
import com.jiam365.flow.server.dto.ChoosedProduct;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.FlowPackageManager;

public class ProtectedProductGrant implements Condition
{
    @Autowired
    private FlowPackageManager flowPackageManager;
    @Autowired
    private UserManager userManager;
    
    @Override
    public boolean pass(final RechargeRequest request, final ChoosedProduct choosedProduct, final ChooseRestrict... restricts) {
        final String fullGroupCode = choosedProduct.getChannelProductProfileId();
        final ChannelProductGroupProfile profile = this.flowPackageManager.loadGroupProfile(fullGroupCode);
        final boolean needProtected = profile.isNeedProtected();
        final User user = this.userManager.loadUserByUsername(request.getUsername());
        final List<String> grantedProducts = user.getGrantedSpecialChannelProducts();
        return !needProtected || grantedProducts.contains(fullGroupCode);
    }
}
