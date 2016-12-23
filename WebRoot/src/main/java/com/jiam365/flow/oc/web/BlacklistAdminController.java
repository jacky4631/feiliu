// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.oc.utils.EventUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jiam365.flow.server.entity.Blacklist;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.BlacklistManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/blacklist" })
public class BlacklistAdminController
{
    @Autowired
    private BlacklistManager blacklistManager;
    
    @RequestMapping({ "" })
    public String createForm(final Model model) {
        model.addAttribute("blacklist", this.blacklistManager.load());
        return "oc/blacklist-form";
    }
    
    @RequestMapping(value = { "save" }, method = { RequestMethod.POST })
    public String update(final Blacklist blacklist, final RedirectAttributes redirectAttributes) {
        try {
            this.blacklistManager.save(blacklist);
            EventUtils.publishLogEvent("更新黑名单");
            redirectAttributes.addFlashAttribute("message", "更新成功");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "更新失败");
        }
        return "redirect:/oc/blacklist";
    }
}
