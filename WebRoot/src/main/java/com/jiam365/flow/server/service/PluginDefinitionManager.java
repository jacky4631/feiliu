// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import com.jiam365.flow.server.entity.PluginDefinition;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.PluginDefinitionDao;

public class PluginDefinitionManager
{
    @Autowired
    private PluginDefinitionDao pluginDefinitionDao;
    
    public List<PluginDefinition> findAll() {
        return (List<PluginDefinition>)this.pluginDefinitionDao.find().asList();
    }
    
    public void remove(final String id) {
        this.pluginDefinitionDao.deleteById(id);
    }
    
    public PluginDefinition get(final String id) {
        return (PluginDefinition)this.pluginDefinitionDao.get(id);
    }
    
    public void save(final PluginDefinition definition) {
        this.pluginDefinitionDao.save(definition);
    }
    
    public Page<PluginDefinition> searchPage(final Page<PluginDefinition> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<PluginDefinition> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.pluginDefinitionDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.pluginDefinitionDao.find((Query)q).asList());
        return page;
    }
    
    private Query<PluginDefinition> buildQuery(final Map<String, String> filters) {
        final Query<PluginDefinition> q = (Query<PluginDefinition>)this.pluginDefinitionDao.createQuery();
        if (filters.containsKey("LIKE_title")) {
            q.field("title").contains((String)filters.get("LIKE_title"));
        }
        return q;
    }
}
