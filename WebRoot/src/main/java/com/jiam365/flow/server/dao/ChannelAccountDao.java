// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Criteria;
import java.util.List;
import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.ChannelAccount;
import org.mongodb.morphia.dao.BasicDAO;

public class ChannelAccountDao extends BasicDAO<ChannelAccount, Long>
{
    public ChannelAccountDao(final Datastore ds) {
        super(ds);
    }
    
    public List<ChannelAccount> findNotEqualsZeroAccounts() {
        final Query<ChannelAccount> query = (Query<ChannelAccount>)this.createQuery();
        query.or(new Criteria[] { (Criteria)query.criteria("balance").greaterThan((Object)0.01), (Criteria)query.criteria("balance").lessThan((Object)0) });
        query.order("balance");
        return (List<ChannelAccount>)query.asList();
    }
    
    public void incBalance(final long channelId, final double money) {
        final Query<ChannelAccount> query = (Query<ChannelAccount>)this.createQuery().filter("channleId", (Object)channelId);
        final UpdateOperations<ChannelAccount> opts = (UpdateOperations<ChannelAccount>)this.createUpdateOperations();
        opts.inc("balance", (Number)money);
        this.update((Query)query, (UpdateOperations)opts);
    }
}
