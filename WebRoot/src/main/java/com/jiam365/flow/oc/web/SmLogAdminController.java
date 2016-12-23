// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.flow.server.entity.SmLog;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.server.entity.User;
import com.jiam365.flow.oc.utils.EventUtils;
import com.jiam365.modules.utils.SimpleDateUtils;
import com.jiam365.flow.base.utils.ShiroUtils;
import com.jiam365.flow.base.web.WebResponse;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.SmLogManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/smlog" })
public class SmLogAdminController
{
    @Autowired
    private SmLogManager smLogManager;
    @Autowired
    private UserManager userManager;
    
    @RequestMapping
    public String list() {
        return "oc/smlog-list";
    }
    
    @RequestMapping({ "fee-dialog" })
    public String dialogFee() {
        return "oc/smlog-fee";
    }
    
    @RequestMapping(value = { "pay" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse payFee(final String username) {
        final User user = this.userManager.loadUserByUsername(username);
        if (user == null) {
            return WebResponse.fail("用户名输入错误, 找不到用户" + username);
        }
        final String operator = ShiroUtils.currentUsername();
        final double amount = this.smLogManager.pay(operator, username, SimpleDateUtils.getTodayPeriod()[0]);
        if (amount > 0.0) {
            EventUtils.publishLogEvent("扣除" + username + "的短信费用" + amount + "元");
            return WebResponse.success("成功扣除" + username + "的短信费用" + amount + "元");
        }
        return WebResponse.fail("扣除" + username + "短信费用" + amount + "元, 请确认有短信消费, 并且账户余额充足");
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<SmLog> listData(final HttpServletRequest request) {
        final PageParamLoader<SmLog> pageParamLoader = (PageParamLoader<SmLog>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<SmLog> page = (Page<SmLog>)pageParamLoader.parse();
        page = this.smLogManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<SmLog>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
}
