// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import com.jiam365.flow.server.entity.Blacklist;
import org.mongodb.morphia.dao.BasicDAO;

public class BlacklistDao extends BasicDAO<Blacklist, String>
{
    public Blacklist getFirstOne() {
        final Query<Blacklist> q = (Query<Blacklist>)this.createQuery();
        return (Blacklist)this.findOne((Query)q);
    }
    
    public BlacklistDao(final Datastore ds) {
        super(ds);
    }
}
