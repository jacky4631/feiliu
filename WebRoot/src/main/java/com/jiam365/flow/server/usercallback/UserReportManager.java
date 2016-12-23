// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.usercallback;

import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import com.jiam365.flow.server.params.UserReportParam;
import java.util.List;
import com.jiam365.flow.server.entity.TradeLog;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.server.engine.Trade;
import com.jiam365.flow.server.params.ParamsService;
import com.jiam365.flow.server.dao.UserReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.UserManager;

public class UserReportManager
{
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserReportDao userReportDao;
    @Autowired
    private ParamsService paramsService;
    
    public void createReport(final boolean isSuccess, final Trade trade) {
        final String username = trade.getRequest().getUsername();
        final String callbackUrl = this.userManager.getCallbackUrl(username);
        if (StringUtils.isNotBlank((CharSequence)callbackUrl)) {
            final UserReport userReport = new UserReport();
            userReport.setTradeId(trade.getTradeId());
            userReport.setUserReqNo(trade.getRequest().getUserReqNo());
            userReport.setIsSuccess(isSuccess);
            userReport.setMessage(trade.getLastMessage());
            userReport.setUsername(username);
            userReport.setCallbackUrl(callbackUrl);
            userReport.setSize(trade.getRequest().getSize());
            userReport.setMobile(trade.getRequest().getMobile());
            userReport.setProductId(trade.getRequest().getProductId());
            final int delayMinutes = this.paramsService.loadUserReportParam().getDelayMinutes();
            userReport.setLastTimestamp(System.currentTimeMillis() - 60000 * delayMinutes);
            userReport.setRetryTimes(0);
            this.userReportDao.save((Object)userReport);
        }
    }
    
    public void createReport(final TradeLog log) {
        final String username = log.getUsername();
        final String callbackUrl = this.userManager.getCallbackUrl(username);
        if (StringUtils.isNotBlank((CharSequence)callbackUrl)) {
            final UserReport userReport = new UserReport();
            userReport.setTradeId(log.getId());
            userReport.setUserReqNo(log.getUserRequestNo());
            userReport.setIsSuccess(log.getResult() == 0);
            userReport.setMessage(log.getMessage());
            userReport.setUsername(username);
            userReport.setCallbackUrl(callbackUrl);
            userReport.setSize(log.getSize());
            userReport.setMobile(log.getMobile());
            userReport.setProductId(log.getProductId());
            final int delayMinutes = this.paramsService.loadUserReportParam().getDelayMinutes();
            userReport.setLastTimestamp(System.currentTimeMillis() - 60000 * delayMinutes);
            userReport.setRetryTimes(0);
            this.userReportDao.save((Object)userReport);
        }
    }
    
    public void reset(final String tradeId) {
        this.userReportDao.reset(tradeId);
    }
    
    public void saveAgain(final UserReport report) {
        report.setRetryTimes(report.getRetryTimes() + 1);
        report.setLastTimestamp(System.currentTimeMillis());
        this.userReportDao.save((Object)report);
    }
    
    public void createReport(final UserReport report) {
        this.userReportDao.save((Object)report);
    }
    
    public void remove(final String tradeId) {
        this.userReportDao.deleteById((Object)tradeId);
    }
    
    public synchronized List<UserReport> popTasks(final int count) {
        final UserReportParam param = this.paramsService.loadUserReportParam();
        final int delayMinutes = param.getDelayMinutes();
        final int maxRetry = param.getRetryTimes();
        return this.userReportDao.popTasks(count, delayMinutes, maxRetry);
    }
    
    public Page<UserReport> searchPage(final Page<UserReport> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<UserReport> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.userReportDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.userReportDao.find((Query)q).asList());
        return page;
    }
    
    private Query<UserReport> buildQuery(final Map<String, String> filters) {
        final Query<UserReport> q = (Query<UserReport>)this.userReportDao.createQuery();
        if (filters.containsKey("EQ_username")) {
            q.field("username").equal((Object)filters.get("EQ_username"));
        }
        if (filters.containsKey("EQ_tradeId")) {
            q.field("tradeId").equal((Object)filters.get("EQ_tradeId"));
        }
        if (filters.containsKey("EQ_mobile")) {
            q.field("mobile").equal((Object)filters.get("EQ_mobile"));
        }
        return q;
    }
}
