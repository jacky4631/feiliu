// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.oc.utils.EventUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jiam365.flow.server.entity.FlowInterceptor;
import org.springframework.ui.Model;
import com.jiam365.flow.server.service.UserManager;
import com.jiam365.flow.server.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.FlowInterceptorManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/interceptors" })
public class FlowInterceptorAdminController
{
    @Autowired
    private FlowInterceptorManager flowInterceptorManager;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private UserManager userManager;
    
    @RequestMapping({ "edit" })
    public String createForm(final Model model) {
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        model.addAttribute("users", (Object)this.userManager.findAll());
        model.addAttribute("interceptor", (Object)this.flowInterceptorManager.get());
        return "oc/interceptor-form";
    }
    
    @RequestMapping(value = { "save" }, method = { RequestMethod.POST })
    public String update(final FlowInterceptor interceptor, final RedirectAttributes redirectAttributes) {
        try {
            this.flowInterceptorManager.save(interceptor);
            EventUtils.publishLogEvent("更新充值拦截器" + interceptor.description());
            redirectAttributes.addFlashAttribute("message", (Object)"修改成功, 请及时处理被拦截的充值请求, 并在不需要的时候放开拦截");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", (Object)"修改失败");
        }
        return "redirect:/oc/interceptors/edit";
    }
}
