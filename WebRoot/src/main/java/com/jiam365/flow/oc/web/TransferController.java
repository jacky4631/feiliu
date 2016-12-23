// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import com.jiam365.flow.server.channel.FlowChannel;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.base.utils.ShiroUtils;
import com.jiam365.flow.base.web.WebResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.server.entity.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import com.jiam365.flow.server.service.UserManager;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import com.jiam365.flow.server.service.TransferLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.engine.BillingCenter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc" })
public class TransferController
{
    @Autowired
    private BillingCenter billingCenter;
    @Autowired
    private TransferLogManager transferLogManager;
    @Autowired
    private ChannelAdminManager channelAdminManager;
    @Autowired
    private UserManager userManager;
    
    @RequestMapping(value = { "transfer/{id}" }, method = { RequestMethod.GET })
    public String transfer(@PathVariable("id") final Long uid, final Model model) {
        final User user = this.userManager.getUser(uid);
        model.addAttribute("user", (Object)user);
        return "oc/balance-setting";
    }
    
    @RequestMapping(value = { "transfer" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse transfer(final String username, final Double amount, final int accountingSubject, final String remark) {
        final String operator = ShiroUtils.currentUsername();
        try {
            if (username != null && amount != null) {
                final double balance = this.billingCenter.transfer(username, amount);
                final User user = this.userManager.getUserByUsername(username);
                final String displayName = (user == null) ? "" : user.getDisplayName();
                this.transferLogManager.saveLog(operator, accountingSubject, username, displayName, amount, balance, remark, new int[0]);
                return WebResponse.success("资金转入" + amount + "元成功, 可在用户列表查看余额");
            }
            return WebResponse.fail("输入错误或信息不完整, 不能转入资金");
        }
        catch (Exception e) {
            return WebResponse.fail(e.getMessage());
        }
    }
    
    @RequestMapping(value = { "transfer2channel/{id}" }, method = { RequestMethod.GET })
    public String transfer2Channel(@PathVariable("id") final Long channelId, final Model model) {
        final FlowChannel channel = this.channelAdminManager.get(channelId);
        model.addAttribute("channel", (Object)channel);
        return "oc/cbalance-setting";
    }
    
    @RequestMapping(value = { "transfer2channel" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse transfer2Channel(final Long channelId, final Double amount, final String remark) {
        final String operator = ShiroUtils.currentUsername();
        try {
            if (channelId != null && amount != null) {
                final FlowChannel channel = this.channelAdminManager.get(channelId);
                final double balance = this.billingCenter.transfer2Channel(channelId, amount);
                this.transferLogManager.saveLog(operator, 9, String.valueOf(channel.getId()), channel.getName(), amount, balance, remark, 1);
                return WebResponse.success("资金加入通道" + amount + "元成功, 可在通道列表查看余额");
            }
            return WebResponse.fail("输入错误或信息不完整, 不能加入资金到通道");
        }
        catch (Exception e) {
            return WebResponse.fail(e.getMessage());
        }
    }
}
