// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import org.slf4j.LoggerFactory;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import com.jiam365.modules.utils.Threads;
import java.util.concurrent.ScheduledFuture;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.server.dto.ChoosedProduct;
import java.util.concurrent.Callable;
import com.jiam365.flow.server.entity.TradeLog;
import com.jiam365.flow.sdk.response.ResponseData;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ScheduledExecutorService;
import com.jiam365.flow.server.service.TradeStorageService;
import com.jiam365.flow.server.service.TradeLogManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;

public class TradeCenter implements InitializingBean
{
    private static final Logger logger;
    private BillingCenter billingCenter;
    private TradeLogManager tradeLogManager;
    private TradeStorageService tradeStorageService;
    private CallbackInterceptor callbackInterceptor;
    private final ScheduledExecutorService executorService;
    private final RechargeRuntimeState state;
    private volatile boolean isShutdown;
    
    public TradeCenter() {
        this.isShutdown = false;
        this.state = new RechargeRuntimeState();
        final ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("flow-recharge-%d").build();
        (this.executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 2, factory)).scheduleAtFixedRate(new TimeoutChecker(), 10L, 120L, TimeUnit.SECONDS);
    }
    
    public boolean isShutdown() {
        return this.isShutdown;
    }
    
    protected void onTradeFinish(final boolean isSuccess, final Trade trade) {
        this.state.clearTradeState(trade);
        if (!isSuccess && !trade.isDummyResult()) {
            this.billingCenter.refundChannelAccount(trade);
            this.tradeLogManager.updateChannelResultAndCost(false, trade);
        }
        this.callbackInterceptor.intercept(isSuccess, trade);
    }
    
    public void continueFail(final Trade trade) {
        this.callbackInterceptor.finish(false, trade);
    }
    
    public void continueFailAsSuccess(final Trade trade) {
        this.callbackInterceptor.continueFailAsSuccess(trade);
    }
    
    protected void onRechareRequestFinish(final Trade trade, final int delaySeconds) {
        this.tradeLogManager.updateTradeReqNo(trade);
        this.continueTrade(trade, delaySeconds);
    }
    
    public boolean onResult(final String tradeId, final boolean isSuccess) {
        final Trade trade = this.state.fetchTrade(tradeId);
        if (trade == null) {
            return false;
        }
        final String reqNo = trade.getRequestNo();
        final ResponseData data = isSuccess ? ResponseData.newSuccess(reqNo) : ResponseData.newFail(reqNo);
        data.setMessage(isSuccess ? "[\u6210\u529f]" : "[\u5931\u8d25]");
        data.setDummy(true);
        trade.onResult(data);
        return true;
    }
    
    public void beginTrade(final Trade trade) throws InsufficientBalanceException {
        this.billingCenter.pay(trade);
        this.state.putTrace(trade);
        final TradeLog tradeLog = new TradeLog(trade);
        this.tradeLogManager.save(tradeLog);
        if (trade.assignedToConnection()) {
            this.executorService.submit((Callable<Object>)trade);
        }
        else {
            trade.delayMe(true);
        }
    }
    
    public void restartTrade(final Trade trade, final ChoosedProduct choosedProduct) {
        final RechargeRequest request = trade.getRequest();
        request.setOrigiProductId(choosedProduct.getOrigiProductId());
        request.setOrigiDiscount(choosedProduct.getCostDiscount());
        request.setExecuteProductId(choosedProduct.getProductId());
        request.setExecuteProductPrice(choosedProduct.getCostPrice());
        trade.setConnection(choosedProduct.connection);
        this.billingCenter.payChannelAccount(trade);
        this.tradeLogManager.updateAfterReRoute(trade);
        this.state.putTrace(trade);
        this.executorService.submit((Callable<Object>)trade);
    }
    
    protected void continueTrade(final Trade callable, final int delaySeconds) {
        if (!this.isShutdown) {
            if (callable.assignedToConnection()) {
                final ScheduledFuture<Void> future = this.executorService.schedule((Callable<Void>)callable, delaySeconds, TimeUnit.SECONDS);
                this.state.futureAdd(callable.getTradeId(), future);
            }
        }
        else {
            TradeCenter.logger.warn("\u4ea4\u6613" + callable.getTradeId() + "\u4e0d\u53ef\u7ee7\u7eed\u6267\u884c,\u670d\u52a1\u5668\u5df2\u7ecf\u9000\u51fa");
        }
    }
    
    public void shutdown() {
        TradeCenter.logger.info("\u6b63\u5728\u5173\u95ed\u4ea4\u6613\u4e2d\u5fc3, \u8bf7\u7a0d\u5019, \u672a\u5b8c\u6210\u4ea4\u6613{}\u4e2a, Future {}\u4e2a, \u91cd\u542f\u540e\u7ee7\u7eed...", (Object)this.state.tradeTotal(), (Object)this.state.futureSize());
        this.isShutdown = true;
        this.state.cancelFutures();
        Threads.gracefulShutdown((ExecutorService)this.executorService, 10, 60, TimeUnit.SECONDS);
        this.tradeStorageService.storePendingTrades(this.state.getTrades());
        TradeCenter.logger.info("\u4ea4\u6613\u4e2d\u5fc3\u5b8c\u6210\u5173\u95ed");
    }
    
    public Trade fetchTrade(final String tradeId) {
        return this.state.fetchTrade(tradeId);
    }
    
    public boolean notFinish(final String tradeId) {
        return this.state.fetchTrade(tradeId) != null;
    }
    
    public void setTradeLogManager(final TradeLogManager tradeLogManager) {
        this.tradeLogManager = tradeLogManager;
    }
    
    public void setTradeStorageService(final TradeStorageService tradeStorageService) {
        this.tradeStorageService = tradeStorageService;
    }
    
    public void setBillingCenter(final BillingCenter billingCenter) {
        this.billingCenter = billingCenter;
    }
    
    public void setCallbackInterceptor(final CallbackInterceptor callbackInterceptor) {
        this.callbackInterceptor = callbackInterceptor;
    }
    
    public void afterPropertiesSet() throws Exception {
        final List<Trade> trades = this.tradeStorageService.loadPendingTrades(this);
        TradeCenter.logger.info("\u52a0\u8f7d\u672a\u5b8c\u6210\u4ea4\u6613\u7684\u5145\u503c\u4e8b\u52a1{}\u4e2a", (Object)trades.size());
        for (final Trade trade : trades) {
            this.state.restoreTradeState(trade);
            this.continueTrade(trade, 0);
        }
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)TradeCenter.class);
    }
    
    class TimeoutChecker implements Runnable
    {
        @Override
        public void run() {
            try {
                if (TimeoutPolicy.isAutoProcess()) {
                    this.check();
                }
            }
            catch (Exception e) {
                TradeCenter.logger.error("Timeout checker fail", (Throwable)e);
            }
        }
        
        private void check() {
            for (final Trade trade : TradeCenter.this.state.getTrades().values()) {
                if (trade.checkTimeout()) {
                    trade.onTimeout();
                }
            }
        }
    }
}
