// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.jiam365.flow.server.entity.ChannelGroup;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.ChannelGroupDao;

public class ChannelGroupManager
{
    @Autowired
    private ChannelGroupDao channelGroupDao;
    private LoadingCache<String, ChannelGroup> groupCache;
    
    public ChannelGroupManager() {
        this.groupCache = (LoadingCache<String, ChannelGroup>)CacheBuilder.newBuilder().maximumSize(100L).expireAfterWrite(6L, TimeUnit.HOURS).build((CacheLoader)new CacheLoader<String, ChannelGroup>() {
            public ChannelGroup load(final String id) throws Exception {
                final ChannelGroup g = ChannelGroupManager.this.get(id);
                return (g == null) ? new ChannelGroup() : g;
            }
        });
    }
    
    public ChannelGroup get(final String id) {
        return (ChannelGroup)this.channelGroupDao.get((Object)id);
    }
    
    public ChannelGroup load(final String id) {
        try {
            return (ChannelGroup)this.groupCache.get((Object)id);
        }
        catch (ExecutionException e) {
            return null;
        }
    }
    
    public void remove(final String id) {
        this.channelGroupDao.deleteById((Object)id);
        this.groupCache.invalidate((Object)id);
    }
    
    public void save(final ChannelGroup channelGroup) {
        this.channelGroupDao.save((Object)channelGroup);
        this.groupCache.refresh((Object)channelGroup.getId());
    }
    
    public List<ChannelGroup> findAll() {
        return (List<ChannelGroup>)this.channelGroupDao.find().asList();
    }
    
    public Page<ChannelGroup> searchPage(final Page<ChannelGroup> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<ChannelGroup> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.channelGroupDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.channelGroupDao.find((Query)q).asList());
        return page;
    }
    
    private Query<ChannelGroup> buildQuery(final Map<String, String> filters) {
        return (Query<ChannelGroup>)this.channelGroupDao.createQuery();
    }
}
