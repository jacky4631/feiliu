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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.oc.utils.EventUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jiam365.flow.base.utils.ShiroUtils;
import com.jiam365.flow.server.entity.SmMemo;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.SmMemoManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/smmemo" })
public class SmMemoAdminController
{
    @Autowired
    private SmMemoManager smMemoManager;
    
    @RequestMapping({ "create" })
    public String smMemoForm(final Model model) {
        final SmMemo smMemo = new SmMemo();
        final String username = ShiroUtils.currentUsername();
        smMemo.setCreator(username);
        model.addAttribute("smMemo", (Object)smMemo);
        return "oc/sm-memo-form";
    }
    
    @RequestMapping(value = { "save" }, method = { RequestMethod.POST })
    public String save(final SmMemo smMemo, final RedirectAttributes redirectAttributes) {
        this.smMemoManager.save(smMemo);
        EventUtils.publishLogEvent("保存短信模板备案信息: " + smMemo.getOwner());
        redirectAttributes.addFlashAttribute("message", (Object)"保存成功");
        return "redirect:/oc/smmemo";
    }
    
    @RequestMapping(value = { "update/{id}" }, method = { RequestMethod.GET })
    public String updateForm(@PathVariable("id") final String id, final Model model) {
        model.addAttribute("smMemo", (Object)this.smMemoManager.get(id));
        return "oc/sm-memo-form";
    }
    
    @RequestMapping(value = { "batch-del" }, method = { RequestMethod.POST })
    @ResponseBody
    public RestResponse batchDel(final String[] ids) {
        for (final String id : ids) {
            this.smMemoManager.remove(id);
        }
        EventUtils.publishLogEvent("删除短信备案信息: " + ids.length + "条");
        return new RestResponse("10000", "操作完成");
    }
    
    @RequestMapping
    public String list() {
        return "oc/sm-memo-list";
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<SmMemo> listData(final HttpServletRequest request) {
        final PageParamLoader<SmMemo> pageParamLoader = (PageParamLoader<SmMemo>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<SmMemo> page = (Page<SmMemo>)pageParamLoader.parse();
        page = this.smMemoManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<SmMemo>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
}
