// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.ChannelGroup;
import org.mongodb.morphia.dao.BasicDAO;

public class ChannelGroupDao extends BasicDAO<ChannelGroup, String>
{
    public ChannelGroupDao(final Datastore ds) {
        super(ds);
    }
}
