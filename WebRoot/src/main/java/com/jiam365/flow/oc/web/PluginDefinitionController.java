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
import com.jiam365.flow.server.entity.PluginDefinition;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.PluginDefinitionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/plugins" })
public class PluginDefinitionController
{
    @Autowired
    private PluginDefinitionManager pluginDefinitionManager;
    
    @RequestMapping({ "create" })
    public String pluginForm(final Model model) {
        final PluginDefinition definition = new PluginDefinition();
        model.addAttribute("action", (Object)"create");
        model.addAttribute("plugin", (Object)definition);
        return "oc/plugin-form";
    }
    
    @RequestMapping(value = { "create" }, method = { RequestMethod.POST })
    public String create(final PluginDefinition definition, final RedirectAttributes redirectAttributes) {
        this.pluginDefinitionManager.save(definition);
        redirectAttributes.addFlashAttribute("message", (Object)(" 保存插件" + definition.getTitle() + "成功"));
        return "redirect:/oc/plugins";
    }
    
    @RequestMapping(value = { "update/{id}" }, method = { RequestMethod.GET })
    public String updateForm(@PathVariable("id") final String id, final Model model) {
        model.addAttribute("plugin", (Object)this.pluginDefinitionManager.get(id));
        model.addAttribute("action", (Object)"update");
        return "oc/plugin-form";
    }
    
    @RequestMapping(value = { "update" }, method = { RequestMethod.POST })
    public String update(@ModelAttribute("definition") final PluginDefinition definition, final RedirectAttributes redirectAttributes) {
        try {
            this.pluginDefinitionManager.save(definition);
            redirectAttributes.addFlashAttribute("message", (Object)"修改成功");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", (Object)"修改失败");
        }
        return "redirect:/oc/plugins/update/" + definition.getId();
    }
    
    @RequestMapping
    public String list() {
        return "oc/plugin-list";
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<PluginDefinition> listData(final HttpServletRequest request) {
        final PageParamLoader<PluginDefinition> pageParamLoader = (PageParamLoader<PluginDefinition>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<PluginDefinition> page = (Page<PluginDefinition>)pageParamLoader.parse();
        page = this.pluginDefinitionManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<PluginDefinition>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
    
    @RequestMapping(value = { "remove" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse remove(final String pluginId) {
        this.pluginDefinitionManager.remove(pluginId);
        return WebResponse.success("执行成功");
    }
    
    @ModelAttribute
    public void getDefinition(@RequestParam(value = "id", defaultValue = "") final String id, final Model model) {
        if (StringUtils.isNotBlank((CharSequence)id)) {
            final PluginDefinition definition = this.pluginDefinitionManager.get(id);
            model.addAttribute("definition", (Object)definition);
        }
    }
}
