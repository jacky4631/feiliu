// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import com.jiam365.flow.server.entity.Blacklist;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.BlacklistManager;
import org.springframework.beans.factory.InitializingBean;

public class FailTimesLimitator implements InitializingBean
{
    @Autowired
    private BlacklistManager blacklistManager;
    private Cache<String, Integer> counter;
    
    public void afterPropertiesSet() throws Exception {
        final Blacklist blacklist = this.blacklistManager.load();
        this.counter = (Cache<String, Integer>)CacheBuilder.newBuilder().expireAfterWrite((long)blacklist.getMonitorPeriod(), TimeUnit.HOURS).maximumSize(500000L).build();
    }
    
    public synchronized void addFailMobile(final String mobile) {
        final Integer total = (Integer)this.counter.getIfPresent((Object)mobile);
        if (total != null) {
            this.counter.put((Object)mobile, (Object)(total + 1));
        }
        else {
            this.counter.put((Object)mobile, (Object)1);
        }
    }
    
    public int failTimes(final String mobile) {
        final Integer total = (Integer)this.counter.getIfPresent((Object)mobile);
        return (total == null) ? 0 : total;
    }
}
