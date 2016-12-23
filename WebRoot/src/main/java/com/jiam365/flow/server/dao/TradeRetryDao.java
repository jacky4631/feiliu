// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import java.util.List;
import com.jiam365.flow.server.entity.TradeRetry;
import org.mongodb.morphia.dao.BasicDAO;

public class TradeRetryDao extends BasicDAO<TradeRetry, String>
{
    public List<TradeRetry> findByTradeId(final String tradeId) {
        final Query<TradeRetry> query = (Query<TradeRetry>)this.createQuery();
        query.filter("tradeId", (Object)tradeId);
        query.order("finishDate");
        return (List<TradeRetry>)query.asList();
    }
    
    public void removeByTradeId(final String tradeId) {
        final Query<TradeRetry> query = (Query<TradeRetry>)this.createQuery();
        query.filter("tradeId", (Object)tradeId);
        this.deleteByQuery((Query)query);
    }
    
    public TradeRetryDao(final Datastore ds) {
        super(ds);
    }
}
