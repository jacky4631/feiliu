// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import com.jiam365.modules.utils.Identities;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.server.entity.Role;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.RoleDao;

public class RoleManager
{
    @Autowired
    private RoleDao roleDao;
    
    public List<Role> findAll() {
        return (List<Role>)this.roleDao.find().asList();
    }
    
    public void remove(final String id) {
        this.roleDao.deleteById(id);
    }
    
    public Role get(final String id) {
        return (Role)this.roleDao.get(id);
    }
    
    public void save(final Role role) {
        if (StringUtils.isBlank((CharSequence)role.getId())) {
            role.setId(Identities.uuid2());
        }
        this.roleDao.save(role);
    }
    
    public Page<Role> searchPage(final Page<Role> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<Role> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.roleDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.roleDao.find((Query)q).asList());
        return page;
    }
    
    private Query<Role> buildQuery(final Map<String, String> filters) {
        return (Query<Role>)this.roleDao.createQuery();
    }
}
