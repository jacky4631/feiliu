// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.jiam365.flow.server.engine.TradeCenter;
import java.util.Iterator;
import com.jiam365.flow.server.entity.PendingTrade;
import com.jiam365.flow.server.engine.Trade;
import java.util.Map;
import com.jiam365.flow.server.channel.ChannelConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.PendingTradeDao;

public class TradeStorageService
{
    @Autowired
    private PendingTradeDao pendingTradeDao;
    @Autowired
    private ChannelConnectionManager channelConnectionManager;
    
    public void storePendingTrades(final Map<String, Trade> trades) {
        for (final Trade trade : trades.values()) {
            final PendingTrade pendingTrade = new PendingTrade(trade);
            this.pendingTradeDao.save((Object)pendingTrade);
        }
    }
    
    public void store(final PendingTrade pendingTrade) {
        this.pendingTradeDao.save((Object)pendingTrade);
    }
    
    public List<Trade> loadPendingTrades(final TradeCenter central) {
        List<PendingTrade> pendingTrades = Collections.emptyList();
        try {
            pendingTrades = (List<PendingTrade>)this.pendingTradeDao.find().asList();
        }
        catch (Exception ex) {}
        final List<Trade> trades = new ArrayList<Trade>();
        for (final PendingTrade pendingTrade : pendingTrades) {
            final Trade trade = pendingTrade.toTrade(central, this.channelConnectionManager);
            if (trade != null) {
                trades.add(trade);
                this.pendingTradeDao.deleteById((Object)pendingTrade.getTradeId());
            }
        }
        return trades;
    }
}
