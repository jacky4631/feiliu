// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import com.jiam365.flow.server.entity.Bulletin;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.BulletinDao;

public class BulletinManager
{
    @Autowired
    private BulletinDao bulletinDao;
    
    public void remove(final String id) {
        this.bulletinDao.deleteById(id);
    }
    
    public void save(final Bulletin bulletin) {
        this.bulletinDao.save(bulletin);
    }
    
    public Page<Bulletin> searchPage(final Page<Bulletin> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<Bulletin> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.bulletinDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.bulletinDao.find((Query)q).asList());
        return page;
    }
    
    private Query<Bulletin> buildQuery(final Map<String, String> filters) {
        return (Query<Bulletin>)this.bulletinDao.createQuery();
    }
}
