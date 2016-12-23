// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.server.rest.RestResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.oc.utils.EventUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jiam365.flow.server.entity.Bulletin;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.BulletinManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/bulletin" })
public class BulletinAdminController
{
    @Autowired
    private BulletinManager bulletinManager;
    
    @RequestMapping({ "create" })
    public String bulletinForm(final Model model) {
        final Bulletin bulletin = new Bulletin();
        model.addAttribute("bulletin", (Object)bulletin);
        return "oc/bulletin-form";
    }
    
    @RequestMapping(value = { "save" }, method = { RequestMethod.POST })
    public String save(final Bulletin bulletin, final RedirectAttributes redirectAttributes) {
        this.bulletinManager.save(bulletin);
        EventUtils.publishLogEvent("发布公告信息: " + bulletin.getTitle());
        redirectAttributes.addFlashAttribute("message", (Object)"发布成功");
        return "redirect:/oc/bulletin";
    }
    
    @RequestMapping(value = { "batch-del" }, method = { RequestMethod.POST })
    @ResponseBody
    public RestResponse batchDel(final String[] ids) {
        for (final String id : ids) {
            this.bulletinManager.remove(id);
        }
        EventUtils.publishLogEvent("删除公告信息: " + ids.length + "条");
        return new RestResponse("10000", "操作完成");
    }
    
    @RequestMapping
    public String list() {
        return "oc/bulletin-list";
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<Bulletin> listData(final HttpServletRequest request) {
        final PageParamLoader<Bulletin> pageParamLoader = (PageParamLoader<Bulletin>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<Bulletin> page = (Page<Bulletin>)pageParamLoader.parse();
        page = this.bulletinManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<Bulletin>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
}
