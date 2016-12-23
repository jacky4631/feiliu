// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.Params;
import org.mongodb.morphia.dao.BasicDAO;

public class ParamsDao extends BasicDAO<Params, String>
{
    public ParamsDao(final Datastore ds) {
        super(ds);
    }
}
