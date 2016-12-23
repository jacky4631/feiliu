// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.flow.server.entity.OperationLog;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.OperationLogManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/oplog" })
public class OpLogController
{
    @Autowired
    private OperationLogManager operationLogManager;
    
    @RequestMapping
    public String list() {
        return "oc/oplog-list";
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<OperationLog> listData(final HttpServletRequest request) {
        final PageParamLoader<OperationLog> pageParamLoader = (PageParamLoader<OperationLog>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<OperationLog> page = (Page<OperationLog>)pageParamLoader.parse();
        page = this.operationLogManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<OperationLog>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
}
