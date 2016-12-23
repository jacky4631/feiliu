// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.server.entity.RefundKeyword;
import java.util.concurrent.ExecutionException;
import java.util.HashSet;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import java.util.Set;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.RefundKeywordDao;

public class RefundKeywordManager
{
    private static String ID;
    @Autowired
    private RefundKeywordDao refundKeywordDao;
    private LoadingCache<String, Set<String>> keywordsCache;
    
    public RefundKeywordManager() {
        this.keywordsCache = (LoadingCache<String, Set<String>>)CacheBuilder.newBuilder().maximumSize(1L).build((CacheLoader)new CacheLoader<String, Set<String>>() {
            public Set<String> load(final String id) throws Exception {
                return RefundKeywordManager.this.getKeywords(id);
            }
        });
    }
    
    public Set<String> loadKeywords() {
        try {
            return (Set<String>)this.keywordsCache.get(RefundKeywordManager.ID);
        }
        catch (ExecutionException e) {
            return new HashSet<String>();
        }
    }
    
    private Set<String> getKeywords(final String id) {
        final RefundKeyword store = (RefundKeyword)this.refundKeywordDao.get(id);
        return (store == null) ? new HashSet<String>() : store.getKeywords();
    }
    
    public boolean match(final String message) {
        if (StringUtils.isBlank((CharSequence)message)) {
            return false;
        }
        final Set<String> keywords = this.loadKeywords();
        for (final String key : keywords) {
            if (message.contains(key)) {
                return true;
            }
        }
        return false;
    }
    
    public int addKeyword(final String keyword) {
        final RefundKeyword store = this.getOrCreate();
        store.addKeyword(keyword);
        this.refundKeywordDao.save(store);
        this.keywordsCache.refresh(RefundKeywordManager.ID);
        return this.loadKeywords().size();
    }
    
    public int reomveKeyword(final String keyword) {
        final RefundKeyword store = this.getOrCreate();
        store.removeKeyword(keyword);
        this.refundKeywordDao.save(store);
        this.keywordsCache.refresh(RefundKeywordManager.ID);
        return this.loadKeywords().size();
    }
    
    public RefundKeyword getOrCreate() {
        RefundKeyword store = (RefundKeyword)this.refundKeywordDao.get(RefundKeywordManager.ID);
        if (store == null) {
            store = new RefundKeyword();
            store.setId("ONLY");
            this.refundKeywordDao.save(store);
        }
        return store;
    }
    
    static {
        RefundKeywordManager.ID = "ONLY";
    }
}
