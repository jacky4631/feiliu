// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.RequestParam;
import com.jiam365.flow.base.web.WebResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jiam365.flow.server.entity.Role;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.RoleManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/roles" })
public class RoleAdminController
{
    @Autowired
    private RoleManager roleManager;
    
    @RequestMapping({ "create" })
    public String roleForm(final Model model) {
        final Role role = new Role();
        model.addAttribute("role", (Object)role);
        model.addAttribute("action", (Object)"create");
        return "oc/role-form";
    }
    
    @RequestMapping(value = { "create" }, method = { RequestMethod.POST })
    public String create(final Role role, final RedirectAttributes redirectAttributes) {
        this.roleManager.save(role);
        redirectAttributes.addFlashAttribute("message", (Object)("保存角色" + role.getName() + "成功"));
        return "redirect:/oc/roles";
    }
    
    @RequestMapping(value = { "update/{id}" }, method = { RequestMethod.GET })
    public String updateForm(@PathVariable("id") final String id, final Model model) {
        model.addAttribute("role", (Object)this.roleManager.get(id));
        model.addAttribute("action", (Object)"update");
        return "oc/role-form";
    }
    
    @RequestMapping(value = { "update" }, method = { RequestMethod.POST })
    public String update(@ModelAttribute("userrole") final Role role, final RedirectAttributes redirectAttributes) {
        try {
            this.roleManager.save(role);
            redirectAttributes.addFlashAttribute("message", (Object)"修改成功");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", (Object)"修改失败");
        }
        return "redirect:/oc/roles/update/" + role.getId();
    }
    
    @RequestMapping
    public String list() {
        return "oc/role-list";
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<Role> listData(final HttpServletRequest request) {
        final PageParamLoader<Role> pageParamLoader = (PageParamLoader<Role>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<Role> page = (Page<Role>)pageParamLoader.parse();
        page = this.roleManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<Role>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
    
    @RequestMapping(value = { "remove" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse remove(final String rid) {
        this.roleManager.remove(rid);
        return WebResponse.success("执行成功");
    }
    
    @ModelAttribute
    public void getRole(@RequestParam(value = "id", defaultValue = "") final String id, final Model model) {
        if (StringUtils.isNotBlank((CharSequence)id)) {
            model.addAttribute("userrole", (Object)this.roleManager.get(id));
        }
    }
}
