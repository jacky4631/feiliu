// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import java.util.List;
import com.jiam365.flow.server.entity.BalanceNotify;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.BalanceNotifyDao;

public class BalanceNotifyManager
{
    @Autowired
    private BalanceNotifyDao balanceNotifyDao;
    
    public BalanceNotify get(final String id) {
        return (BalanceNotify)this.balanceNotifyDao.get((Object)id);
    }
    
    public void remove(final String id) {
        this.balanceNotifyDao.deleteById((Object)id);
    }
    
    public void save(final BalanceNotify notify) {
        this.balanceNotifyDao.save((Object)notify);
    }
    
    public List<BalanceNotify> findAllEnabled() {
        return this.balanceNotifyDao.findAllEnabled();
    }
    
    public Page<BalanceNotify> searchPage(final Page<BalanceNotify> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<BalanceNotify> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.balanceNotifyDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.balanceNotifyDao.find((Query)q).asList());
        return page;
    }
    
    private Query<BalanceNotify> buildQuery(final Map<String, String> filters) {
        return (Query<BalanceNotify>)this.balanceNotifyDao.createQuery();
    }
}
