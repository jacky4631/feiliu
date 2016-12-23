// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.server.channel.ChannelConnection;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.RechargeRequest;
import org.apache.commons.lang3.StringUtils;
import java.util.Iterator;
import java.util.List;
import com.jiam365.flow.server.params.SystemProperties;
import org.slf4j.LoggerFactory;
import java.util.Date;
import com.jiam365.flow.server.entity.TradeLog;
import java.util.concurrent.ExecutorService;
import com.jiam365.modules.utils.Threads;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import com.jiam365.flow.server.channel.ChannelConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.TradeLogManager;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import org.slf4j.Logger;

public class TerminatedRechargeManager
{
    private static Logger logger;
    private final ScheduledExecutorService executorService;
    private final ScheduledFuture future;
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private ChannelConnectionManager channelConnectionManager;
    @Autowired
    private BillingCenter billingCenter;
    
    public TerminatedRechargeManager() {
        this.executorService = Executors.newScheduledThreadPool(1);
        this.future = this.executorService.scheduleWithFixedDelay(new UnFinishTradeProcess(), 10L, 300L, TimeUnit.SECONDS);
    }
    
    public void shutdown() {
        this.future.cancel(true);
        Threads.gracefulShutdown((ExecutorService)this.executorService, 10, 60, TimeUnit.SECONDS);
    }
    
    private void checkTimeoutAndSave(final TradeLog log) {
        if (this.checkTimeout(log.getStartDate())) {
            TerminatedRechargeManager.logger.info("\u624b\u5de5\u7ec8\u6b62\u7684\u4ea4\u6613{}\u8865\u53d6\u72b6\u6001\u4ecd\u7136\u4e3a\u5904\u7406\u4e2d, \u5df2\u8d85\u65f6", (Object)log.getId());
            log.setChannelFinishDate(new Date());
            log.setChannelMessage("\u8d85\u65f6\u5931\u8d25");
            this.fail(log);
            this.tradeLogManager.save(log);
        }
    }
    
    private void fail(final TradeLog log) {
        this.billingCenter.refundChannelAccount(log.getId(), log.getChannelId(), log.getCostAmount());
        log.setChannelResult(-1);
        log.setCostAmount(0.0);
    }
    
    public boolean checkTimeout(final Date startTime) {
        return System.currentTimeMillis() - startTime.getTime() > TimeoutPolicy.timeoutSeconds() * 1000;
    }
    
    static {
        TerminatedRechargeManager.logger = LoggerFactory.getLogger((Class)TerminatedRechargeManager.class);
    }
    
    class UnFinishTradeProcess implements Runnable
    {
        @Override
        public void run() {
            try {
                if (!SystemProperties.isDebug()) {
                    this.loadAndProcess();
                }
            }
            catch (Exception e) {
                TerminatedRechargeManager.logger.error("Terminate trade process fail", (Throwable)e);
            }
        }
        
        private void loadAndProcess() {
            final List<TradeLog> tradeLogs = TerminatedRechargeManager.this.tradeLogManager.findTerminateTradeLog(100);
            for (final TradeLog log : tradeLogs) {
                this.processTrade(log);
            }
        }
        
        private void processTrade(final TradeLog log) {
            final long channelId = (log.getChannelId() == null) ? 0L : log.getChannelId();
            final ChannelConnection connection = TerminatedRechargeManager.this.channelConnectionManager.getConnection(channelId);
            final String reqNo = log.getRequestNo();
            if (connection != null) {
                if (StringUtils.isBlank((CharSequence)reqNo)) {
                    log.setChannelFinishDate(new Date());
                    log.setChannelMessage("\u63d0\u4ea4\u5931\u8d25, \u6ca1\u6709\u8fd4\u56de\u4ea4\u6613ID");
                    TerminatedRechargeManager.this.fail(log);
                    TerminatedRechargeManager.this.tradeLogManager.save(log);
                }
                else {
                    final RechargeRequest request = new RechargeRequest();
                    request.setMobile(log.getMobile());
                    request.setOrigiProductId(log.getOrigiProductId());
                    request.setUsername(log.getUsername());
                    try {
                        final ResponseData data = connection.queryReport(request, log.getRequestNo());
                        if (!data.canRetry()) {
                            TerminatedRechargeManager.logger.info("\u624b\u5de5\u7ec8\u6b62\u7684\u4ea4\u6613{}\u8865\u53d6\u72b6\u6001\u5f97\u5230\u6700\u7ec8\u72b6\u6001{}", (Object)log.getId(), (Object)(data.isSuccess() ? "\u6210\u529f" : "\u5931\u8d25"));
                            if (data.isSuccess()) {
                                log.setChannelResult(0);
                            }
                            else {
                                TerminatedRechargeManager.this.fail(log);
                            }
                            log.setChannelFinishDate(new Date());
                            log.setChannelMessage("[\u8865\u53d6]" + data.getMessage());
                            TerminatedRechargeManager.this.tradeLogManager.save(log);
                        }
                        else {
                            TerminatedRechargeManager.this.checkTimeoutAndSave(log);
                        }
                    }
                    catch (ChannelConnectionException e) {
                        TerminatedRechargeManager.this.checkTimeoutAndSave(log);
                    }
                }
            }
        }
    }
}
