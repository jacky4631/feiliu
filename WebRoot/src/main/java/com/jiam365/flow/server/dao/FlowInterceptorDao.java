// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.FlowInterceptor;
import org.mongodb.morphia.dao.BasicDAO;

public class FlowInterceptorDao extends BasicDAO<FlowInterceptor, String>
{
    public FlowInterceptorDao(final Datastore ds) {
        super(ds);
    }
    
    public FlowInterceptor getFirstInterceptor() {
        final Query<FlowInterceptor> query = (Query<FlowInterceptor>)this.createQuery();
        return (FlowInterceptor)super.findOne((Query)query);
    }
}
