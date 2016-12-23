// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.slf4j.LoggerFactory;
import com.jiam365.flow.server.product.FlowPackage;
import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.flow.server.channel.ChannelConnection;
import com.jiam365.flow.oc.utils.EventUtils;
import com.jiam365.flow.server.dic.State;
import org.springframework.ui.Model;
import java.util.Iterator;
import java.util.List;
import com.jiam365.flow.server.entity.TradeLog;
import com.jiam365.modules.telco.Telco;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jiam365.flow.server.dto.ChoosedProduct;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.server.engine.Trade;
import com.jiam365.flow.server.engine.TradeException;
import com.jiam365.flow.server.engine.ChooseRestrict;
import com.jiam365.flow.base.web.WebResponse;
import com.jiam365.flow.server.service.TradeRetryManager;
import com.jiam365.flow.server.engine.ChannelChooser;
import com.jiam365.flow.server.service.FlowPackageManager;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import com.jiam365.flow.server.channel.ChannelConnectionManager;
import com.jiam365.flow.server.usercallback.UserReportManager;
import com.jiam365.flow.server.service.MobileService;
import com.jiam365.flow.server.service.TradeLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.engine.TradeCenter;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class TradeController
{
    private static final Logger logger;
    @Autowired
    private TradeCenter tradeCenter;
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private UserReportManager userReportManager;
    @Autowired
    private ChannelConnectionManager channelConnectionManager;
    @Autowired
    private ChannelAdminManager channelAdminManager;
    @Autowired
    private FlowPackageManager flowPackageManager;
    @Autowired
    private ChannelChooser channelChooser;
    @Autowired
    private TradeRetryManager tradeRetryManager;
    
    @RequestMapping({ "/oc/trade/auto" })
    @ResponseBody
    public WebResponse autoRoute(final String[] ids) {
        int count = 0;
        for (final String id : ids) {
            final Trade trade = this.loadTrade(id);
            if (trade != null && !trade.isRecharging()) {
                final RechargeRequest request = trade.getRequest();
                try {
                    final ChoosedProduct choosedProduct = this.channelChooser.choose(request, new ChooseRestrict[0]);
                    this.tradeCenter.restartTrade(trade, choosedProduct);
                    ++count;
                }
                catch (TradeException e) {
                    TradeController.logger.warn("自动继续执行订单{}时错误, {}", (Object)id, (Object)e.getMessage());
                }
            }
        }
        return WebResponse.success("为" + count + "个订单重选了通道, 剩余" + (ids.length - count) + "无合适通道执行");
    }
    
    @RequestMapping({ "/oc/trade/auto-by-condition" })
    @ResponseBody
    public WebResponse autoRouteByCondition(final String provider, final String stateCode) {
        final Telco telco = StringUtils.isNotBlank((CharSequence)provider) ? Telco.valueOf(provider) : null;
        final List<TradeLog> tradeLogs = this.tradeLogManager.findPendingTradeLog(telco, stateCode);
        int count = 0;
        for (final TradeLog tradeLog : tradeLogs) {
            final Trade trade = this.loadTrade(tradeLog.getId());
            if (trade != null && !trade.isRecharging()) {
                final RechargeRequest request = trade.getRequest();
                try {
                    final ChoosedProduct choosedProduct = this.channelChooser.choose(request, new ChooseRestrict[0]);
                    this.tradeCenter.restartTrade(trade, choosedProduct);
                    ++count;
                }
                catch (TradeException e) {
                    TradeController.logger.warn("自动继续执行订单{}时错误, {}", (Object)tradeLog.getId(), (Object)e.getMessage());
                }
            }
        }
        return WebResponse.success("为" + count + "个订单重选了通道, 剩余" + (tradeLogs.size() - count) + "无合适通道执行");
    }
    
    @RequestMapping({ "/oc/trade/autosel-dlg" })
    public String autoSelDlg(final Model model) {
        final List<State> states = this.mobileService.findAllStates();
        model.addAttribute("states", (Object)states);
        return "/oc/tradelog-autosel";
    }
    
    @RequestMapping({ "/oc/trade/reroute" })
    @ResponseBody
    public WebResponse reRoute(final String[] ids, final long channelId, final boolean naFirst, final boolean priceProtected) {
        final ChannelConnection connection = this.channelConnectionManager.pickOnlineChannleConnection(channelId);
        if (connection == null) {
            return WebResponse.fail("新指定的通道不在线, 请重新指定");
        }
        int count = 0;
        try {
            for (final String id : ids) {
                final Trade trade = this.loadTrade(id);
                if (trade != null && !trade.isRecharging()) {
                    final String productId = trade.getRequest().getProductId();
                    final String stateCode = trade.getRequest().getMobileInfo().getStateCode();
                    if (this.doRestartTrade(connection, trade, productId, stateCode, naFirst, priceProtected)) {
                        ++count;
                        EventUtils.publishLogEvent("重新选择通道" + connection.getChannel().getName() + "执行订单" + trade.getTradeId());
                    }
                }
            }
            final int failCount = ids.length - count;
            if (failCount > 0) {
                return WebResponse.success("为" + count + "个订单重选了通道, 剩余" + failCount + "个不符合重选条件,继续在原通道尝试");
            }
            return WebResponse.success("为全部" + count + "个订单重选了通道");
        }
        catch (Exception e) {
            TradeController.logger.error("重选通道时出现错误, {}", (Object)e.getMessage(), (Object)e);
            return WebResponse.fail("执行失败,请报告管理员");
        }
    }
    
    @RequestMapping({ "/oc/trade/retry" })
    @ResponseBody
    public WebResponse retry(final String[] ids) {
        int count = 0;
        try {
            for (final String id : ids) {
                final Trade trade = this.loadTrade(id);
                if (trade != null && !trade.isRecharging()) {
                    final String productId = trade.getRequest().getProductId();
                    final String stateCode = trade.getRequest().getMobileInfo().getStateCode();
                    final TradeLog log = this.tradeLogManager.get(id);
                    final long channelId = log.getChannelId();
                    final ChannelConnection connection = this.channelConnectionManager.pickOnlineChannleConnection(channelId);
                    if (connection != null) {
                        if (this.doRestartTrade(connection, trade, productId, stateCode, false, false)) {
                            EventUtils.publishLogEvent("在通道" + connection.getChannel().getName() + "重试订单" + trade.getTradeId());
                            ++count;
                        }
                    }
                }
            }
            final int failCount = ids.length - count;
            if (failCount > 0) {
                return WebResponse.success("为" + count + "个订单开始重试, 剩余" + failCount + "个不符合条件, 无法重试");
            }
            return WebResponse.success("为全部" + count + "个订单提交重试");
        }
        catch (Exception e) {
            TradeController.logger.error("提交重试时出现错误, {}, {}", (Object)e.getMessage(), (Object)e);
            return WebResponse.fail("执行失败,请报告管理员");
        }
    }
    
    private boolean doRestartTrade(final ChannelConnection connection, final Trade trade, final String productId, final String stateCode, final boolean naFirst, final boolean priceProtected) {
        boolean isSuc = false;
        final FlowPackage flowPackage = this.flowPackageManager.selectFlowPackage(connection.channelId(), productId, stateCode, naFirst);
        if (flowPackage != null && (!priceProtected || trade.getBillAmount() >= DoubleUtils.mul(flowPackage.getPrice(), flowPackage.getDiscount()))) {
            this.tradeRetryManager.saveRetry(trade);
            this.tradeCenter.restartTrade(trade, new ChoosedProduct(connection, flowPackage));
            isSuc = true;
        }
        else {
            TradeController.logger.warn("为{}重新指定通道时, 找不到目标通道中{}的包{}", new Object[] { trade.getTradeId(), connection.channelId(), trade.getRequest().getProductId() });
        }
        return isSuc;
    }
    
    private Trade loadTrade(final String tradeId) {
        Trade trade = this.tradeCenter.fetchTrade(tradeId);
        if (trade == null) {
            final TradeLog log = this.tradeLogManager.get(tradeId);
            trade = Trade.restoreTrade(this.tradeCenter, this.mobileService, log);
            if (trade != null && log.getChannelId() != null) {
                final ChannelConnection connection = this.channelConnectionManager.getConnection(log.getChannelId());
                if (connection != null) {
                    trade.setConnection(connection);
                }
            }
        }
        return trade;
    }
    
    @RequestMapping({ "/oc/trade/selchannel" })
    public String selectChannel(final Model model) {
        model.addAttribute("channels", (Object)this.channelAdminManager.findEnabledChannels());
        return "oc/channel-sel";
    }
    
    @RequestMapping({ "/oc/trade/fail-pass" })
    @ResponseBody
    public WebResponse continueFail(final String[] ids) {
        try {
            int count = 0;
            for (final String id : ids) {
                final Trade trade = this.loadTrade(id);
                if (trade != null) {
                    this.tradeCenter.continueFail(trade);
                    ++count;
                }
            }
            return WebResponse.success("执行成功, 已经处理" + count + "个订单");
        }
        catch (Exception e) {
            TradeController.logger.error("主动回调执行时出错, {}", (Object)e.getMessage(), (Object)e);
            return WebResponse.fail("执行失败,请报告管理员");
        }
    }
    
    @RequestMapping({ "/oc/trade/ok" })
    @ResponseBody
    public WebResponse continueFailAsSuccess(final String[] ids) {
        try {
            int count = 0;
            for (final String id : ids) {
                final Trade trade = this.loadTrade(id);
                if (trade != null) {
                    this.tradeCenter.continueFailAsSuccess(trade);
                    ++count;
                }
            }
            return WebResponse.success("已经确认" + count + "个订单为上游成功");
        }
        catch (Exception e) {
            TradeController.logger.error("确认为上游成功失败, {}", (Object)e.getMessage(), (Object)e);
            return WebResponse.fail("执行失败,请报告管理员");
        }
    }
    
    @RequestMapping({ "/oc/trade/callback" })
    @ResponseBody
    public WebResponse foreceResult(final String[] ids, final boolean success) {
        try {
            for (final String id : ids) {
                this.tradeCenter.onResult(id, success);
            }
            return WebResponse.success("执行成功");
        }
        catch (Exception e) {
            TradeController.logger.error("主动回调执行时出错, {}", (Object)e.getMessage(), (Object)e);
            return WebResponse.fail("执行失败,请报告管理员");
        }
    }
    
    @RequestMapping({ "/oc/trade/report-2" })
    @ResponseBody
    public WebResponse recreateReport(final String[] ids) {
        try {
            int count = 0;
            for (final String id : ids) {
                final TradeLog log = this.tradeLogManager.get(id);
                if (log != null && log.getResult() != 9) {
                    this.userReportManager.createReport(log);
                    ++count;
                }
            }
            return WebResponse.success("执行成功, 重新生成" + count + "份用户报告, 即将开始发送");
        }
        catch (Exception e) {
            TradeController.logger.error("重新生成用户报告出错, {}", (Object)e.getMessage(), (Object)e);
            return WebResponse.fail("重新生成用户报告出错, 请报告管理员");
        }
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)TradeController.class);
    }
}
