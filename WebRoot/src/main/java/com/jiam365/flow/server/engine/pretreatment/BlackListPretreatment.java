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
            BlackListPretreatment.logger.warn("发现钓鱼执法手机号码{}充值, 来自{}, 充值包{}, 已拒绝", new Object[] { request.getMobile(), request.getUsername(), request.getProductId() });
            throw new TradeException("手机号码在黑名单中, 无法充值");
        }
        final int failTimes = this.failTimesLimitator.failTimes(request.getMobile());
        final Blacklist blacklist = this.blacklistManager.load();
        if (blacklist.isEnableDynamic() && failTimes >= blacklist.getAllowFailTimes()) {
            throw new TradeException("手机号码充值失败次数过多, 请暂时不要再试");
        }
    }
    
    static {
        BlackListPretreatment.logger = LoggerFactory.getLogger((Class)BlackListPretreatment.class);
    }
}
