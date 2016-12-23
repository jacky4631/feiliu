// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import com.jiam365.modules.tools.SpringContext;
import com.jiam365.flow.server.event.RechargeFailEvent;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.flow.server.entity.TradeRetry;
import com.jiam365.flow.server.dto.ChoosedProduct;
import com.jiam365.flow.sdk.RechargeRequest;
import java.util.Iterator;
import java.util.List;
import com.jiam365.flow.server.entity.FlowCallbackInterceptor;
import com.jiam365.flow.server.service.RefundKeywordManager;
import com.jiam365.flow.server.service.FlowInterceptorManager;
import com.jiam365.flow.server.usercallback.UserReportManager;
import com.jiam365.flow.server.service.TradeRetryManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.TradeLogManager;
import org.slf4j.Logger;

public class CallbackInterceptor
{
    private static Logger logger;
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private TradeRetryManager tradeRetryManager;
    @Autowired
    private RechargeMobilePromptManager rechargeMobilePromptManager;
    @Autowired
    private UserReportManager userReportManager;
    @Autowired
    private BillingCenter billingCenter;
    @Autowired
    private TradeCenter tradeCenter;
    @Autowired
    private FlowInterceptorManager flowInterceptorManager;
    @Autowired
    private ChannelChooser channelChooser;
    @Autowired
    private RefundKeywordManager refundKeywordManager;
    
    public void intercept(final boolean isSuccess, final Trade trade) {
        if (isSuccess || trade.isDummyResult()) {
            this.finish(isSuccess, trade);
        }
        else {
            if (this.attempForceInterceptor(trade)) {
                CallbackInterceptor.logger.debug("{}/{}\u4ea4\u6613\u5931\u8d25\u62a5\u544a\u88ab\u5f3a\u5236\u62e6\u622a, \u5c06\u4ea4\u7531\u4eba\u5de5\u5904\u7406", (Object)trade.getTradeId(), (Object)trade.getRequest().getMobile());
                return;
            }
            final List<FlowCallbackInterceptor> interceptors = this.flowInterceptorManager.findEnabledCallbackInterceptors();
            for (final FlowCallbackInterceptor interceptor : interceptors) {
                if (interceptor.match(trade)) {
                    CallbackInterceptor.logger.debug("{}/{}\u4ea4\u6613\u5931\u8d25\u62a5\u544a\u88ab\u62e6\u622a, \u5c06\u5f97\u5230\u518d\u6b21\u5904\u7406\u7684\u673a\u4f1a", (Object)trade.getTradeId(), (Object)trade.getRequest().getMobile());
                    this.doNext(interceptor, trade);
                    return;
                }
            }
            this.finish(false, trade);
        }
    }
    
    private boolean attempForceInterceptor(final Trade trade) {
        if (trade.isForeceInterceptorMe()) {
            trade.clearInterceptor();
            return true;
        }
        return false;
    }
    
    private void doNext(final FlowCallbackInterceptor interceptor, final Trade trade) {
        if (interceptor.nextAuto()) {
            final RechargeRequest request = trade.getRequest();
            final ChooseRestrict restrict = this.createChooseRestrict(interceptor, trade);
            if (interceptor.getRetryTimes() == 0 || interceptor.getRetryTimes() > restrict.retryTimes()) {
                ChoosedProduct choosedProduct = null;
                try {
                    choosedProduct = this.channelChooser.choose(request, restrict);
                }
                catch (Exception ex) {}
                if (choosedProduct != null) {
                    this.tradeRetryManager.saveRetry(trade);
                    this.restart(trade, choosedProduct);
                }
                else {
                    CallbackInterceptor.logger.debug("{}/{}\u627e\u4e0d\u5230\u5408\u9002\u7684\u4e0b\u4e00\u901a\u9053, \u7ed3\u675f\u4ea4\u6613", (Object)trade.getTradeId(), (Object)trade.getRequest().getMobile());
                    this.finish(false, trade);
                }
            }
            else {
                CallbackInterceptor.logger.debug("{}/{}\u91cd\u8bd5\u6b21\u6570\u8fbe\u5230\u8bbe\u7f6e\u503c, \u7ed3\u675f\u4ea4\u6613", (Object)trade.getTradeId(), (Object)trade.getRequest().getMobile());
                this.finish(false, trade);
            }
        }
        else if (this.refundKeywordManager.match(trade.getLastMessage())) {
            CallbackInterceptor.logger.debug("{}/{}\u5931\u8d25\u4fe1\u606f\u7b26\u5408\u81ea\u52a8\u9000\u6b3e\u6761\u4ef6, \u7ed3\u675f\u4ea4\u6613", (Object)trade.getTradeId(), (Object)trade.getRequest().getMobile());
            this.finish(false, trade);
        }
    }
    
    private ChooseRestrict createChooseRestrict(final FlowCallbackInterceptor interceptor, final Trade trade) {
        final List<TradeRetry> tradeRetries = this.tradeRetryManager.findByTradeId(trade.getTradeId());
        final ChooseRestrict restrict = new ChooseRestrict();
        restrict.setPriceProtected(interceptor.isPriceProtected());
        restrict.setUserDiscount(trade.getBillDiscount());
        for (final TradeRetry tradeRetry : tradeRetries) {
            restrict.addExceptChannel(tradeRetry.getFullChannelProductGroupId());
        }
        final String groupCode = ProductIDHelper.productGroup(trade.getRequest().getExecuteProductId());
        final String fullChannelGroupId = trade.getConnection().channelId() + "-" + groupCode;
        restrict.addExceptChannel(fullChannelGroupId);
        return restrict;
    }
    
    private void restart(final Trade trade, final ChoosedProduct choosedProduct) {
        final RechargeRequest request = trade.getRequest();
        this.tradeCenter.restartTrade(trade, choosedProduct);
        CallbackInterceptor.logger.debug("{}/{}\u91cd\u542f\u4ea4\u6613\u4e8b\u52a1, \u65b0\u901a\u9053\u9009\u7528{}", new Object[] { trade.getTradeId(), request.getMobile(), choosedProduct.connection.getChannel().getName() });
    }
    
    protected void finish(final boolean isSuccess, final Trade trade) {
        this.tradeLogManager.updateOnFinish(isSuccess, trade);
        if (isSuccess) {
            this.rechargeMobilePromptManager.notify(trade);
        }
        this.userReportManager.createReport(isSuccess, trade);
        this.billingCenter.balance(isSuccess, trade);
        if (!isSuccess) {
            SpringContext.fireEvent((ApplicationEvent)new RechargeFailEvent(trade.getRequest().getMobile()));
        }
    }
    
    protected void continueFailAsSuccess(final Trade trade) {
        this.tradeLogManager.updateOnFinish(true, trade);
        this.billingCenter.payChannelAccount(trade);
        this.userReportManager.createReport(true, trade);
    }
    
    static {
        CallbackInterceptor.logger = LoggerFactory.getLogger((Class)CallbackInterceptor.class);
    }
}
