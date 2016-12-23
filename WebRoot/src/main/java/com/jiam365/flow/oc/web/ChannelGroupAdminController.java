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
import com.jiam365.flow.base.utils.ShiroUtils;
import com.jiam365.flow.server.entity.ChannelGroup;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.ChannelGroupManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/channelgroup" })
public class ChannelGroupAdminController
{
    @Autowired
    private ChannelGroupManager channelGroupManager;
    
    @RequestMapping({ "create" })
    public String form(final Model model) {
        final ChannelGroup group = new ChannelGroup();
        group.setCreator(ShiroUtils.currentUsername());
        model.addAttribute("action", (Object)"create");
        model.addAttribute("group", (Object)group);
        return "oc/channel-group-form";
    }
    
    @RequestMapping(value = { "create" }, method = { RequestMethod.POST })
    public String create(final ChannelGroup group, final RedirectAttributes redirectAttributes) {
        this.channelGroupManager.save(group);
        redirectAttributes.addFlashAttribute("message", (Object)("保存通道组" + group.getTitle() + "成功"));
        return "redirect:/oc/channelgroup/update/" + group.getId();
    }
    
    @RequestMapping(value = { "update/{id}" }, method = { RequestMethod.GET })
    public String updateForm(@PathVariable("id") final String id, final Model model) {
        model.addAttribute("group", (Object)this.channelGroupManager.get(id));
        model.addAttribute("action", (Object)"update");
        return "oc/channel-group-form";
    }
    
    @RequestMapping(value = { "update" }, method = { RequestMethod.POST })
    public String update(@ModelAttribute("group") final ChannelGroup group, final RedirectAttributes redirectAttributes) {
        try {
            this.channelGroupManager.save(group);
            redirectAttributes.addFlashAttribute("message", (Object)"修改成功");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", (Object)"修改失败");
        }
        return "redirect:/oc/channelgroup/update/" + group.getId();
    }
    
    @RequestMapping
    public String list() {
        return "oc/channel-group-list";
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<ChannelGroup> listData(final HttpServletRequest request) {
        final PageParamLoader<ChannelGroup> pageParamLoader = (PageParamLoader<ChannelGroup>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<ChannelGroup> page = (Page<ChannelGroup>)pageParamLoader.parse();
        page = this.channelGroupManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<ChannelGroup>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
    
    @RequestMapping(value = { "remove" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse remove(final String channelGroupId) {
        this.channelGroupManager.remove(channelGroupId);
        return WebResponse.success("执行成功");
    }
    
    @ModelAttribute
    public void getDefinition(@RequestParam(value = "id", defaultValue = "") final String id, final Model model) {
        if (StringUtils.isNotBlank((CharSequence)id)) {
            final ChannelGroup group = this.channelGroupManager.get(id);
            model.addAttribute("group", (Object)group);
        }
    }
}
