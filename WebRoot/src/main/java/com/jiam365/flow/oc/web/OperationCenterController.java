// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import com.jiam365.flow.server.entity.User;
import java.util.Iterator;
import java.util.ArrayList;
import com.jiam365.flow.server.dto.TradeCountByChannelName;
import java.util.Date;
import com.jiam365.modules.utils.SimpleDateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import com.jiam365.flow.oc.dto.UserBillInfo;
import com.jiam365.flow.server.dao.TradeLogDao;
import java.util.List;
import com.jiam365.modules.telco.Telco;
import java.util.Map;
import com.jiam365.flow.oc.dto.OCDashboard;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.SecurityUtils;
import com.jiam365.flow.server.service.UserManager;
import com.jiam365.flow.server.channel.ChannelConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.statis.StatisticsManager;
import org.springframework.stereotype.Controller;

@Controller
public class OperationCenterController
{
    @Autowired
    private StatisticsManager statisticsManager;
    @Autowired
    private ChannelConnectionManager channelConnectionManager;
    @Autowired
    private UserManager userManager;
    
    @RequestMapping({ "/oc" })
    private String index() {
        final Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.hasRole("admin") || currentUser.hasRole("financial")) {
            return "redirect:/oc/dashboard";
        }
        if (currentUser.hasRole("operator")) {
            return "redirect:/oc/tradelog";
        }
        return "/error/401";
    }
    
    @RequiresRoles(value = { "financial", "admin" }, logical = Logical.OR)
    @RequestMapping({ "/oc/dashboard" })
    public String index(final Model model) {
        final Map<Telco, Double> billAmountMap = this.statisticsManager.totayBillAmountByProvider(null);
        final OCDashboard dashboard = new OCDashboard();
        dashboard.setBillAmountMap(billAmountMap);
        dashboard.setSuccessTotal(this.statisticsManager.todayTradeCount(null, 0));
        dashboard.setFailTotal(this.statisticsManager.todayTradeCount(null, -1));
        dashboard.setPendingTotal(this.statisticsManager.todayTradeCount(null, 9));
        final List<TradeLogDao.TradeCountByChannel> tradeCountByChannelList = this.statisticsManager.todayTradeCountByChannel();
        dashboard.setTradeCountByChannels(this.fillChannelNameAndSuccessCount(tradeCountByChannelList));
        final List<TradeLogDao.TradeCountByScope> notFinishTradeCountByScope = this.statisticsManager.todayNotFinishTradeCountByScope();
        dashboard.setNotFinishTradeCountByScope(notFinishTradeCountByScope);
        dashboard.setCostAmount(this.statisticsManager.todayCostAmount());
        dashboard.setProfits(this.statisticsManager.getRecentDaysProfit(7));
        model.addAttribute("dashboard", (Object)dashboard);
        final Map<String, Double> billAmountMapToday = this.statisticsManager.totayBillAmountGroupByUsername();
        final List<UserBillInfo> userBillInfoList = this.convert2UserBillAmountList(billAmountMapToday);
        model.addAttribute("userBillInfoList", (Object)userBillInfoList);
        return "/oc/index";
    }
    
    @RequestMapping({ "/oc/index/billInfo/{type}" })
    public String billAmount(final Model model, @PathVariable("type") final int type) {
        Date[] period = null;
        switch (type) {
            case 1: {
                period = SimpleDateUtils.getWeekPeriod();
                break;
            }
            case 2: {
                period = SimpleDateUtils.getMonthPeriod();
                break;
            }
            default: {
                period = SimpleDateUtils.getTodayPeriod();
                break;
            }
        }
        final Map<String, Double> billAmountMapToday = this.statisticsManager.billAmountGroupByUsername(period[0], period[1]);
        final List<UserBillInfo> userBillInfoList = this.convert2UserBillAmountList(billAmountMapToday);
        model.addAttribute("userBillInfoList", (Object)userBillInfoList);
        return "/oc/index-bill-info";
    }
    
    private List<TradeCountByChannelName> fillChannelNameAndSuccessCount(final List<TradeLogDao.TradeCountByChannel> tradeCountByChannels) {
        final List<TradeCountByChannelName> tradeCountByChannelNames = new ArrayList<TradeCountByChannelName>();
        for (final TradeLogDao.TradeCountByChannel tradeCount : tradeCountByChannels) {
            final TradeCountByChannelName tradeCountByChannelName = new TradeCountByChannelName();
            tradeCountByChannelName.setCount(tradeCount.count);
            try {
                tradeCountByChannelName.setChannelName(this.channelConnectionManager.getConnection(tradeCount.channelId).getChannel().getName());
            }
            catch (NullPointerException e) {
                tradeCountByChannelName.setChannelName("ID-" + tradeCount.channelId);
            }
            tradeCountByChannelName.setFinishCount(this.statisticsManager.todayFinsishTradeCountByChannel(tradeCount.channelId));
            tradeCountByChannelNames.add(tradeCountByChannelName);
        }
        return tradeCountByChannelNames;
    }
    
    private List<UserBillInfo> convert2UserBillAmountList(final Map<String, Double> billAmountMap) {
        final List<UserBillInfo> userBillInfos = new ArrayList<UserBillInfo>();
        for (final String username : billAmountMap.keySet()) {
            final User user = this.userManager.getUserByUsername(username);
            if (user != null) {
                final UserBillInfo userBillInfo = new UserBillInfo(username, user.getDisplayName(), billAmountMap.get(username));
                userBillInfo.setCompany(user.getCompany());
                userBillInfo.setLinkman(user.getLinkman());
                userBillInfo.setBalance(this.userManager.getBalance(username));
                userBillInfos.add(userBillInfo);
            }
        }
        return userBillInfos;
    }
}
