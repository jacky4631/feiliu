// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.oc.utils.EventUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jiam365.flow.server.entity.SmConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.params.SmConfigManager;
import org.springframework.stereotype.Controller;

@Controller
public class SmConfigController
{
    @Autowired
    private SmConfigManager smConfigManager;
    
    @RequestMapping({ "/oc/sm-config" })
    public String smConfig(final Model model) {
        model.addAttribute("smConfig", (Object)this.smConfigManager.loadSmConfig());
        return "oc/sm-config";
    }
    
    @RequestMapping(value = { "/oc/sm-config" }, method = { RequestMethod.POST })
    public String smConfig(final SmConfig config, final RedirectAttributes redirectAttributes) {
        this.smConfigManager.saveSmConfig(config);
        EventUtils.publishLogEvent("更新短信全局配置, 状态" + (config.isSendSm() ? "开启" : "关闭") + " 关闭不发短信的运营商代码:" + config.getDisabledForTelco());
        redirectAttributes.addFlashAttribute("message", (Object)"保存成功!");
        return "redirect:/oc/sm-config";
    }
}
