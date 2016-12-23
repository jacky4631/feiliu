// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.apache.commons.lang3.time.DateFormatUtils;
import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import com.jiam365.flow.server.dto.UnProcessedTradeCount;
import com.jiam365.modules.utils.SimpleDateUtils;
import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import com.jiam365.modules.telco.Telco;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.server.engine.Trade;
import com.jiam365.flow.sdk.response.ResponseData;
import java.util.Date;
import java.util.List;
import com.jiam365.flow.server.entity.User;
import com.mongodb.MongoClientException;
import org.slf4j.LoggerFactory;
import com.jiam365.flow.server.entity.TradeLog;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.TradeLogDao;
import org.slf4j.Logger;

public class TradeLogManager
{
    private static Logger logger;
    @Autowired
    private TradeLogDao tradeLogDao;
    @Autowired
    private UserManager userManager;
    
    public void save(final TradeLog tradeLog) {
        try {
            final User user = this.userManager.getUserByUsername(tradeLog.getUsername());
            tradeLog.setDisplayUsername(user.getDisplayName());
            this.tradeLogDao.save((Object)tradeLog);
        }
        catch (MongoClientException e) {
            TradeLogManager.logger.warn("\u4ea4\u6613\u65e5\u5fd7\u4fdd\u5b58\u5931\u8d25, \u8bf7\u624b\u5de5\u5904\u7406\u8be5\u4ea4\u6613\u65e5\u5fd7, \u8be6\u89c1\u672a\u5904\u7406\u4ea4\u6613\u65e5\u5fd7\u6587\u4ef6");
            final Logger log = LoggerFactory.getLogger("unsave_trade");
            log.info("\u672a\u6210\u529f\u5165\u5e93\u7684\u4ea4\u6613\u65e5\u5fd7: {}", (Object)tradeLog.asString());
        }
    }
    
    public void delete(final String tradeId) {
        this.tradeLogDao.deleteById((Object)tradeId);
    }
    
    public List<TradeLog> findTerminateTradeLog(final int max) {
        return this.tradeLogDao.findTerminateTradeLog(max);
    }
    
    public List<TradeLog> findOldTradeLog(final Date before, final int max) {
        return this.tradeLogDao.findOldTradeLog(before, max);
    }
    
    public void updateAfterTimeout(final ResponseData data) {
        final TradeLog log = this.getByRequestNo(data.getRequestNo());
        if (log == null) {
            TradeLogManager.logger.warn("\u6536\u5230\u672a\u77e5\u7684\u54cd\u5e94, {}", (Object)data.toString());
        }
        else {
            log.setChannelResult(data.isSuccess() ? 0 : -1);
            log.setChannelFinishDate(new Date());
            log.setChannelMessage("\u7ed3\u675f\u540e\u6536\u5230\u7684\u62a5\u6587:" + data.getMessage());
            TradeLogManager.logger.warn("\u6536\u5230\u5df2\u7ecf\u7ed3\u675f\u7684\u4ea4\u6613\u54cd\u5e94, {}, \u8865\u5145\u8bb0\u5f55\u5230\u65e5\u5fd7\u7684message\u4fe1\u606f\u4e2d", (Object)data.toString());
            this.save(log);
        }
    }
    
    public void updateAfterReRoute(final Trade trade) {
        final TradeLog log = (TradeLog)this.tradeLogDao.get((Object)trade.getTradeId());
        if (log != null) {
            log.setChannelId(trade.getConnection().channelId());
            log.setChannelName(trade.getConnection().getChannel().getName());
            log.setChannelFinishDate(null);
            log.setChannelMessage(null);
            log.setChannelResult(9);
            log.setCostAmount(trade.getCost());
            log.setCostDiscount(trade.getRequest().getOrigiDiscount());
            log.setOrigiProductId(trade.getRequest().getOrigiProductId());
            log.setExecuteProductId(trade.getRequest().getExecuteProductId());
            log.setExecuteProductPrice(trade.getRequest().getExecuteProductPrice());
            log.setBillAmount(trade.getBillAmount());
            log.setBillDiscount(trade.getBillDiscount());
            this.save(log);
        }
        else {
            TradeLogManager.logger.error("\u91cd\u65b0\u9009\u901a\u9053\u540e,\u4ea4\u6613\u65e5\u5fd7\u627e\u4e0d\u5230 TradeId {}", (Object)trade.getTradeId());
        }
    }
    
