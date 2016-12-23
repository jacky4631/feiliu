// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import com.jiam365.modules.utils.SimpleDateUtils;
import org.mongodb.morphia.query.Query;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import com.jiam365.flow.server.entity.TransferLog;
import java.util.Map;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.TransferLogDao;

public class TransferLogManager
{
    @Autowired
    private TransferLogDao transferLogDao;
    
    public Map<Integer, Double> getTransferTotal(final Date d1, final Date d2, final String... usernames) {
        return this.transferLogDao.getTransferTotal(d1, d2, usernames);
    }
    
    public TransferLog get(final String tid) {
        return (TransferLog)this.transferLogDao.get((Object)tid);
    }
    
    public void update(final TransferLog log) {
        this.transferLogDao.save((Object)log);
    }
    
    public void delete(final String tid) {
        this.transferLogDao.deleteById((Object)tid);
    }
    
    public void saveLog(final String operator, final int accountingSubject, final String username, final String displayName, final double amount, final double balance, final String remark, final int... type) {
        final TransferLog log = new TransferLog();
        log.setOperator(operator);
        log.setAccountingSubject(accountingSubject);
        log.setUsername(username);
        log.setDisplayName(displayName);
        log.setAmount(amount);
        log.setBalance(balance);
        log.setRemark(remark);
        if (type.length > 0) {
            log.setType(type[0]);
        }
        this.transferLogDao.save((Object)log);
    }
    
    public Page<TransferLog> searchPage(final Page<TransferLog> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<TransferLog> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.transferLogDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.transferLogDao.find((Query)q).asList());
        return page;
    }
    
    private Query<TransferLog> buildQuery(final Map<String, String> filters) {
        final Query<TransferLog> q = (Query<TransferLog>)this.transferLogDao.createQuery();
        if (filters.containsKey("EQ_username")) {
            q.field("username").equal((Object)filters.get("EQ_username"));
        }
        if (filters.containsKey("EQ_accountingSubject")) {
            q.field("accountingSubject").equal((Object)Integer.parseInt(filters.get("EQ_accountingSubject")));
        }
        if (filters.containsKey("EQ_operator")) {
            q.field("operator").equal((Object)filters.get("EQ_operator"));
        }
        if (filters.containsKey("GED_operateTime")) {
            final String startDate = filters.get("GED_operateTime");
            final Date d = SimpleDateUtils.safeParseDate(startDate, "yyyy-MM-dd");
            if (d != null) {
                q.filter("operateTime >=", (Object)SimpleDateUtils.getDateStart(d));
            }
        }
        if (filters.containsKey("LED_operateTime")) {
            final String finishDate = filters.get("LED_operateTime");
            final Date d = SimpleDateUtils.safeParseDate(finishDate, "yyyy-MM-dd");
            if (d != null) {
                q.filter("operateTime <=", (Object)SimpleDateUtils.getDateEnd(d));
            }
        }
        return q;
    }
}
