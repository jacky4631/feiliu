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
            DuplicateUserReqNo.logger.warn("\u7528\u6237\u8ba2\u5355\u53f7\u91cd\u590d, \u6765\u81ea{}, \u7528\u6237\u8ba2\u5355\u53f7{}, \u5df2\u62d2\u7edd", (Object)username, (Object)userReqNo);
            throw new GeneralRestException("50009");
        }
    }
    
    static {
        DuplicateUserReqNo.logger = LoggerFactory.getLogger((Class)DuplicateUserReqNo.class);
    }
}
