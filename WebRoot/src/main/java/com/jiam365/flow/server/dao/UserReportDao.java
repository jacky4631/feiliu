// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.UpdateOperations;
import java.util.Iterator;
import org.mongodb.morphia.query.Query;
import java.util.List;
import com.jiam365.flow.server.usercallback.UserReport;
import org.mongodb.morphia.dao.BasicDAO;

public class UserReportDao extends BasicDAO<UserReport, String>
{
    public List<UserReport> popTasks(final int count, final int delayMinutes, final int maxRetry) {
        final Query<UserReport> query = (Query<UserReport>)this.createQuery();
        query.filter("retryTimes <", (Object)maxRetry);
        final long timestamp = System.currentTimeMillis() - 60000 * delayMinutes;
        query.filter("lastTimestamp <=", (Object)timestamp);
        final List<UserReport> tasks = (List<UserReport>)query.order("lastTimestamp").offset(0).limit(count).asList();
        for (final UserReport task : tasks) {
            this.delete(task);
        }
        return tasks;
    }
    
    public void reset(final String tradeId) {
        final Query<UserReport> query = (Query<UserReport>)this.createQuery().filter("tradeId", (Object)tradeId);
        final UpdateOperations<UserReport> opts = (UpdateOperations<UserReport>)this.createUpdateOperations();
        opts.set("retryTimes", (Object)0);
        this.update((Query)query, (UpdateOperations)opts);
    }
    
    public UserReportDao(final Datastore ds) {
        super(ds);
    }
}
