// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.RequestParam;
import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.base.web.WebResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.oc.utils.EventUtils;
import com.jiam365.flow.server.entity.FlowInterceptor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jiam365.flow.server.entity.FlowCallbackInterceptor;
import org.springframework.ui.Model;
import com.jiam365.flow.server.service.UserManager;
import com.jiam365.flow.server.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.FlowInterceptorManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/interceptors/callback" })
public class FlowCallbackInterceptorAdminController
{
    @Autowired
    private FlowInterceptorManager flowInterceptorManager;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private UserManager userManager;
    
    @RequestMapping
    public String list() {
        return "oc/interceptor-callback-list";
    }
    
    @RequestMapping({ "create" })
    public String createForm(final Model model) {
        model.addAttribute("interceptor", (Object)new FlowCallbackInterceptor());
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        model.addAttribute("users", (Object)this.userManager.findAll());
        model.addAttribute("action", (Object)"create");
        return "oc/interceptor-callback-form";
    }
    
    @RequestMapping(value = { "create" }, method = { RequestMethod.POST })
    public String create(final FlowCallbackInterceptor interceptor, final RedirectAttributes redirectAttributes) {
        this.flowInterceptorManager.save(interceptor);
        redirectAttributes.addFlashAttribute("message", (Object)" 保存成功");
        EventUtils.publishLogEvent("创建失败拦截器" + interceptor.getName() + interceptor.description());
        return "redirect:/oc/interceptors/callback/update/" + interceptor.getId();
    }
    
    @RequestMapping(value = { "update/{id}" }, method = { RequestMethod.GET })
    public String updateForm(@PathVariable("id") final String id, final Model model) {
        model.addAttribute("interceptor", (Object)this.flowInterceptorManager.getFlowCallbackInterceptor(id));
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        model.addAttribute("users", (Object)this.userManager.findAll());
        model.addAttribute("action", (Object)"update");
        return "oc/interceptor-callback-form";
    }
    
    @RequestMapping(value = { "update" }, method = { RequestMethod.POST })
    public String update(@ModelAttribute("interceptor") final FlowCallbackInterceptor interceptor, final RedirectAttributes redirectAttributes) {
        try {
            this.flowInterceptorManager.save(interceptor);
            EventUtils.publishLogEvent("更新失败拦截器" + interceptor.getName() + interceptor.description());
            redirectAttributes.addFlashAttribute("message", (Object)"修改成功");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", (Object)"修改失败");
        }
        return "redirect:/oc/interceptors/callback/update/" + interceptor.getId();
    }
    
    @RequestMapping(value = { "chgstatus" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse changeStatus(final String iid) {
        final FlowCallbackInterceptor interceptor = this.flowInterceptorManager.getFlowCallbackInterceptor(iid);
        interceptor.setStatus(!interceptor.isEnabled());
        this.flowInterceptorManager.saveCallbackInterceptor(interceptor);
        EventUtils.publishLogEvent("改变失败拦截器[" + interceptor.getName() + "]状态为" + (interceptor.isEnabled() ? "启用" : "关闭"));
        return WebResponse.success("执行成功");
    }
    
    @RequestMapping(value = { "batch-del" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse batchDel(final String[] ids) {
        for (final String id : ids) {
            try {
                final FlowCallbackInterceptor interceptor = this.flowInterceptorManager.deleteCallbackInterceptor(id);
                if (interceptor != null) {
                    EventUtils.publishLogEvent("删除失败拦截器" + interceptor.getName());
                }
            }
            catch (Exception ex) {}
        }
        return WebResponse.success("成功删除");
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<FlowCallbackInterceptor> listData(final HttpServletRequest request) {
        final PageParamLoader<FlowCallbackInterceptor> pageParamLoader = (PageParamLoader<FlowCallbackInterceptor>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<FlowCallbackInterceptor> page = (Page<FlowCallbackInterceptor>)pageParamLoader.parse();
        page = this.flowInterceptorManager.searchCallbackInterceptorPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<FlowCallbackInterceptor>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
    
    @ModelAttribute
    public void getInterceptor(@RequestParam(value = "id", defaultValue = "") final String id, final Model model) {
        if (StringUtils.isNotBlank((CharSequence)id)) {
            model.addAttribute("interceptor", (Object)this.flowInterceptorManager.getFlowCallbackInterceptor(id));
        }
    }
}
