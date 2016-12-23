// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.query.Query;
import java.util.List;
import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.dic.MobileLocation;
import org.mongodb.morphia.dao.BasicDAO;

public class MobileLocationDao extends BasicDAO<MobileLocation, String>
{
    public MobileLocationDao(final Datastore ds) {
        super(ds);
    }
    
    public MobileLocation getByMobile(final String mobile) {
        try {
            return (MobileLocation)this.get(mobile.substring(0, 7));
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public List<MobileLocation> findAllStartwith(final String pre) {
        final Query<MobileLocation> q = (Query<MobileLocation>)this.createQuery();
        q.field("sectionNo").startsWith(pre);
        q.order("sectionNo");
        return (List<MobileLocation>)q.asList();
    }
}
