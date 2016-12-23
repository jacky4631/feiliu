// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.PluginDefinition;
import org.mongodb.morphia.dao.BasicDAO;

public class PluginDefinitionDao extends BasicDAO<PluginDefinition, String>
{
    public PluginDefinitionDao(final Datastore ds) {
        super(ds);
    }
}
