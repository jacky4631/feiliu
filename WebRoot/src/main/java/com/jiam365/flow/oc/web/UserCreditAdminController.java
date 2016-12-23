// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.oc.utils.EventUtils;
import com.jiam365.flow.base.web.WebResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.server.entity.FundAccount;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/credit" })
public class UserCreditAdminController
{
    @Autowired
    private UserManager userManager;
    
    @RequestMapping(value = { "update/{id}" }, method = { RequestMethod.GET })
    public String credit(@PathVariable("id") final Long uid, final Model model) {
        model.addAttribute("uid", (Object)uid);
        final FundAccount fundAccount = this.userManager.getCredit(uid);
        model.addAttribute("credit", (Object)((fundAccount == null) ? 0.0 : fundAccount.getCreditLine()));
        return "oc/credit-setting";
    }
    
    @RequestMapping(value = { "update" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse saveCredit(final Long uid, final double credit) {
        this.userManager.updatecreditLine(uid, credit);
        EventUtils.publishLogEvent("设置用户" + uid + "授信为" + credit);
        return WebResponse.success("执行成功");
    }
}
