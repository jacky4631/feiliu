// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.slf4j.LoggerFactory;
import com.jiam365.flow.server.engine.Trade;
import java.util.Iterator;
import com.jiam365.flow.server.entity.ChannelAccount;
import java.util.ArrayList;
import com.jiam365.flow.server.channel.FlowChannel;
import java.util.List;
import com.jiam365.flow.server.dao.FlowChannelDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.ChannelAccountDao;
import org.slf4j.Logger;

public class ChannelAccountManager
{
    private static Logger logger;
    @Autowired
    private ChannelAccountDao channelAccountDao;
    @Autowired
    private FlowChannelDao flowChannelDao;
    
    public List<FlowChannel> findNotEqualsZeroChannels() {
        final List<ChannelAccount> accounts = this.channelAccountDao.findNotEqualsZeroAccounts();
        final List<FlowChannel> channels = new ArrayList<FlowChannel>();
        for (final ChannelAccount account : accounts) {
            final FlowChannel channel = (FlowChannel)this.flowChannelDao.get((Object)account.getChannleId());
            if (channel != null) {
                channel.setBalance(account.getBalance());
                channels.add(channel);
            }
        }
        return channels;
    }
    
    public double trans2Channel(final long channelId, final double money) {
        this.changeBalance(channelId, money);
        return ((ChannelAccount)this.channelAccountDao.get((Object)channelId)).getBalance();
    }
    
    public double getBalance(final long channelId) {
        final ChannelAccount account = (ChannelAccount)this.channelAccountDao.get((Object)channelId);
        return (account == null) ? 0.0 : account.getBalance();
    }
    
    public void costing(final Trade trade) {
        final double money = trade.getCost();
        this.changeBalance(trade.getConnection().channelId(), -money);
    }
    
    public void refund(final Trade trade) {
        final double money = trade.getCost();
        this.changeBalance(trade.getConnection().channelId(), money);
    }
    
    public void refund(final long channelId, final double amount) {
        this.changeBalance(channelId, amount);
    }
    
    private void changeBalance(final long channelId, final double money) {
        try {
            ChannelAccount account = (ChannelAccount)this.channelAccountDao.get((Object)channelId);
            if (account == null) {
                account = new ChannelAccount();
                account.setChannleId(channelId);
                account.setBalance(money);
                this.channelAccountDao.save((Object)account);
            }
            else {
                this.channelAccountDao.incBalance(channelId, money);
            }
        }
        catch (Exception e) {
            ChannelAccountManager.logger.error("\u901a\u9053\u8d26\u6237\u8d44\u91d1\u5904\u7406\u5931\u8d25 {}, {}", (Object)channelId, (Object)money);
        }
    }
    
    static {
        ChannelAccountManager.logger = LoggerFactory.getLogger((Class)ChannelAccountManager.class);
    }
}
