// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.Map;

class RechargeRuntimeState
{
    private final Map<String, Trade> trades;
    private final Map<String, ScheduledFuture<Void>> futures;
    
    public RechargeRuntimeState() {
        this.trades = new ConcurrentHashMap<String, Trade>();
        this.futures = new ConcurrentHashMap<String, ScheduledFuture<Void>>();
    }
    
    public void clearTradeState(final Trade trade) {
        this.trades.remove(trade.getTradeId());
        this.futures.remove(trade.getTradeId());
    }
    
    public void restoreTradeState(final Trade trade) {
        this.trades.put(trade.getTradeId(), trade);
    }
    
    public void cancelFutures() {
        synchronized (this.futures) {
            this.futures.values().stream().filter(future -> !future.isDone()).forEach(future -> future.cancel(true));
        }
    }
    
    public Trade fetchTrade(final String tradeId) {
        return this.trades.get(tradeId);
    }
    
    public void putTrace(final Trade trade) {
        this.trades.put(trade.getTradeId(), trade);
    }
    
    public void futureAdd(final String tradeId, final ScheduledFuture<Void> future) {
        this.futures.put(tradeId, future);
    }
    
    public int futureSize() {
        return this.futures.size();
    }
    
    public int tradeTotal() {
        return this.trades.size();
    }
    
    public Map<String, Trade> getTrades() {
        return this.trades;
    }
    
    public Map<String, ScheduledFuture<Void>> getFutures() {
        return this.futures;
    }
}