    public void clearTradeLogBillInfo(final String tradeId) {
        final TradeLog log = (TradeLog)this.tradeLogDao.get((Object)tradeId);
        if (log != null) {
            log.setBillAmount(0.0);
            this.tradeLogDao.save((Object)log);
        }
    }
    
    public void updateRemark(final String tradeId, final String remark) {
        this.tradeLogDao.updateRemark(tradeId, remark);
    }
    
    public void updateChannelMessage(final String tradeId, final String message) {
        final TradeLog tradeLog = (TradeLog)this.tradeLogDao.get((Object)tradeId);
        if (tradeLog != null && tradeLog.getResult() == 9) {
            if (StringUtils.isBlank((CharSequence)tradeLog.getRemark())) {
                tradeLog.setRemark("\u539f\u4e0a\u6e38\u6d88\u606f: " + tradeLog.getChannelMessage());
            }
            tradeLog.setChannelMessage(message);
        }
        this.tradeLogDao.save((Object)tradeLog);
    }
    
    public void updateChannelResultAndCost(final boolean isSuccess, final Trade trade) {
        this.tradeLogDao.updateChannelResultAndCost(isSuccess, trade);
    }
    
    public void updateOnFinish(final boolean isSuccess, final Trade trade) {
        TradeLogManager.logger.debug("\u66f4\u65b0\u4ea4\u6613{}\u7684\u72b6\u6001\u4e3a{}, \u6700\u540e\u6d88\u606f:{}", new Object[] { trade.getTradeId(), isSuccess ? "\u6210\u529f" : "\u5931\u8d25", trade.getLastMessage() });
        try {
            this.tradeLogDao.updateOnFinish(isSuccess, trade);
        }
        catch (MongoClientException e) {
            TradeLogManager.logger.warn("\u4ea4\u6613\u65e5\u5fd7\u66f4\u65b0\u5931\u8d25, \u8bf7\u624b\u5de5\u5904\u7406\u8be5\u4ea4\u6613\u65e5\u5fd7, \u8be6\u89c1\u672a\u5904\u7406\u4ea4\u6613\u65e5\u5fd7\u6587\u4ef6");
            final Logger log = LoggerFactory.getLogger("unsave_trade");
            log.info("\u672a\u6210\u529f\u66f4\u65b0\u7684\u4ea4\u6613\u65e5\u5fd7: {}, \u6210\u529f\u4e0e\u5426 {}, \u6700\u540e\u6d88\u606f: {}", new Object[] { trade.getTradeId(), isSuccess, trade.getLastMessage() });
        }
    }
    
    public void updateTradeReqNo(final Trade trade) {
        try {
            this.tradeLogDao.updateTradeReqNo(trade.getTradeId(), trade.getRequestNo());
        }
        catch (MongoClientException e) {
            TradeLogManager.logger.error("\u672a\u6210\u529f\u66f4\u65b0tradeId: {}, reqNo: {}", (Object)trade.getTradeId(), (Object)trade.getRequestNo());
        }
    }
    
    public long getSuccessCount(final long channelId, final String origiProductId, final Date startDate, final Date endDate) {
        return this.tradeLogDao.getSuccessCount(channelId, origiProductId, startDate, endDate);
    }
    
    public long getUserTradeLogCount(final String username) {
        return this.tradeLogDao.countUsersTradeLog(username);
    }
    
    public long getChannelTradeLogCount(final long channelId) {
        return this.tradeLogDao.countChannelTradeLog(channelId);
    }
    
    public List<TradeLog> findPendingTradeLog(final Telco provider, final String stateCode) {
        return this.tradeLogDao.findPendingTradeLog(provider, stateCode);
    }
    
    public List<TradeLog> findRunningTradeLog() {
        return this.tradeLogDao.findRunningTradeLog();
    }
    
