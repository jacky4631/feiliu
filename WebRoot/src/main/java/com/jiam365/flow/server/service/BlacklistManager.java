// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.apache.commons.lang3.StringUtils;
import java.util.concurrent.ConcurrentSkipListSet;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.jiam365.flow.server.entity.Blacklist;
import com.google.common.cache.LoadingCache;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.BlacklistDao;

public class BlacklistManager
{
    @Autowired
    private BlacklistDao blacklistDao;
    private Set<String> blacklist;
    private LoadingCache<String, Blacklist> cache;
    private static String KEY;
    
    public BlacklistManager() {
        this.cache = (LoadingCache<String, Blacklist>)CacheBuilder.newBuilder().maximumSize(10L).expireAfterWrite(5L, TimeUnit.DAYS).build((CacheLoader)new CacheLoader<String, Blacklist>() {
            public Blacklist load(final String key) {
                Blacklist blacklist = BlacklistManager.this.blacklistDao.getFirstOne();
                if (blacklist == null) {
                    blacklist = new Blacklist();
                }
                BlacklistManager.this.updateBlacklistSet(blacklist.getMobiles());
                return blacklist;
            }
        });
    }
    
    private synchronized void updateBlacklistSet(final String value) {
        this.blacklist = new ConcurrentSkipListSet<String>();
        if (StringUtils.isNotBlank((CharSequence)value)) {
            final String[] split;
            final String[] mobiles = split = value.split(",");
            for (final String mobile : split) {
                this.blacklist.add(mobile.trim());
            }
        }
    }
    
    public boolean inList(final String mobile) {
        synchronized (this) {
            if (this.blacklist == null) {
                this.load();
            }
        }
        return this.blacklist.contains(mobile);
    }
    
    public Blacklist load() {
        try {
            return (Blacklist)this.cache.get((Object)BlacklistManager.KEY);
        }
        catch (Exception e) {
            return new Blacklist();
        }
    }
    
    public void save(final Blacklist list) {
        this.blacklistDao.save((Object)list);
        this.cache.refresh((Object)BlacklistManager.KEY);
    }
    
    static {
        BlacklistManager.KEY = "FIRST";
    }
}
