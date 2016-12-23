// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.pretreatment;

import org.slf4j.LoggerFactory;
import com.jiam365.flow.server.rest.RestException;
import com.jiam365.flow.server.entity.Blacklist;
import com.jiam365.flow.server.engine.TradeException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.server.engine.FailTimesLimitator;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.BlacklistManager;
import org.slf4j.Logger;

public class BlackListPretreatment implements Pretreatment
{
    private static Logger logger;
    @Autowired
    private BlacklistManager blacklistManager;
    @Autowired
    private FailTimesLimitator failTimesLimitator;
    
    @Override
    public void check(final RechargeRequest request) throws RestException {
        if (this.blacklistManager.inList(request.getMobile())) {
            BlackListPretreatment.logger.warn("\u53d1\u73b0\u9493\u9c7c\u6267\u6cd5\u624b\u673a\u53f7\u7801{}\u5145\u503c, \u6765\u81ea{}, \u5145\u503c\u5305{}, \u5df2\u62d2\u7edd", new Object[] { request.getMobile(), request.getUsername(), request.getProductId() });
            throw new TradeException("\u624b\u673a\u53f7\u7801\u5728\u9ed1\u540d\u5355\u4e2d, \u65e0\u6cd5\u5145\u503c");
        }
        final int failTimes = this.failTimesLimitator.failTimes(request.getMobile());
        final Blacklist blacklist = this.blacklistManager.load();
        if (blacklist.isEnableDynamic() && failTimes >= blacklist.getAllowFailTimes()) {
            throw new TradeException("\u624b\u673a\u53f7\u7801\u5145\u503c\u5931\u8d25\u6b21\u6570\u8fc7\u591a, \u8bf7\u6682\u65f6\u4e0d\u8981\u518d\u8bd5");
        }
    }
    
    static {
        BlackListPretreatment.logger = LoggerFactory.getLogger((Class)BlackListPretreatment.class);
    }
}