    public Page<TradeLog> searchPage(final Page<TradeLog> page) {
        final PageParseMongo<TradeLog> builder = (PageParseMongo<TradeLog>)new PageParseMongo((Page)page);
        final Query<TradeLog> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.tradeLogDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.tradeLogDao.find((Query)q).asList());
        return page;
    }
    
    private Query<TradeLog> buildQuery(final Map<String, String> filters) {
        final Query<TradeLog> q = (Query<TradeLog>)this.tradeLogDao.createQuery();
        if (filters.containsKey("EQ_id")) {
            q.field("id").equal((Object)filters.get("EQ_id"));
        }
        if (filters.containsKey("EQ_mobile")) {
            q.field("mobile").equal((Object)filters.get("EQ_mobile"));
        }
        if (filters.containsKey("GED_startDate")) {
            final String startDate = filters.get("GED_startDate");
            final Date d = SimpleDateUtils.safeParseDate(startDate, "yyyy-MM-dd");
            if (d != null) {
                q.filter("startDate >=", (Object)SimpleDateUtils.getDateStart(d));
            }
        }
        if (filters.containsKey("LED_startDate")) {
            final String finishDate = filters.get("LED_startDate");
            final Date d = SimpleDateUtils.safeParseDate(finishDate, "yyyy-MM-dd");
            if (d != null) {
                q.filter("startDate <=", (Object)SimpleDateUtils.getDateEnd(d));
            }
        }
        if (filters.containsKey("EQ_productId")) {
            q.field("productId").equal((Object)filters.get("EQ_productId"));
        }
        if (filters.containsKey("EQ_type")) {
            final String type = filters.get("EQ_type");
            if ("0".equals(type)) {
                q.filter("channelId =", (Object)null);
                q.filter("result =", (Object)9);
            }
            else if ("1".equals(type)) {
                q.filter("result =", (Object)9);
                q.filter("channelResult =", (Object)(-1));
            }
            else if ("2".equals(type)) {
                q.filter("result =", (Object)9);
                Date d = new Date();
                d = SimpleDateUtils.getDateStart(d);
                q.filter("startDate <", (Object)d);
            }
        }
        if (filters.containsKey("GED_timeConsuming")) {
            q.filter("timeConsuming >=", (Object)(Integer.valueOf(filters.get("GED_timeConsuming")) * 3600));
        }
        if (filters.containsKey("EQ_username")) {
            q.field("username").equal((Object)filters.get("EQ_username"));
        }
        if (filters.containsKey("EQ_state")) {
            q.field("stateCode").equal((Object)filters.get("EQ_state"));
        }
        if (filters.containsKey("EQ_channel")) {
            q.field("channelId").equal((Object)Long.parseLong(filters.get("EQ_channel")));
        }
        if (filters.containsKey("EQ_provider")) {
            q.field("provider").equal((Object)Telco.valueOf((String)filters.get("EQ_provider")));
        }
        if (filters.containsKey("EQ_result")) {
            q.field("result").equal((Object)Integer.parseInt(filters.get("EQ_result")));
        }
        if (filters.containsKey("EQ_size")) {
            q.field("size").equal((Object)Integer.parseInt(filters.get("EQ_size")));
        }
        return q;
    }
    
    public UnProcessedTradeCount getUnProcessedCount() {
        final long pendingCount = this.tradeLogDao.getPendingTradeCount();
        final long unCallbackCount = this.tradeLogDao.getUnCallbakFailTradeCount();
        return new UnProcessedTradeCount(pendingCount, unCallbackCount);
    }
    
    public TradeLog get(final String id) {
        return (TradeLog)this.tradeLogDao.get((Object)id);
    }
    
    public TradeLog getByUserRequestNo(final String username, final String userRequestNo) {
        return this.tradeLogDao.getByUserRequestNo(username, userRequestNo);
    }
    
    public TradeLog getByRequestNo(final String requestNo) {
        return this.tradeLogDao.getByRequestNo(requestNo);
    }
    
    public void dump2CSV(final String fullFileName, final Page<TradeLog> page) {
        final char SEPERATOR = ',';
        try (final BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fullFileName), "GBK"))) {
            fileWriter.write("\u6d41\u6c34\u53f7,\u8d26\u53f7,\u7528\u6237\u6d41\u6c34\u53f7,\u63d0\u4ea4\u65f6\u95f4,\u5145\u503c\u53f7\u7801,\u5f52\u5c5e\u5730,\u5957\u9910\u5305,\u4ea7\u54c1ID,\u539f\u4ef7,\u6298\u6263,\u5b9e\u6263,\u5b8c\u6210\u65f6\u95f4,\u72b6\u6001\r\n");
            final Iterator<TradeLog> tradeLogs = this.findExportTradeLogs(page);
            while (tradeLogs.hasNext()) {
                final TradeLog log = tradeLogs.next();
                fileWriter.write(log.getId() + ',' + log.getUsername() + ',' + log.getUserRequestNo() + ',' + this.safeFormat(log.getStartDate()) + ',' + log.getMobile() + ',' + log.getMobileInfo() + ',' + log.getSize() + "MB" + ',' + log.getProductId() + ',' + log.getPrice() + ',' + log.getBillDiscount() + ',' + log.getBillAmount() + ',' + this.safeFormat(log.getFinishDate()) + ',' + log.getResultDescription());
                fileWriter.write("\r\n");
            }
            fileWriter.flush();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void dumpCostList2CSV(final String fullFileName, final Page<TradeLog> page) {
        final char SEPERATOR = ',';
        try (final BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fullFileName), "GBK"))) {
            fileWriter.write("\u6d41\u6c34\u53f7,\u4f9b\u5e94\u5546,\u4e0a\u6e38\u5bf9\u8d26\u6d41\u6c34,\u63d0\u4ea4\u65f6\u95f4,\u5145\u503c\u53f7\u7801,\u5f52\u5c5e\u5730,\u5957\u9910\u5305,\u4e0a\u6e38\u4ea7\u54c1ID,\u539f\u4ef7,\u6210\u672c\u6298\u6263,\u6210\u672c,\u5b8c\u6210\u65f6\u95f4,\u72b6\u6001\r\n");
            final Iterator<TradeLog> tradeLogs = this.findExportTradeLogs(page);
            while (tradeLogs.hasNext()) {
                final TradeLog log = tradeLogs.next();
                fileWriter.write(log.getId() + ',' + log.getChannelName() + ',' + log.getRequestNo() + ',' + this.safeFormat(log.getStartDate()) + ',' + log.getMobile() + ',' + log.getMobileInfo() + ',' + log.getSize() + "MB" + ',' + log.getOrigiProductId() + ',' + log.getPrice() + ',' + log.getCostDiscount() + ',' + log.getCostAmount() + ',' + this.safeFormat(log.getFinishDate()) + ',' + log.getResultDescription());
                fileWriter.write("\r\n");
            }
            fileWriter.flush();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Iterator<TradeLog> findExportTradeLogs(final Page<TradeLog> page) {
        page.setTotalCount(500000L);
        final PageParseMongo<TradeLog> builder = (PageParseMongo<TradeLog>)new PageParseMongo((Page)page);
        final Map<String, String> conditons = (Map<String, String>)builder.searchParams();
        if (StringUtils.isBlank((CharSequence)conditons.get("GED_startDate")) && StringUtils.isBlank((CharSequence)conditons.get("LED_startDate"))) {
            final Date[] period = SimpleDateUtils.getMonthPeriod();
            conditons.put("GED_startDate", DateFormatUtils.format(period[0], "yyyy-MM-dd"));
            conditons.put("LED_startDate", DateFormatUtils.format(period[1], "yyyy-MM-dd"));
        }
        final Query<TradeLog> q = this.buildQuery(conditons);
        page.setTotalCount(this.tradeLogDao.count((Query)q));
        q.order("startDate");
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        return (Iterator<TradeLog>)q.iterator();
    }
    
    private String safeFormat(final Date date) {
        return (date == null) ? "" : DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }
    
    static {
        TradeLogManager.logger = LoggerFactory.getLogger((Class)TradeLogManager.class);
    }
}
