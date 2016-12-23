// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.annotations.Id;
import org.slf4j.LoggerFactory;
import com.jiam365.flow.server.dto.TChannelProductTimeConsuming;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.aggregation.Sort;
import com.jiam365.flow.server.dto.TProfitByState;
import com.jiam365.flow.server.dto.TProfitByTecol;
import com.jiam365.flow.server.dto.TProfitByUser;
import java.util.ArrayList;
import com.jiam365.flow.server.dto.TProfitByChannel;
import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.flow.server.dto.DoubleWrapper;
import java.util.Iterator;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.query.MorphiaIterator;
import java.util.HashMap;
import org.mongodb.morphia.aggregation.Group;
import java.util.Map;
import com.jiam365.flow.server.engine.Trade;
import org.mongodb.morphia.query.UpdateOperations;
import com.jiam365.modules.telco.Telco;
import java.util.List;
import org.mongodb.morphia.query.Query;
import org.apache.commons.lang3.StringUtils;
import java.util.Date;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import com.jiam365.flow.server.entity.TradeLog;
import org.mongodb.morphia.dao.BasicDAO;

public class TradeLogDao extends BasicDAO<TradeLog, String>
{
    private static Logger logger;
    
    public TradeLogDao(final Datastore ds) {
        super(ds);
    }
    
