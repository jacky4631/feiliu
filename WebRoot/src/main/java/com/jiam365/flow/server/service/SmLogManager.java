// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.slf4j.LoggerFactory;
import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import com.jiam365.flow.server.entity.User;
import com.jiam365.flow.server.engine.InsufficientBalanceException;
import java.util.Date;
import com.jiam365.flow.server.entity.SmLog;
import com.jiam365.flow.server.engine.BillingCenter;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.SmLogDao;
import org.slf4j.Logger;

public class SmLogManager
{
    private static Logger logger;
    @Autowired
    private SmLogDao smLogDao;
    @Autowired
    private BillingCenter billingCenter;
    @Autowired
    private TransferLogManager transferLogManager;
    @Autowired
    private UserManager userManager;
    
    public void save(final SmLog log) {
        this.smLogDao.save(log);
    }
    
    public double pay(final String operator, final String username, final Date toDate) {
        final double billAmount = this.smLogDao.getNotPaidBillAmount(username, toDate);
        if (billAmount <= 0.0) {
            return 0.0;
        }
        double balance;
        try {
            SmLogManager.logger.info("\u51c6\u5907\u6263\u9664{}\u7684\u77ed\u4fe1\u8d39\u7528{}\u5143, \u64cd\u4f5c\u4eba{}", new Object[] { username, billAmount, operator });
            balance = this.billingCenter.pay(username, billAmount);
        }
        catch (InsufficientBalanceException e) {
            SmLogManager.logger.warn("\u6263\u9664{}\u7684\u77ed\u4fe1\u8d39{}\u65f6\u4f59\u989d\u4e0d\u8db3, \u653e\u5f03\u6263\u8d39", (Object)username, (Object)billAmount);
            return 0.0;
        }
        final User user = this.userManager.getUserByUsername(username);
        final String displayName = (user == null) ? "" : user.getDisplayName();
        this.transferLogManager.saveLog(operator, 107, username, displayName, -billAmount, balance, "\u77ed\u4fe1\u8d39\u7528\u6263\u9664", new int[0]);
        this.smLogDao.markAsPaid(username, toDate);
        return billAmount;
    }
    
    public Page<SmLog> searchPage(final Page<SmLog> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<SmLog> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.smLogDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.smLogDao.find((Query)q).asList());
        return page;
    }
    
    private Query<SmLog> buildQuery(final Map<String, String> filters) {
        final Query<SmLog> q = (Query<SmLog>)this.smLogDao.createQuery();
        if (filters.containsKey("EQ_username")) {
            q.field("username").equal((Object)filters.get("EQ_username"));
        }
        if (filters.containsKey("EQ_mobile")) {
            q.field("mobile").equal((Object)filters.get("EQ_mobile"));
        }
        if (filters.containsKey("EQ_tradeId")) {
            q.field("tradeId").equal((Object)filters.get("EQ_tradeId"));
        }
        return q;
    }
    
    static {
        SmLogManager.logger = LoggerFactory.getLogger((Class)SmLogManager.class);
    }
}
