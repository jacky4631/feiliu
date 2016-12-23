// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import com.jiam365.flow.server.entity.SmMemo;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.SmMemoDao;

public class SmMemoManager
{
    @Autowired
    private SmMemoDao smMemoDao;
    
    public SmMemo get(final String id) {
        return (SmMemo)this.smMemoDao.get((Object)id);
    }
    
    public void remove(final String id) {
        this.smMemoDao.deleteById((Object)id);
    }
    
    public void save(final SmMemo smMemo) {
        this.smMemoDao.save((Object)smMemo);
    }
    
    public Page<SmMemo> searchPage(final Page<SmMemo> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<SmMemo> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.smMemoDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.smMemoDao.find((Query)q).asList());
        return page;
    }
    
    private Query<SmMemo> buildQuery(final Map<String, String> filters) {
        return (Query<SmMemo>)this.smMemoDao.createQuery();
    }
}
