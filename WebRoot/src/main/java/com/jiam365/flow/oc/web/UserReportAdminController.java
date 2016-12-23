// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.flow.server.usercallback.UserReport;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.server.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.usercallback.UserReportManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/userreport" })
public class UserReportAdminController
{
    @Autowired
    private UserReportManager userReportManager;
    
    @RequestMapping(value = { "repush" }, method = { RequestMethod.POST })
    @ResponseBody
    public RestResponse repush(final String[] ids) {
        for (final String id : ids) {
            this.userReportManager.reset(id);
        }
        return new RestResponse("10000", "操作完成");
    }
    
    @RequestMapping(value = { "batch-del" }, method = { RequestMethod.POST })
    @ResponseBody
    public RestResponse remove(final String[] ids) {
        for (final String id : ids) {
            this.userReportManager.remove(id);
        }
        return new RestResponse("10000", "操作完成");
    }
    
    @RequestMapping
    public String index() {
        return "/oc/userreport-list";
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<UserReport> listData(final HttpServletRequest request) {
        final PageParamLoader<UserReport> pageParamLoader = (PageParamLoader<UserReport>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<UserReport> page = (Page<UserReport>)pageParamLoader.parse();
        page = this.userReportManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<UserReport>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
}
