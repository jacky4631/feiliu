// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.agent.web;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jiam365.flow.server.entity.SmTemplate;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.shiro.SecurityUtils;
import com.jiam365.flow.base.service.account.ShiroDbRealm;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/agent" })
public class SmTemplateController
{
    @Autowired
    private UserManager userManager;
    
    @RequestMapping(value = { "sm-template" }, method = { RequestMethod.GET })
    public String smsTemplate(final Model model) {
        final ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("template", (Object)this.userManager.getSmsTemplate(user.id));
        return "agent/sm-template";
    }
    
    @RequestMapping(value = { "sms-template" }, method = { RequestMethod.POST })
    public String smsTemplate(final SmTemplate template, final RedirectAttributes redirectAttributes) {
        final ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        template.setUserId(user.id);
        this.userManager.saveSmsTemplate(template);
        redirectAttributes.addFlashAttribute("message", (Object)"\u4fdd\u5b58\u6210\u529f!");
        return "redirect:/agent/sm-template";
    }
}
