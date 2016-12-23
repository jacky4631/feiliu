// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import java.util.List;
import com.jiam365.flow.server.entity.FlowCallbackInterceptor;
import org.mongodb.morphia.dao.BasicDAO;

public class FlowCallbackInterceptorDao extends BasicDAO<FlowCallbackInterceptor, String>
{
    public List<FlowCallbackInterceptor> findEnabled() {
        final Query<FlowCallbackInterceptor> query = (Query<FlowCallbackInterceptor>)this.createQuery();
        query.filter("status", (Object)true);
        query.order("idx");
        return (List<FlowCallbackInterceptor>)this.find((Query)query).asList();
    }
    
    public FlowCallbackInterceptorDao(final Datastore ds) {
        super(ds);
    }
}
