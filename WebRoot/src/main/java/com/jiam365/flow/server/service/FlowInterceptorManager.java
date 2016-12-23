// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import java.util.List;
import com.jiam365.flow.server.entity.FlowCallbackInterceptor;
import com.jiam365.flow.server.entity.FlowInterceptor;
import com.jiam365.flow.server.dao.FlowCallbackInterceptorDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.FlowInterceptorDao;

public class FlowInterceptorManager
{
    @Autowired
    private FlowInterceptorDao flowInterceptorDao;
    @Autowired
    private FlowCallbackInterceptorDao flowCallbackInterceptorDao;
    
    public void save(final FlowInterceptor interceptor) {
        this.flowInterceptorDao.save((Object)interceptor);
    }
    
    public FlowInterceptor get() {
        final FlowInterceptor interceptor = this.flowInterceptorDao.getFirstInterceptor();
        return (interceptor == null) ? new FlowInterceptor() : interceptor;
    }
    
    public FlowCallbackInterceptor getFlowCallbackInterceptor(final String id) {
        return (FlowCallbackInterceptor)this.flowCallbackInterceptorDao.get((Object)id);
    }
    
    public void saveCallbackInterceptor(final FlowCallbackInterceptor interceptor) {
        this.flowCallbackInterceptorDao.save((Object)interceptor);
    }
    
    public void deleteCallbackInterceptor(final FlowCallbackInterceptor interceptor) {
        this.flowCallbackInterceptorDao.delete((Object)interceptor);
    }
    
    public FlowCallbackInterceptor deleteCallbackInterceptor(final String id) {
        final FlowCallbackInterceptor flowCallbackInterceptor = (FlowCallbackInterceptor)this.flowCallbackInterceptorDao.get((Object)id);
        if (flowCallbackInterceptor != null) {
            this.flowCallbackInterceptorDao.delete((Object)flowCallbackInterceptor);
        }
        return flowCallbackInterceptor;
    }
    
    public List<FlowCallbackInterceptor> findEnabledCallbackInterceptors() {
        return this.flowCallbackInterceptorDao.findEnabled();
    }
    
    public Page<FlowCallbackInterceptor> searchCallbackInterceptorPage(final Page<FlowCallbackInterceptor> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<FlowCallbackInterceptor> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.flowCallbackInterceptorDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.flowCallbackInterceptorDao.find((Query)q).asList());
        return page;
    }
    
    private Query<FlowCallbackInterceptor> buildQuery(final Map<String, String> filters) {
        return (Query<FlowCallbackInterceptor>)this.flowCallbackInterceptorDao.createQuery();
    }
}
