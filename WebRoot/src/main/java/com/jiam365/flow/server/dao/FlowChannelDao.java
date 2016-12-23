// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateOperations;
import com.jiam365.modules.telco.Telco;
import com.mongodb.MongoClientException;
import org.mongodb.morphia.query.Query;
import java.util.List;
import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.channel.FlowChannel;
import org.mongodb.morphia.dao.BasicDAO;

public class FlowChannelDao extends BasicDAO<FlowChannel, Long>
{
    public FlowChannelDao(final Datastore ds) {
        super(ds);
    }
    
    public List<FlowChannel> findChannelsByHandlerClass(final String handleClassName) {
        return (List<FlowChannel>)((Query)this.createQuery().field("channelConnectionParam.handlerClass").equal((Object)handleClassName)).asList();
    }
    
    public List<FlowChannel> findEnabledChannels() {
        final Query<FlowChannel> query = (Query<FlowChannel>)this.createQuery().filter("status", (Object)0);
        query.order("name");
        try {
            return (List<FlowChannel>)this.find((Query)query).asList();
        }
        catch (MongoClientException e) {
            throw new DbException(e.getMessage(), (Throwable)e);
        }
    }
    
    public List<FlowChannel> findChannels() {
        final Query<FlowChannel> query = (Query<FlowChannel>)this.createQuery();
        try {
            return (List<FlowChannel>)this.find((Query)query).asList();
        }
        catch (MongoClientException e) {
            throw new DbException(e.getMessage(), (Throwable)e);
        }
    }
    
    public List<FlowChannel> find(final Telco provider) {
        final Query<FlowChannel> query = (Query<FlowChannel>)this.createQuery().filter("provider", (Object)provider);
        return (List<FlowChannel>)this.find((Query)query).asList();
    }
    
    public void disableChannel(final Long channelId) {
        this.updateChannelStatus(channelId, -1);
    }
    
    public void enableChannel(final Long channelId) {
        this.updateChannelStatus(channelId, 0);
    }
    
    public synchronized void insertNew(final FlowChannel channel) {
        if (channel.getId() != null) {
            throw new DbException("FlowChannel cannot have an Id before insert, Id would be set inside the data layer");
        }
        channel.setId(this.generateId());
        this.save(channel);
    }
    
    public void updateChannelStatus(final Long channelId, final int status) {
        final Query<FlowChannel> query = (Query<FlowChannel>)this.createQuery().filter("id", (Object)channelId);
        final UpdateOperations<FlowChannel> opts = (UpdateOperations<FlowChannel>)this.createUpdateOperations();
        opts.set("status", (Object)status);
        this.update((Query)query, (UpdateOperations)opts);
    }
    
    private Long generateId() {
        final Query<FlowChannel> query = (Query<FlowChannel>)this.createQuery();
        query.order("-id");
        final Key<FlowChannel> key = (Key<FlowChannel>)super.findOneId((Query)query);
        if (key == null) {
            return 100L;
        }
        Long curMaxId = (Long)key.getId();
        if (curMaxId >= 9999L) {
            throw new DbException("Too many flow channels defined, max channel id is 9999");
        }
        return ++curMaxId;
    }
}
