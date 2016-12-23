// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import java.util.List;
import com.jiam365.flow.server.engine.Trade;
import com.jiam365.flow.server.entity.TradeRetry;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.TradeRetryDao;

public class TradeRetryManager
{
    @Autowired
    private TradeRetryDao tradeRetryDao;
    
    public void save(final TradeRetry tradeRetry) {
        this.tradeRetryDao.save((Object)tradeRetry);
    }
    
    public void saveRetry(final Trade trade) {
        if (trade.getConnection() != null) {
            final TradeRetry retry = new TradeRetry();
            retry.setChannelId(trade.getConnection().channelId());
            retry.setChannelName(trade.getConnection().getChannel().getName());
            retry.setExecuteProductId(trade.getRequest().getExecuteProductId());
            retry.setTradeId(trade.getTradeId());
            retry.setFailReason(trade.getLastMessage());
            this.tradeRetryDao.save((Object)retry);
        }
    }
    
    public void remove(final String tradeId) {
        this.tradeRetryDao.removeByTradeId(tradeId);
    }
    
    public List<TradeRetry> findByTradeId(final String tradeId) {
        return this.tradeRetryDao.findByTradeId(tradeId);
    }
}
