// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.pretreatment;

import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.jiam365.flow.server.params.FunctionLimitParam;
import com.jiam365.flow.server.engine.TradeException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.params.ParamsService;
import org.springframework.beans.factory.InitializingBean;

public class RateLimitationPretreatment implements Pretreatment, InitializingBean
{
    @Autowired
    private ParamsService paramsService;
    private Cache<String, Integer> counter;
    
    @Override
    public void check(final RechargeRequest request) throws TradeException {
        final String key = request.getUsername() + request.getMobile();
        final Integer total = (Integer)this.counter.getIfPresent(key);
        final FunctionLimitParam param = this.paramsService.loadFunctionLimitParam();
        final int maxTimesPerDay = param.getRechargeTimesLimit();
        if (total != null) {
            if (total >= maxTimesPerDay) {
                throw new TradeException(param.getLimitScopeSeconds() + "秒内最多只能给同一个号码充值" + maxTimesPerDay + "\u6b21");
            }
            this.counter.put(key, (total + 1));
        }
        else {
            this.counter.put(key, 1);
        }
    }
    
    public void afterPropertiesSet() throws Exception {
        final FunctionLimitParam param = this.paramsService.loadFunctionLimitParam();
        this.counter = CacheBuilder.newBuilder().expireAfterWrite((long)param.getLimitScopeSeconds(), TimeUnit.SECONDS).maximumSize(40000L).build();
    }
}
