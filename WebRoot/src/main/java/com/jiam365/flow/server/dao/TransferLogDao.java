// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import java.util.Iterator;
import org.mongodb.morphia.query.MorphiaIterator;
import com.jiam365.flow.server.utils.DoubleUtils;
import org.mongodb.morphia.aggregation.Group;
import org.mongodb.morphia.query.Query;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import com.jiam365.flow.server.entity.TransferLog;
import org.mongodb.morphia.dao.BasicDAO;

public class TransferLogDao extends BasicDAO<TransferLog, String>
{
    public Map<Integer, Double> getTransferTotal(final Date d1, final Date d2, final String... usernames) {
        final Map<Integer, Double> map = new HashMap<Integer, Double>();
        Iterator<TransAmount> iterator = null;
        try {
            final Query<TransferLog> query = (Query<TransferLog>)this.createQuery();
            query.filter("operateTime >=", (Object)d1);
            query.filter("operateTime <=", (Object)d2);
            query.filter("accountingSubject <", (Object)100);
            query.filter("accountingSubject >", (Object)0);
            if (usernames.length > 0) {
                query.filter("username =", (Object)usernames[0]);
            }
            final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TransferLog.class).match((Query)query).group("type", new Group[] { Group.grouping("totalAmount", Group.sum("amount")) });
            iterator = (Iterator<TransAmount>)pipeline.aggregate((Class)TransAmount.class);
            while (iterator.hasNext()) {
                final TransAmount p = iterator.next();
                map.put(p.type, DoubleUtils.round(p.totalAmount, 2));
            }
        }
        finally {
            if (iterator != null) {
                ((MorphiaIterator)iterator).close();
            }
        }
        return map;
    }
    
    public TransferLogDao(final Datastore ds) {
        super(ds);
    }
    
    public static class TransAmount
    {
        @Id
        private Integer type;
        private Double totalAmount;
    }
}
