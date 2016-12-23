// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.server.channel.ChannelConnection;
import com.jiam365.flow.sdk.MobileInfo;
import com.jiam365.flow.server.engine.Trade;
import com.jiam365.modules.telco.Telco;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.base.utils.ShiroUtils;
import com.jiam365.flow.base.web.WebResponse;
import com.jiam365.flow.server.product.FlowPackage;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import com.jiam365.flow.server.engine.TradeCenter;
import com.jiam365.flow.server.channel.ChannelConnectionManager;
import com.jiam365.flow.server.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.FlowPackageManager;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/channel" })
public class ChannelTestController
{
    private static Logger logger;
    @Autowired
    private FlowPackageManager flowPackageManager;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private ChannelConnectionManager channelConnectionManager;
    @Autowired
    private TradeCenter tradeCenter;
    
    @RequestMapping({ "test/{channelId}" })
    public String channelTest(@PathVariable("channelId") final long channelId, final Model model) {
        final List<FlowPackage> packages = this.flowPackageManager.findByChannelId(channelId);
        model.addAttribute("packages", (Object)packages);
        return "oc/channel-test";
    }
    
    @RequestMapping(value = { "test" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse doTest(final long channelId, final String flowPackageId, final String mobile) {
        try {
            final FlowPackage flowPackage = this.flowPackageManager.getFlowPackage(flowPackageId);
            final String username = ShiroUtils.currentUsername();
            final RechargeRequest request = new RechargeRequest(username, mobile, flowPackage.getProductId(), "");
            final MobileInfo info = this.mobileService.mobileInfo(mobile);
            if (info.getProvider() == null) {
                info.setProvider(Telco.CMCC);
            }
            request.setMobileInfo(info);
            request.setPrice(flowPackage.getPrice());
            request.setExecuteProductPrice(flowPackage.getPrice());
            request.setSize(flowPackage.getSize());
            request.setProvider(info.getProvider());
            request.setProductName(flowPackage.getTitle());
            request.setOrigiProductId(flowPackage.getOrigiProductId());
            request.setExecuteProductId(flowPackage.getProductId());
            request.setOrigiDiscount(flowPackage.getDiscount());
            final ChannelConnection connection = this.channelConnectionManager.getConnection(channelId);
            final Trade trade = new Trade(this.tradeCenter, connection, request);
            this.tradeCenter.beginTrade(trade);
            ChannelTestController.logger.debug("为用户{}向{}充值创建测试交易, 交易流水号为{}", new Object[] { request.getUsername(), request.getMobile(), trade.getTradeId() });
            return WebResponse.success("测试创建成功, 交易流水号" + trade.getTradeId());
        }
        catch (Exception e) {
            ChannelTestController.logger.error("测试时出错", (Throwable)e);
            return WebResponse.fail("测试创建失败, " + e.getMessage());
        }
    }
    
    static {
        ChannelTestController.logger = LoggerFactory.getLogger((Class)ChannelTestController.class);
    }
}
