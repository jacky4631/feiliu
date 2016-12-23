// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service.statis;

import com.jiam365.flow.server.dto.TProfitByTecol;
import com.jiam365.flow.server.dto.TChannelProductTimeConsuming;
import com.jiam365.flow.server.dto.TProfitByState;
import com.jiam365.flow.server.entity.User;
import com.jiam365.flow.server.dto.TProfitByUser;
import com.jiam365.flow.server.channel.FlowChannel;
import com.jiam365.flow.server.dto.TProfitByChannel;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.jiam365.modules.telco.Telco;
import java.util.Date;
import com.jiam365.modules.utils.SimpleDateUtils;
import java.util.Map;
import com.jiam365.flow.server.utils.DoubleUtils;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.jiam365.flow.server.service.UserManager;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import com.jiam365.flow.server.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.TradeLogDao;

public class StatisticsManager
{
    @Autowired
    private TradeLogDao tradeLogDao;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private ChannelAdminManager channelAdminManager;
    @Autowired
    private UserManager userManager;
    private LoadingCache<PeriodKey, Double> profitCache;
    
    public StatisticsManager() {
        this.profitCache = (LoadingCache<PeriodKey, Double>)CacheBuilder.newBuilder().maximumSize(60L).expireAfterWrite(1L, TimeUnit.HOURS).build((CacheLoader)new CacheLoader<PeriodKey, Double>() {
            public Double load(final PeriodKey period) throws Exception {
                final double amount = StatisticsManager.this.tradeLogDao.getBillAmount(period.d1, period.d2);
                final double cost = StatisticsManager.this.tradeLogDao.getCostAmount(period.d1, period.d2);
                return DoubleUtils.sub(amount, cost);
            }
        });
    }
    
    public Map<String, Double> totayBillAmountGroupByUsername() {
        final Date[] period = SimpleDateUtils.getTodayPeriod();
        return this.tradeLogDao.getBillAmountGroupByUsername(period[0], period[1]);
    }
    
    public Map<String, Double> billAmountGroupByUsername(final Date startDate, final Date endDate) {
        return this.tradeLogDao.getBillAmountGroupByUsername(startDate, endDate);
    }
    
    public Map<Telco, Double> totayBillAmountByProvider(final String username) {
        final Date[] period = SimpleDateUtils.getTodayPeriod();
        return this.tradeLogDao.getBillAmountGroupByProvider(username, period[0], period[1]);
    }
    
    public double todayCostAmount() {
        final Date[] period = SimpleDateUtils.getTodayPeriod();
        return this.tradeLogDao.getCostAmount(period[0], period[1]);
    }
    
    public Map<Telco, Long> todayTradeCount(final String username, final int result) {
        final Date[] period = SimpleDateUtils.getTodayPeriod();
        return this.tradeLogDao.getTradeCountGroupByProvider(username, result, period[0], period[1]);
    }
    
    public List<TradeLogDao.TradeCountByChannel> todayTradeCountByChannel() {
        final Date[] period = SimpleDateUtils.getTodayPeriod();
        return this.tradeLogDao.getTradeCountGroupByChannel(period[0], period[1]);
    }
    
    public List<TradeLogDao.TradeCountByScope> todayNotFinishTradeCountByScope() {
        final Date[] period = SimpleDateUtils.getTodayPeriod();
        final List<TradeLogDao.TradeCountByScope> list = this.tradeLogDao.getNotFinishTradeCountByScope(period[0], period[1]);
        for (final TradeLogDao.TradeCountByScope t : list) {
            t.stateCode = this.mobileService.getStateNameByCode(t.stateCode);
        }
        return list;
    }
    
    public long todayFinsishTradeCountByChannel(final Long channleId) {
        final Date[] period = SimpleDateUtils.getTodayPeriod();
        return this.tradeLogDao.getFinsishTradeCountByChannel(channleId, period[0], period[1]);
    }
    
    public List<Double> getRecentDaysProfit(final int days) {
        final List<Double> profilts = new ArrayList<Double>();
        for (int i = -days + 1; i <= 0; ++i) {
            final Date[] period = SimpleDateUtils.getDayPeriod(i);
            if (i < 0) {
                try {
                    profilts.add((Double)this.profitCache.get((Object)new PeriodKey(period)));
                }
                catch (ExecutionException e) {
                    profilts.add(0.0);
                }
            }
            else {
                final double amount = this.tradeLogDao.getBillAmount(period[0], period[1]);
                final double cost = this.tradeLogDao.getCostAmount(period[0], period[1]);
                profilts.add(DoubleUtils.sub(amount, cost));
            }
        }
        return profilts;
    }
    
    public List<TProfitByChannel> getChannelCostProfit(final Date d1, final Date d2) {
        final List<TProfitByChannel> profitByChannelList = this.tradeLogDao.getChannelCostProfit(d1, d2);
        for (final TProfitByChannel profitByChannel : profitByChannelList) {
            final FlowChannel channel = this.channelAdminManager.get(profitByChannel.getChannelId());
            if (channel != null) {
                profitByChannel.setChannelName(channel.getName());
            }
        }
        return profitByChannelList;
    }
    
    public List<TProfitByUser> getUserCostProfit(final Date d1, final Date d2) {
        final List<TProfitByUser> profitByUsers = this.tradeLogDao.getUserCostProfit(d1, d2);
        for (final TProfitByUser profitByUser : profitByUsers) {
            final User user = this.userManager.loadUserByUsername(profitByUser.getUsername());
            if (user != null) {
                profitByUser.setDisplayName(user.getDisplayName());
                profitByUser.setCompany(user.getCompany());
            }
        }
        return profitByUsers;
    }
    
    public List<TProfitByState> getStateCostProfit(final Telco telco, final Date d1, final Date d2) {
        final List<TProfitByState> profits = this.tradeLogDao.getStateCostProfit(telco, d1, d2);
        for (final TProfitByState profit : profits) {
            profit.setStateName(this.mobileService.getStateNameByCode(profit.getStateCode()));
        }
        return profits;
    }
    
    public List<TChannelProductTimeConsuming> getTimeConsumingAverage(final Long channelId, final Telco telco, final Date d1, final Date d2) {
        final List<TChannelProductTimeConsuming> list = this.tradeLogDao.getTimeConsumingAverage(channelId, telco, d1, d2);
        for (final TChannelProductTimeConsuming c : list) {
            final FlowChannel channel = this.channelAdminManager.get(channelId);
            c.setChannelName((channel == null) ? "" : channel.getName());
            c.setStateName(this.mobileService.getStateNameByCode(c.getStateCode()));
        }
        return list;
    }
    
    public List<TProfitByTecol> getTecolCostProfit(final Date d1, final Date d2) {
        return this.tradeLogDao.getTecolCostProfit(d1, d2);
    }
    
    static class PeriodKey
    {
        Date d1;
        Date d2;
        
        PeriodKey(final Date[] period) {
            this.d1 = period[0];
            this.d2 = period[1];
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PeriodKey)) {
                return false;
            }
            final PeriodKey periodKey = (PeriodKey)o;
            return this.d1.equals(periodKey.d1) && this.d2.equals(periodKey.d2);
        }
        
        @Override
        public int hashCode() {
            int result = this.d1.hashCode();
            result = 31 * result + this.d2.hashCode();
            return result;
        }
    }
}
