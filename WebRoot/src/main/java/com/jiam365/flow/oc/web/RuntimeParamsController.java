// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import com.jiam365.flow.server.params.ProductSelectionParam;
import com.jiam365.flow.server.params.UserReportParam;
import com.jiam365.flow.server.params.FunctionLimitParam;
import com.jiam365.flow.oc.utils.EventUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jiam365.flow.server.params.TimeoutParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import com.jiam365.flow.server.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.params.ParamsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc" })
public class RuntimeParamsController
{
    @Autowired
    private ParamsService paramsService;
    @Autowired
    private MobileService mobileService;
    
    @RequestMapping(value = { "params" }, method = { RequestMethod.GET })
    public String paramsForm(final Model model) {
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        model.addAttribute("functionLimitParam", (Object)this.paramsService.loadFunctionLimitParam());
        model.addAttribute("timeoutParam", (Object)this.paramsService.loadTimeoutParam());
        model.addAttribute("userreportParam", (Object)this.paramsService.loadUserReportParam());
        model.addAttribute("productSelectionParam", (Object)this.paramsService.loadProductSelectionParam());
        return "/oc/params";
    }
    
    @RequestMapping(value = { "params/timeout" }, method = { RequestMethod.POST })
    public String timeout(final TimeoutParam param, final RedirectAttributes redirectAttributes) {
        this.paramsService.saveTimeoutParam(param);
        EventUtils.publishLogEvent("更新超时配置, 设置为" + param.getTimeout() + "小时, 处理方式:" + param.getTimeOutStrategy());
        redirectAttributes.addFlashAttribute("message", (Object)"保存成功");
        return "redirect:/oc/params";
    }
    
    @RequestMapping(value = { "params/function" }, method = { RequestMethod.POST })
    public String funcitonLimit(final FunctionLimitParam param, final RedirectAttributes redirectAttributes) {
        this.paramsService.saveFunctionLimitParam(param);
        EventUtils.publishLogEvent("更新功能限制设置, 自动限制" + (param.isEnableAutoLimit() ? "开启" : "关闭"));
        redirectAttributes.addFlashAttribute("message", (Object)"保存成功");
        return "redirect:/oc/params";
    }
    
    @RequestMapping(value = { "params/userreport" }, method = { RequestMethod.POST })
    public String userReport(final UserReportParam param, final RedirectAttributes redirectAttributes) {
        this.paramsService.saveUserReport(param);
        EventUtils.publishLogEvent("更新用户报告设置");
        redirectAttributes.addFlashAttribute("message", (Object)"保存成功");
        return "redirect:/oc/params";
    }
    
    @RequestMapping(value = { "params/product-selection" }, method = { RequestMethod.POST })
    public String userReport(final ProductSelectionParam param, final RedirectAttributes redirectAttributes) {
        this.paramsService.saveProductSelectionParam(param);
        redirectAttributes.addFlashAttribute("message", (Object)"保存成功");
        return "redirect:/oc/params";
    }
}
