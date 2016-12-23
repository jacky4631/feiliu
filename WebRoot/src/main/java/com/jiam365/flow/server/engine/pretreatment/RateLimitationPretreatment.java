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
        final Integer total = (Integer)this.counter.getIfPresent((Object)key);
        final FunctionLimitParam param = this.paramsService.loadFunctionLimitParam();
        final int maxTimesPerDay = param.getRechargeTimesLimit();
        if (total != null) {
            if (total >= maxTimesPerDay) {
                throw new TradeException(param.getLimitScopeSeconds() + "\u79d2\u5185\u6700\u591a\u53ea\u80fd\u7ed9\u540c\u4e00\u4e2a\u53f7\u7801\u5145\u503c" + maxTimesPerDay + "\u6b21");
            }
            this.counter.put((Object)key, (Object)(total + 1));
        }
        else {
            this.counter.put((Object)key, (Object)1);
        }
    }
    
    public void afterPropertiesSet() throws Exception {
        final FunctionLimitParam param = this.paramsService.loadFunctionLimitParam();
        this.counter = (Cache<String, Integer>)CacheBuilder.newBuilder().expireAfterWrite((long)param.getLimitScopeSeconds(), TimeUnit.SECONDS).maximumSize(40000L).build();
    }
}
