// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.agent.web;

import org.springframework.web.bind.annotation.RequestMapping;
import com.jiam365.modules.telco.Telco;
import java.util.Map;
import com.jiam365.flow.agent.dto.TDashboard;
import org.apache.shiro.SecurityUtils;
import com.jiam365.flow.base.service.account.ShiroDbRealm;
import org.springframework.ui.Model;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.statis.StatisticsManager;
import org.springframework.stereotype.Controller;

@Controller
public class DashboardController
{
    @Autowired
    private StatisticsManager statisticsManager;
    @Autowired
    private UserManager userManager;
    
    @RequestMapping({ "/agent" })
    public String index(final Model model) {
        final ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        final String username = user.username;
        final Map<Telco, Double> billAmountMap = this.statisticsManager.totayBillAmountByProvider(username);
        final TDashboard TDashboard = new TDashboard();
        TDashboard.setBillAmountMap(billAmountMap);
        TDashboard.setBalance(this.userManager.getBalance(username));
        TDashboard.setSuccessTotal(this.statisticsManager.todayTradeCount(username, 0));
        TDashboard.setFailTotal(this.statisticsManager.todayTradeCount(username, -1));
        TDashboard.setPendingTotal(this.statisticsManager.todayTradeCount(username, 9));
        model.addAttribute("dashboard", (Object)TDashboard);
        return "agent/index";
    }
}
