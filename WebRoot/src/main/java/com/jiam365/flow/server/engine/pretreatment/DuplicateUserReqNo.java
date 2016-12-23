// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.pretreatment;

import org.slf4j.LoggerFactory;
import com.jiam365.flow.server.rest.RestException;
import com.jiam365.flow.server.rest.GeneralRestException;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.sdk.RechargeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.TradeLogManager;
import org.slf4j.Logger;

public class DuplicateUserReqNo implements Pretreatment
{
    private static Logger logger;
    @Autowired
    private TradeLogManager tradeLogManager;
    
    @Override
    public void check(final RechargeRequest request) throws RestException {
        final String username = request.getUsername();
        final String userReqNo = request.getUserReqNo();
        if (StringUtils.isNotBlank((CharSequence)userReqNo) && this.tradeLogManager.getByUserRequestNo(username, userReqNo) != null) {
            DuplicateUserReqNo.logger.warn("用户订单号重复, 来自{}, 用户订单号{}, 已拒绝", (Object)username, (Object)userReqNo);
            throw new GeneralRestException("50009");
        }
    }
    
    static {
        DuplicateUserReqNo.logger = LoggerFactory.getLogger((Class)DuplicateUserReqNo.class);
    }
}
