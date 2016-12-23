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
import com.jiam365.flow.base.web.WebResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.oc.utils.EventUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jiam365.flow.server.entity.BalanceNotify;
import org.springframework.ui.Model;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.BalanceNotifyManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/notify" })
public class BalanceNotifyController
{
    @Autowired
    private BalanceNotifyManager balanceNotifyManager;
    @Autowired
    private ChannelAdminManager channelAdminManager;
    
    @RequestMapping({ "create" })
    public String notifyForm(final Model model) {
        final BalanceNotify notify = new BalanceNotify();
        model.addAttribute("notify", notify);
        model.addAttribute("channels", this.channelAdminManager.findAll());
        return "oc/balance-notify-form";
    }
    
    @RequestMapping(value = { "save" }, method = { RequestMethod.POST })
    public String save(final BalanceNotify notify, final RedirectAttributes redirectAttributes) {
        this.balanceNotifyManager.save(notify);
        EventUtils.publishLogEvent("保存余额提醒: " + notify.getTitle());
        redirectAttributes.addFlashAttribute("message", "保存成功");
        return "redirect:/oc/notify";
    }
    
    @RequestMapping(value = { "update/{id}" }, method = { RequestMethod.GET })
    public String updateForm(@PathVariable("id") final String id, final Model model) {
        model.addAttribute("notify", (Object)this.balanceNotifyManager.get(id));
        model.addAttribute("channels", (Object)this.channelAdminManager.findAll());
        return "oc/balance-notify-form";
    }
    
    @RequestMapping(value = { "chgstatus" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse changeStatus(final String nid) {
        final BalanceNotify notify = this.balanceNotifyManager.get(nid);
        notify.setStatus(!notify.isStatus());
        this.balanceNotifyManager.save(notify);
        EventUtils.publishLogEvent("修改余额提醒" + notify.getTitle() + "状态为" + (notify.isStatus() ? "启用" : "关闭"));
        return WebResponse.success("执行成功");
    }
    
    @RequestMapping(value = { "del" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse batchDel(final String nid) {
        this.balanceNotifyManager.remove(nid);
        EventUtils.publishLogEvent("删除余额提醒" + nid);
        return WebResponse.success("执行成功");
    }
    
    @RequestMapping
    public String list() {
        return "oc/balance-notify-list";
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<BalanceNotify> listData(final HttpServletRequest request) {
        final PageParamLoader<BalanceNotify> pageParamLoader = (PageParamLoader<BalanceNotify>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<BalanceNotify> page = (Page<BalanceNotify>)pageParamLoader.parse();
        page = this.balanceNotifyManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<BalanceNotify>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
}
