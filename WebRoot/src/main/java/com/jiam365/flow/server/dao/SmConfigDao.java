// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.SmConfig;
import org.mongodb.morphia.dao.BasicDAO;

public class SmConfigDao extends BasicDAO<SmConfig, String>
{
    public SmConfig getSetting() {
        final SmConfig setting = (SmConfig)super.findOne(this.createQuery());
        if (setting == null) {
            return new SmConfig();
        }
        return setting;
    }
    
    public SmConfigDao(final Datastore ds) {
        super(ds);
    }
}
