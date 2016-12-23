// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import java.util.List;
import com.jiam365.flow.server.entity.BalanceNotify;
import org.mongodb.morphia.dao.BasicDAO;

public class BalanceNotifyDao extends BasicDAO<BalanceNotify, String>
{
    public List<BalanceNotify> findAllEnabled() {
        final Query<BalanceNotify> q = (Query<BalanceNotify>)this.createQuery().filter("status", (Object)true);
        return (List<BalanceNotify>)q.asList();
    }
    
    public BalanceNotifyDao(final Datastore ds) {
        super(ds);
    }
}
