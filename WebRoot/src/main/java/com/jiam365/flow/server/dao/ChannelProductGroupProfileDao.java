// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import java.util.List;
import org.mongodb.morphia.query.Query;
import com.jiam365.flow.server.entity.ChannelProductGroupProfile;
import org.mongodb.morphia.dao.BasicDAO;

public class ChannelProductGroupProfileDao extends BasicDAO<ChannelProductGroupProfile, String>
{
    public void deleteByChannelId(final long channelId) {
        final Query<ChannelProductGroupProfile> query = (Query<ChannelProductGroupProfile>)this.createQuery();
        query.filter("channelId", (Object)channelId);
        super.deleteByQuery((Query)query);
    }
    
    public List<ChannelProductGroupProfile> findProtectedProfile() {
        final Query<ChannelProductGroupProfile> query = (Query<ChannelProductGroupProfile>)this.createQuery();
        query.filter("needProtected", (Object)true);
        return (List<ChannelProductGroupProfile>)query.asList();
    }
    
    public ChannelProductGroupProfileDao(final Datastore ds) {
        super(ds);
    }
}