    public long getSuccessCount(final long channelId, final String origiProductId, final Date startDate, final Date endDate) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("result !=", (Object)0);
        if (StringUtils.isNotBlank((CharSequence)origiProductId)) {
            query.filter("origiProductId", (Object)origiProductId);
        }
        query.filter("channelId", (Object)channelId);
        query.filter("startDate >=", (Object)startDate);
        query.filter("startDate <=", (Object)endDate);
        return this.count((Query)query);
    }
    
    public long getPendingTradeCount() {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("channelId =", (Object)null);
        query.filter("result =", (Object)9);
        return super.count((Query)query);
    }
    
    public long getUnCallbakFailTradeCount() {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("result =", (Object)9);
        query.filter("channelResult =", (Object)(-1));
        return super.count((Query)query);
    }
    
    public List<TradeLog> findAllTradeLogByUsername(final String username, final Date startDate, final Date endDate) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("username =", (Object)username);
        query.filter("result !=", (Object)(-1));
        query.filter("startDate >=", (Object)startDate);
        query.filter("startDate <=", (Object)endDate);
        return (List<TradeLog>)query.asList();
    }
    
    public List<TradeLog> findRunningTradeLog() {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("channelId !=", (Object)null);
        query.filter("result =", (Object)9);
        query.filter("channelResult =", (Object)9);
        return (List<TradeLog>)query.asList();
    }
    
    public List<TradeLog> findPendingTradeLog(final Telco telco, final String stateCode) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("channelId =", (Object)null);
        query.filter("result =", (Object)9);
        if (telco != null) {
            query.filter("provider", (Object)telco);
        }
        if (stateCode != null) {
            query.filter("stateCode", (Object)stateCode);
        }
        return (List<TradeLog>)query.asList();
    }
    
    public List<TradeLog> findOldTradeLog(final Date beforeDate, final int rowLimit) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("result !=", (Object)9);
        query.filter("startDate <=", (Object)beforeDate);
        query.limit(rowLimit);
        return (List<TradeLog>)query.asList();
    }
    
    public List<TradeLog> findTerminateTradeLog(final int rowLimit) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("channelId !=", (Object)null);
        query.filter("channelFinishDate", (Object)null);
        query.filter("finishDate !=", (Object)null);
        query.order("startDate");
        query.limit(rowLimit);
        return (List<TradeLog>)query.asList();
    }
    
    public List<TradeLog> findByChannleId(final long channelId) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("channelId", (Object)channelId);
        query.order("startDate");
        return (List<TradeLog>)query.asList();
    }
    
    public List<TradeLog> findByChannleIdAndPeriod(final long channelId, final Date startDate, final Date endDate) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("channelId", (Object)channelId);
        query.filter("startDate >=", (Object)startDate);
        query.filter("startDate <=", (Object)endDate);
        query.order("startDate");
        return (List<TradeLog>)query.asList();
    }
    
    public List<TradeLog> findByUsername(final String username) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("username", (Object)username);
        query.order("startDate");
        return (List<TradeLog>)query.asList();
    }
    
    public List<TradeLog> findByUsernameAndPeriod(final String username, final Date startDate, final Date endDate) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("username", (Object)username);
        query.filter("startDate >=", (Object)startDate);
        query.filter("startDate <=", (Object)endDate);
        query.order("startDate");
        return (List<TradeLog>)query.asList();
    }
    
    public TradeLog getByRequestNo(final String requestNo) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery().filter("requestNo", (Object)requestNo);
        return (TradeLog)this.findOne((Query)query);
    }
    
    public TradeLog getByUserRequestNo(final String username, final String userRequestNo) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery().filter("userRequestNo", (Object)userRequestNo);
        query.filter("username", (Object)username);
        return (TradeLog)this.findOne((Query)query);
    }
    
    public List<TradeLog> findByMobile(final String mobile) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery().filter("mobile", (Object)mobile);
        return (List<TradeLog>)this.find((Query)query).asList();
    }
    
    public long countUsersTradeLog(final String username) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery().filter("username", (Object)username);
        return this.count((Query)query);
    }
    
    public long countChannelTradeLog(final long channelId) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery().filter("channelId", (Object)channelId);
        return this.count((Query)query);
    }
    
    public void updateTradeReqNo(final String tradeId, final String reqNo) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery().filter("id", (Object)tradeId);
        final UpdateOperations<TradeLog> opts = (UpdateOperations<TradeLog>)this.createUpdateOperations();
        opts.set("requestNo", (Object)reqNo);
        opts.set("submitTime", (Object)new Date());
        this.update((Query)query, (UpdateOperations)opts);
    }
    
    public void updateRemark(final String tradeId, final String remark) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery().filter("id", (Object)tradeId);
        final UpdateOperations<TradeLog> opts = (UpdateOperations<TradeLog>)this.createUpdateOperations();
        opts.set("remark", (Object)remark);
        this.update((Query)query, (UpdateOperations)opts);
    }
    
    public void updateChannelMessage(final String tradeId, final String message) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery().filter("id", (Object)tradeId);
        final UpdateOperations<TradeLog> opts = (UpdateOperations<TradeLog>)this.createUpdateOperations();
        opts.set("channelMessage", (Object)message);
        this.update((Query)query, (UpdateOperations)opts);
    }
    
    public void updateChannelResultAndCost(final boolean isSuccess, final Trade trade) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery().filter("id", (Object)trade.getTradeId());
        final TradeLog log = (TradeLog)this.findOne((Query)query);
        if (log != null && !trade.isDummyResult()) {
            log.setChannelResult(isSuccess ? 0 : -1);
            log.setChannelMessage(trade.getLastMessage());
            log.setChannelFinishDate(new Date());
            if (!isSuccess) {
                log.setCostAmount(0.0);
            }
            this.save(log);
        }
    }
    
    public void updateOnFinish(final boolean isSuccess, final Trade trade) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery().filter("id", (Object)trade.getTradeId());
        TradeLog log = (TradeLog)this.findOne((Query)query);
        if (log != null) {
            log.setMessage(trade.getLastMessage());
            log.setRequestNo(trade.getRequestNo());
            log.setResult(isSuccess ? 0 : -1);
            log.setFinishDate(new Date());
            log.setCostAmount((isSuccess || trade.isDummyResult()) ? trade.getCost() : 0.0);
            final int timeConsuming = (int)(log.getFinishDate().getTime() - log.getStartDate().getTime()) / 1000;
            log.setTimeConsuming(timeConsuming);
        }
        else {
            TradeLogDao.logger.warn("遇到没有保存的交易记录{}/{}, 直接保存, 请检查问题产生的原因", (Object)trade.getTradeId(), (Object)trade.getRequestNo());
            log = new TradeLog(isSuccess, trade);
            TradeLogDao.logger.debug("最终保存交易日志 {}", (Object)trade.toString());
        }
        if (!trade.isDummyResult()) {
            log.setChannelResult(log.getResult());
            log.setChannelFinishDate(log.getFinishDate());
            log.setChannelMessage(log.getMessage());
        }
        this.save(log);
    }
    
    public Map<String, Double> getBillAmountGroupByUsername(final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        query.filter("result !=", (Object)(-1));
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group("username", new Group[] { Group.grouping("totalBillAmount", Group.sum("billAmount")) });
        final Iterator<BillAmountTotal> iterator = (Iterator<BillAmountTotal>)pipeline.aggregate((Class)BillAmountTotal.class);
        final Map<String, Double> map = new HashMap<String, Double>();
        try {
            while (iterator.hasNext()) {
                try {
                    final BillAmountTotal p = iterator.next();
                    map.put(p.username, p.totalBillAmount);
                }
                catch (NullPointerException e) {
                    TradeLogDao.logger.warn("聚合KEY存在null值, 请检查数据流逻辑, 是否存在可能的问题");
                }
            }
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
        return map;
    }
    
    public double getBillAmount(final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        query.filter("result !=", (Object)(-1));
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group((String)null, new Group[] { Group.grouping("amount", Group.sum("billAmount")) });
        final Iterator<DoubleWrapper> iterator = (Iterator<DoubleWrapper>)pipeline.aggregate((Class)DoubleWrapper.class);
        try {
            while (iterator.hasNext()) {
                try {
                    final DoubleWrapper p = iterator.next();
                    return DoubleUtils.round(p.amount, 2);
                }
                catch (NullPointerException e) {
                    TradeLogDao.logger.warn("getBillAmount时,聚合KEY存在null值, 请检查数据流逻辑, 是否存在可能的问题");
                    continue;
                }
            }
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
        return 0.0;
    }
    
    public double getCostAmount(final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        query.filter("result !=", (Object)(-1));
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group((String)null, new Group[] { Group.grouping("amount", Group.sum("costAmount")) });
        final Iterator<DoubleWrapper> iterator = (Iterator<DoubleWrapper>)pipeline.aggregate((Class)DoubleWrapper.class);
        try {
            while (iterator.hasNext()) {
                try {
                    final DoubleWrapper p = iterator.next();
                    return DoubleUtils.round(p.amount, 2);
                }
                catch (NullPointerException e) {
                    TradeLogDao.logger.warn("聚合KEY存在null值, 请检查数据流逻辑, 是否存在可能的问题");
                    continue;
                }
            }
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
        return 0.0;
    }
    
    public Map<Telco, Double> getBillAmountGroupByProvider(final String username, final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        if (StringUtils.isNotBlank((CharSequence)username)) {
            query.filter("username", (Object)username);
        }
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group("provider", new Group[] { Group.grouping("totalBillAmount", Group.sum("billAmount")) });
        final Map<Telco, Double> map = new HashMap<Telco, Double>();
        final Iterator<UserBillAmountTotal> iterator = (Iterator<UserBillAmountTotal>)pipeline.aggregate((Class)UserBillAmountTotal.class);
        try {
            while (iterator.hasNext()) {
                try {
                    final UserBillAmountTotal p = iterator.next();
                    map.put(p.provider, p.totalBillAmount);
                }
                catch (NullPointerException e) {
                    TradeLogDao.logger.warn("聚合KEY存在null值, 请检查数据流逻辑, 是否存在可能的问题");
                }
            }
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
        return map;
    }
    
    public List<TProfitByChannel> getChannelCostProfit(final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        query.filter("channelId !=", (Object)null);
        query.filter("result !=", (Object)9);
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group("channelId", new Group[] { Group.grouping("cost", Group.sum("costAmount")), Group.grouping("amount", Group.sum("billAmount")) });
        final List<TProfitByChannel> profitByChannelList = new ArrayList<TProfitByChannel>();
        final Iterator<ChannelCostProfit> iterator = (Iterator<ChannelCostProfit>)pipeline.aggregate((Class)ChannelCostProfit.class);
        try {
            while (iterator.hasNext()) {
                final ChannelCostProfit p = iterator.next();
                profitByChannelList.add(new TProfitByChannel(p.channelId, p.cost, p.amount));
            }
            return profitByChannelList;
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
    }
    
    public List<TProfitByUser> getUserCostProfit(final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        query.filter("result !=", (Object)9);
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group("username", new Group[] { Group.grouping("cost", Group.sum("costAmount")), Group.grouping("amount", Group.sum("billAmount")) });
        final List<TProfitByUser> profitByUsers = new ArrayList<TProfitByUser>();
        final Iterator<UserCostProfit> iterator = (Iterator<UserCostProfit>)pipeline.aggregate((Class)UserCostProfit.class);
        try {
            while (iterator.hasNext()) {
                final UserCostProfit p = iterator.next();
                profitByUsers.add(new TProfitByUser(p.username, p.cost, p.amount));
            }
            return profitByUsers;
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
    }
    
    public List<TProfitByTecol> getTecolCostProfit(final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        query.filter("result !=", (Object)9);
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group("provider", new Group[] { Group.grouping("cost", Group.sum("costAmount")), Group.grouping("amount", Group.sum("billAmount")) });
        final List<TProfitByTecol> profitByTecolList = new ArrayList<TProfitByTecol>();
        final Iterator<TecolCostProfit> iterator = (Iterator<TecolCostProfit>)pipeline.aggregate((Class)TecolCostProfit.class);
        try {
            while (iterator.hasNext()) {
                final TecolCostProfit p = iterator.next();
                profitByTecolList.add(new TProfitByTecol(p.telco, p.cost, p.amount));
            }
            return profitByTecolList;
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
    }
    
    public List<TProfitByState> getStateCostProfit(final Telco telco, final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        if (telco != null) {
            query.filter("provider", (Object)telco);
        }
        query.filter("stateCode !=", (Object)null);
        query.filter("result !=", (Object)9);
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group("stateCode", new Group[] { Group.grouping("cost", Group.sum("costAmount")), Group.grouping("amount", Group.sum("billAmount")) }).sort(new Sort[] { new Sort("amount", -1) });
        final Iterator<StateCostProfit> iterator = (Iterator<StateCostProfit>)pipeline.aggregate((Class)StateCostProfit.class);
        final List<TProfitByState> list = new ArrayList<TProfitByState>();
        try {
            while (iterator.hasNext()) {
                final StateCostProfit p = iterator.next();
                list.add(new TProfitByState(p.stateCode, p.cost, p.amount));
            }
            return list;
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
    }
    
    public Map<Telco, Long> getTradeCountGroupByProvider(final String username, final int result, final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        if (StringUtils.isNotBlank((CharSequence)username)) {
            query.filter("username", (Object)username);
        }
        query.filter("result", (Object)result);
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group("provider", new Group[] { Group.grouping("count", new Accumulator("$sum", (Object)1)) });
        final Map<Telco, Long> map = new HashMap<Telco, Long>();
        final Iterator<TradeCountByTelco> iterator = (Iterator<TradeCountByTelco>)pipeline.aggregate((Class)TradeCountByTelco.class);
        try {
            while (iterator.hasNext()) {
                try {
                    final TradeCountByTelco t = iterator.next();
                    map.put(t.provider, t.count);
                }
                catch (NullPointerException e) {
                    TradeLogDao.logger.warn("聚合KEY存在null值, 请检查数据流逻辑, 是否存在可能的问题");
                }
            }
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
        return map;
    }
    
    public List<TradeCountByScope> getNotFinishTradeCountByScope(final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        query.filter("result =", (Object)9);
        query.filter("stateCode !=", (Object)null);
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group("stateCode", new Group[] { Group.grouping("count", new Accumulator("$sum", (Object)1)) }).sort(new Sort[] { new Sort("count", -1) }).limit(4);
        final List<TradeCountByScope> tradeCountByScope = new ArrayList<TradeCountByScope>();
        final Iterator<TradeCountByScope> iterator = (Iterator<TradeCountByScope>)pipeline.aggregate((Class)TradeCountByScope.class);
        try {
            while (iterator.hasNext()) {
                final TradeCountByScope t = iterator.next();
                tradeCountByScope.add(t);
            }
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
        return tradeCountByScope;
    }
    
    public List<TradeCountByChannel> getTradeCountGroupByChannel(final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        query.filter("channelId !=", (Object)null);
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group("channelId", new Group[] { Group.grouping("count", new Accumulator("$sum", (Object)1)) }).sort(new Sort[] { new Sort("count", -1) }).limit(5);
        final List<TradeCountByChannel> tradeCountByChannels = new ArrayList<TradeCountByChannel>();
        final Iterator<TradeCountByChannel> iterator = (Iterator<TradeCountByChannel>)pipeline.aggregate((Class)TradeCountByChannel.class);
        try {
            while (iterator.hasNext()) {
                try {
                    final TradeCountByChannel t = iterator.next();
                    tradeCountByChannels.add(t);
                }
                catch (NullPointerException e) {
                    TradeLogDao.logger.warn("聚合KEY存在null值, 请检查数据流逻辑, 是否存在可能的问题");
                }
            }
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
        return tradeCountByChannels;
    }
    
    public long getFinsishTradeCountByChannel(final Long channleId, final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("channelId", (Object)channleId);
        query.filter("result !=", (Object)9);
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        return query.countAll();
    }
    
    public List<TChannelProductTimeConsuming> getTimeConsumingAverage(final Long channelId, final Telco telco, final Date d1, final Date d2) {
        final Query<TradeLog> query = (Query<TradeLog>)this.createQuery();
        query.filter("channelId", (Object)channelId);
        query.filter("startDate >=", (Object)d1);
        query.filter("startDate <=", (Object)d2);
        query.filter("provider", (Object)telco);
        query.filter("stateCode !=", (Object)null);
        query.filter("result !=", (Object)9);
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)TradeLog.class).match((Query)query).group("stateCode", new Group[] { Group.grouping("timeConsuming", Group.average("timeConsuming")) }).sort(new Sort[] { new Sort("timeConsuming", -1) });
        final Iterator<ChannelProductTimeConsuming> iterator = (Iterator<ChannelProductTimeConsuming>)pipeline.aggregate((Class)ChannelProductTimeConsuming.class);
        final List<TChannelProductTimeConsuming> list = new ArrayList<TChannelProductTimeConsuming>();
        try {
            while (iterator.hasNext()) {
                final ChannelProductTimeConsuming c = iterator.next();
                final TChannelProductTimeConsuming consuming = new TChannelProductTimeConsuming();
                consuming.setChannelId(channelId);
                consuming.setTelco(telco);
                consuming.setStateCode(c.stateCode);
                consuming.setTimeConsuming(c.timeConsuming);
                list.add(consuming);
            }
            return list;
        }
        finally {
            ((MorphiaIterator)iterator).close();
        }
    }
    
    static {
        TradeLogDao.logger = LoggerFactory.getLogger((Class)TradeLog.class);
    }
    
    public static class ChannelProductTimeConsuming
    {
        @Id
        private String stateCode;
        private int timeConsuming;
    }
    
    public static class TradeCountByScope
    {
        @Id
        public String stateCode;
        public Long count;
    }
    
    public static class TradeCountByChannel
    {
        @Id
        public Long channelId;
        public Long count;
    }
    
    public static class TradeCountByTelco
    {
        @Id
        Telco provider;
        Long count;
    }
    
    public static class BillAmountTotal
    {
        @Id
        String username;
        Double totalBillAmount;
    }
    
    public static class UserBillAmountTotal
    {
        @Id
        Telco provider;
        Double totalBillAmount;
    }
    
    static class ChannelCostProfit
    {
        @Id
        Long channelId;
        Double cost;
        Double amount;
    }
    
    static class UserCostProfit
    {
        @Id
        String username;
        Double cost;
        Double amount;
    }
    
    static class StateCostProfit
    {
        @Id
        String stateCode;
        Double cost;
        Double amount;
    }
    
    static class TecolCostProfit
    {
        @Id
        Telco telco;
        Double cost;
        Double amount;
    }
}
