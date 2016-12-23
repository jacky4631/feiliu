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
    private static Logger logger = LoggerFactory.getLogger(RestoreMissingPendingTradeController.class);
    @Autowired
    private TradeCenter tradeCenter;
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private TradeStorageService tradeStorageService;

    @RequestMapping(value={"restore"})
    @ResponseBody
    public String load() {
        List<TradeLog> tradeLogs = this.tradeLogManager.findRunningTradeLog();
        StringBuilder sb = new StringBuilder(1024);
        sb.append("共有").append(tradeLogs.size()).append("个正在等待报告的交易, 需要恢复的如下:");
        logger.info("共有{}个正在等待报告的交易, 需要恢复的如下");
        tradeLogs.stream().filter(tradeLog -> this.tradeCenter.fetchTrade(tradeLog.getId()) == null).forEach(tradeLog -> {
                    PendingTrade pendingTrade = new PendingTrade();
                    pendingTrade.setStartTime(tradeLog.getStartDate());
                    pendingTrade.setTradeId(tradeLog.getId());
                    pendingTrade.setRequestNo(tradeLog.getRequestNo());
                    RechargeRequest request = new RechargeRequest();
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
                    pendingTrade.setBillDiscount(Double.valueOf(tradeLog.getBillDiscount()));
                    pendingTrade.setBillAmount(Double.valueOf(tradeLog.getBillAmount()));
                    this.tradeStorageService.store(pendingTrade);
                    logger.info("恢复订单{}为等待报告状态", (Object)tradeLog.getId());
                    sb.append("恢复订单").append(pendingTrade.getTradeId()).append("为等待报告状态");
                }
        );
        return sb.toString();
    }
}
