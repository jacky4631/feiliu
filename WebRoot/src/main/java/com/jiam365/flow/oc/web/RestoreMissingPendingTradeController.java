// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.server.entity.TradeLog;
import java.util.List;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.server.entity.PendingTrade;
import com.jiam365.flow.server.service.TradeStorageService;
import com.jiam365.flow.server.service.MobileService;
import com.jiam365.flow.server.service.TradeLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.engine.TradeCenter;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/manager/missingTrade" })
public class RestoreMissingPendingTradeController
{
    private static Logger logger;
    @Autowired
    private TradeCenter tradeCenter;
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private TradeStorageService tradeStorageService;
    
    @RequestMapping({ "restore" })
    @ResponseBody
    public String load() {
        final List<TradeLog> tradeLogs = this.tradeLogManager.findRunningTradeLog();
        final StringBuilder sb = new StringBuilder(1024);
        sb.append("\u5171\u6709").append(tradeLogs.size()).append("\u4e2a\u6b63\u5728\u7b49\u5f85\u62a5\u544a\u7684\u4ea4\u6613, \u9700\u8981\u6062\u590d\u7684\u5982\u4e0b:");
        RestoreMissingPendingTradeController.logger.info("\u5171\u6709{}\u4e2a\u6b63\u5728\u7b49\u5f85\u62a5\u544a\u7684\u4ea4\u6613, \u9700\u8981\u6062\u590d\u7684\u5982\u4e0b");
        final PendingTrade pendingTrade;
        final RechargeRequest request;
        final StringBuilder sb2;
        tradeLogs.stream().filter(tradeLog -> this.tradeCenter.fetchTrade(tradeLog.getId()) == null).forEach(tradeLog -> {
            pendingTrade = new PendingTrade();
            pendingTrade.setStartTime(tradeLog.getStartDate());
            pendingTrade.setTradeId(tradeLog.getId());
            pendingTrade.setRequestNo(tradeLog.getRequestNo());
            request = new RechargeRequest();
            request.setUsername(tradeLog.getUsername());
            request.setBillDiscount(tradeLog.getBillDiscount());
            request.setExecuteProductId(tradeLog.getExecuteProductId());
            request.setMobile(tradeLog.getMobile());
            request.setMobileInfo(this.mobileService.mobileInfo(tradeLog.getMobile()));
            request.setOrigiDiscount(tradeLog.getCostDiscount());
            request.setOrigiProductId(tradeLog.getOrigiProductId());
            request.setPrice(tradeLog.getPrice());
            request.setExecuteProductPrice(tradeLog.getExecuteProductPrice());
            request.setProductId(tradeLog.getProductId());
            request.setProductName(tradeLog.getProductName());
            request.setProvider(request.getMobileInfo().getProvider());
            request.setSize(tradeLog.getSize());
            request.setSubmitTime(1466666666666L);
            request.setUserReqNo(tradeLog.getUserRequestNo());
            pendingTrade.setRechargeRequest(request);
            pendingTrade.setChannelId(tradeLog.getChannelId());
            pendingTrade.setProvider(request.getProvider());
            pendingTrade.setStatus(tradeLog.getChannelResult());
            pendingTrade.setStatus(4);
            pendingTrade.setLastMessage(tradeLog.getChannelMessage());
            pendingTrade.setBillDiscount(tradeLog.getBillDiscount());
            pendingTrade.setBillAmount(tradeLog.getBillAmount());
            this.tradeStorageService.store(pendingTrade);
            RestoreMissingPendingTradeController.logger.info("\u6062\u590d\u8ba2\u5355{}\u4e3a\u7b49\u5f85\u62a5\u544a\u72b6\u6001", (Object)tradeLog.getId());
            sb2.append("\u6062\u590d\u8ba2\u5355").append(pendingTrade.getTradeId()).append("\u4e3a\u7b49\u5f85\u62a5\u544a\u72b6\u6001");
            return;
        });
        return sb.toString();
    }
    
    static {
        RestoreMissingPendingTradeController.logger = LoggerFactory.getLogger((Class)RestoreMissingPendingTradeController.class);
    }
}
