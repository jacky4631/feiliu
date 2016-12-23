// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.SmTemplate;
import org.mongodb.morphia.dao.BasicDAO;

public class SmTemplateDao extends BasicDAO<SmTemplate, Long>
{
    public SmTemplateDao(final Datastore ds) {
        super(ds);
    }
}
