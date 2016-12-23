// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import com.mongodb.WriteResult;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.TradeReport;
import org.mongodb.morphia.dao.BasicDAO;

public class TradeReportDao extends BasicDAO<TradeReport, String>
{
    public TradeReportDao(final Datastore ds) {
        super(ds);
    }
    
    public int cleanOldData() {
        final Query<TradeReport> query = (Query<TradeReport>)this.createQuery();
        final long timestamp = System.currentTimeMillis() - 259200000L;
        query.filter("created <=", (Object)timestamp);
        final WriteResult result = super.deleteByQuery((Query)query);
        return result.getN();
    }
}
