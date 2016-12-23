// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import java.util.Iterator;
import org.mongodb.morphia.query.MorphiaIterator;
import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.flow.server.dto.DoubleWrapper;
import org.mongodb.morphia.aggregation.Group;
import org.mongodb.morphia.query.Query;
import java.util.Date;
import com.jiam365.flow.server.entity.SmLog;
import org.mongodb.morphia.dao.BasicDAO;

public class SmLogDao extends BasicDAO<SmLog, String>
{
    public double getNotPaidBillAmount(final String username, final Date toDate) {
        Iterator<DoubleWrapper> iterator = null;
        try {
            final Query<SmLog> query = (Query<SmLog>)this.createQuery();
            query.filter("paid =", (Object)false);
            query.filter("username =", (Object)username);
            query.filter("created <", (Object)toDate);
            final AggregationPipeline pipeline = this.getDs().createAggregation((Class)SmLog.class).match((Query)query).group((String)null, new Group[] { Group.grouping("amount", Group.sum("billAmount")) });
            iterator = (Iterator<DoubleWrapper>)pipeline.aggregate((Class)DoubleWrapper.class);
            if (iterator.hasNext()) {
                final DoubleWrapper p = iterator.next();
                return DoubleUtils.round(p.amount / 100.0, 2);
            }
        }
        finally {
            if (iterator != null) {
                ((MorphiaIterator)iterator).close();
            }
        }
        return 0.0;
    }
    
    public void markAsPaid(final String username, final Date toDate) {
        final Query<SmLog> query = (Query<SmLog>)this.createQuery();
        query.filter("paid =", (Object)false);
        query.filter("username =", (Object)username);
        query.filter("created <", (Object)toDate);
        final UpdateOperations<SmLog> opts = (UpdateOperations<SmLog>)this.createUpdateOperations();
        opts.set("paid", (Object)true);
        this.update((Query)query, (UpdateOperations)opts);
    }
    
    public SmLogDao(final Datastore ds) {
        super(ds);
    }
}
