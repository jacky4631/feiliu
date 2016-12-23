// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.route;

import com.jiam365.flow.server.entity.User;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.server.engine.ChooseRestrict;
import com.jiam365.flow.server.dto.ChoosedProduct;
import com.jiam365.flow.sdk.RechargeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.UserManager;

public class NotSameOrgRestrict implements Condition
{
    @Autowired
    private UserManager userManager;
    
    @Override
    public boolean pass(final RechargeRequest request, final ChoosedProduct choosedProduct, final ChooseRestrict... restricts) {
        final String username = request.getUsername();
        final User user = this.userManager.loadUserByUsername(username);
        return user != null && (StringUtils.isBlank((CharSequence)user.getOrgcode()) || !StringUtils.equals((CharSequence)user.getOrgcode(), (CharSequence)choosedProduct.connection.getChannel().getOrgcode()));
    }
}
