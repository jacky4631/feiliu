// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.server.utils.DoubleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.server.entity.TransferLog;
import com.jiam365.flow.oc.utils.EventUtils;
import com.jiam365.flow.base.utils.ShiroUtils;
import com.jiam365.flow.base.web.WebResponse;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.TransferLogManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/transferlog" })
public class TransferLogController
{
    @Autowired
    private TransferLogManager transferLogManager;
    
    @RequiresRoles(value = { "financial", "admin" }, logical = Logical.OR)
    @RequestMapping
    public String list() {
        return "oc/transferlog-list";
    }
    
    @RequiresRoles(value = { "financial", "admin" }, logical = Logical.OR)
    @RequestMapping(value = { "chgsubject" }, produces = { "application/json" })
    @ResponseBody
    public WebResponse chg(final String tid, final int subject) {
        final TransferLog log = this.transferLogManager.get(tid);
        log.setAccountingSubject(subject);
        this.transferLogManager.update(log);
        EventUtils.publishLogEvent(ShiroUtils.currentUserDisplayName() + "设置财务日志" + tid + "的科目为" + subject);
        return WebResponse.success("成功更新" + tid + "的科目为" + subject);
    }
    
    @RequiresRoles(value = { "financial", "admin" }, logical = Logical.OR)
    @RequestMapping(value = { "checkoff" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse checkoff(final String[] ids) {
        String username = "";
        double amount = 0.0;
        for (int i = 0; i < ids.length; ++i) {
            final TransferLog log = this.transferLogManager.get(ids[i]);
            if (i == 0) {
                username = log.getUsername();
            }
            else if (!StringUtils.endsWithIgnoreCase((CharSequence)username, (CharSequence)log.getUsername())) {
                return WebResponse.fail("不是一个用户或通道的财务流水无法核销");
            }
            if (log.getAccountingSubject() < 100) {
                return WebResponse.fail("基本收入、支出无法进行核销");
            }
            amount = DoubleUtils.add(amount, log.getAmount());
        }
        if (amount > 0.0 || amount < 0.0) {
            return WebResponse.fail("财务流水无法核销, 因为合计流水不为0");
        }
        for (final String id : ids) {
            this.transferLogManager.delete(id);
        }
        EventUtils.publishLogEvent(ShiroUtils.currentUsername() + "核销了" + ids.length + "条关于" + username + "的财务流水");
        return WebResponse.success("成功核销完成, 共清理" + ids.length + "条记录");
    }
    
    @RequiresRoles(value = { "financial", "admin" }, logical = Logical.OR)
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<TransferLog> listData(final HttpServletRequest request) {
        final PageParamLoader<TransferLog> pageParamLoader = (PageParamLoader<TransferLog>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<TransferLog> page = (Page<TransferLog>)pageParamLoader.parse();
        page = this.transferLogManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<TransferLog>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
}
