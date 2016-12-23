// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.agent.web;

import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.persistent.SearchFilter;
import com.jiam365.modules.web.DataTablePageLoader;
import org.apache.shiro.SecurityUtils;
import com.jiam365.flow.base.service.account.ShiroDbRealm;
import com.jiam365.flow.server.entity.TransferLog;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.TransferLogManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/agent/usertransfer" })
public class UserTransferController
{
    @Autowired
    private TransferLogManager transferLogManager;
    
    @RequestMapping
    public String list() {
        return "agent/user-transfer-list";
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<TransferLog> listData(final HttpServletRequest request) {
        final ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        final PageParamLoader<TransferLog> pageParamLoader = (PageParamLoader<TransferLog>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<TransferLog> page = (Page<TransferLog>)pageParamLoader.parse();
        page.getSearchFilters().add(new SearchFilter("EQ_username", user.username));
        page = this.transferLogManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<TransferLog>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
}
