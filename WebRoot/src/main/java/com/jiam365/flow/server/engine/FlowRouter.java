// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import org.slf4j.LoggerFactory;
import java.util.Arrays;
import com.jiam365.flow.server.dto.ChoosedProduct;
import java.util.Iterator;
import com.jiam365.flow.server.utils.RechargeRequests;
import com.jiam365.flow.server.channel.ChannelConnection;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.server.rest.RechareApplication;
import com.jiam365.flow.server.engine.pretreatment.Pretreatment;
import java.util.List;
import org.slf4j.Logger;

public class FlowRouter
{
    private static Logger logger;
    private TradeCenter tradeCenter;
    private ChannelChooser channelChooser;
    private RechargeInterceptor rechargeInterceptor;
    private List<Pretreatment> pretreatments;
    
    public String route(final RechareApplication application) {
        final RechargeRequest request = new RechargeRequest(application.username(), application.getMobile(), application.getProductId(), application.getUserReqNo());
        for (final Pretreatment pretreatment : this.pretreatments) {
            pretreatment.check(request);
        }
        Trade trade;
        if (this.rechargeInterceptor.intercept(request)) {
            trade = new Trade(this.tradeCenter, null, request);
        }
        else {
            final ChoosedProduct choosedProduct = this.channelChooser.choose(request, new ChooseRestrict[0]);
            RechargeRequests.bindChannelPackage(request, choosedProduct);
            trade = new Trade(this.tradeCenter, choosedProduct.connection, request);
        }
        return this.start(trade);
    }
    
    private String start(final Trade trade) {
        final RechargeRequest request = trade.getRequest();
        this.tradeCenter.beginTrade(trade);
        FlowRouter.logger.debug("\u4e3a\u7528\u6237{}\u5411{}\u5145\u503c\u521b\u5efa\u4ea4\u6613\u4e8b\u52a1, \u4ea4\u6613\u6d41\u6c34\u53f7\u4e3a{}", new Object[] { request.getUsername(), request.getMobile(), trade.getTradeId() });
        return trade.getTradeId();
    }
    
    public void setTradeCenter(final TradeCenter tradeCenter) {
        this.tradeCenter = tradeCenter;
    }
    
    public void setChannelChooser(final ChannelChooser channelChooser) {
        this.channelChooser = channelChooser;
    }
    
    public void setRechargeInterceptor(final RechargeInterceptor rechargeInterceptor) {
        this.rechargeInterceptor = rechargeInterceptor;
    }
    
    public void setPretreatments(final Pretreatment... pretreatments) {
        this.pretreatments = Arrays.asList(pretreatments);
    }
    
    static {
        FlowRouter.logger = LoggerFactory.getLogger((Class)FlowRouter.class);
    }
}
