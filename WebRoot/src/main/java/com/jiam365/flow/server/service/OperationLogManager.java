// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import java.util.Date;
import com.jiam365.modules.utils.SimpleDateUtils;
import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import com.jiam365.flow.server.entity.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.OperationLogDao;

public class OperationLogManager
{
    @Autowired
    private OperationLogDao operationLogDao;
    
    public void save(final OperationLog log) {
        this.operationLogDao.save(log);
    }
    
    public Page<OperationLog> searchPage(final Page<OperationLog> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<OperationLog> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.operationLogDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.operationLogDao.find((Query)q).asList());
        return page;
    }
    
    private Query<OperationLog> buildQuery(final Map<String, String> filters) {
        final Query<OperationLog> q = (Query<OperationLog>)this.operationLogDao.createQuery();
        if (filters.containsKey("EQ_username")) {
            q.field("username").equal((Object)filters.get("EQ_username"));
        }
        if (filters.containsKey("GED_created")) {
            final String startDate = filters.get("GED_created");
            final Date d = SimpleDateUtils.safeParseDate(startDate, "yyyy-MM-dd");
            if (d != null) {
                q.filter("created >=", (Object)SimpleDateUtils.getDateStart(d));
            }
        }
        if (filters.containsKey("LED_created")) {
            final String finishDate = filters.get("LED_created");
            final Date d = SimpleDateUtils.safeParseDate(finishDate, "yyyy-MM-dd");
            if (d != null) {
                q.filter("created <=", (Object)SimpleDateUtils.getDateEnd(d));
            }
        }
        if (filters.containsKey("LIKE_description")) {
            q.field("description").contains((String)filters.get("LIKE_description"));
        }
        return q;
    }
}
