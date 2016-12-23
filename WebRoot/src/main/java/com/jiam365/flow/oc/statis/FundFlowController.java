// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.statis;

import java.util.Map;
import java.util.Date;
import com.jiam365.modules.utils.SimpleDateUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.ui.Model;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.TransferLogManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/statis" })
public class FundFlowController
{
    @Autowired
    private TransferLogManager transferLogManager;
    
    @RequiresRoles(value = { "financial", "admin" }, logical = Logical.OR)
    @RequestMapping({ "fundflow" })
    public String fundFlow() {
        return "oc/statis/fund-flow";
    }
    
    @RequiresRoles(value = { "financial", "admin" }, logical = Logical.OR)
    @RequestMapping({ "fundflow-content" })
    public String fundFlowContent(final Model model, final String startDate, final String endDate) {
        try {
            final Date d1 = SimpleDateUtils.getDateStart(DateUtils.parseDate(startDate, new String[] { "yyyy-MM-dd" }));
            final Date d2 = SimpleDateUtils.getDateEnd(DateUtils.parseDate(endDate, new String[] { "yyyy-MM-dd" }));
            final Map<Integer, Double> totalTransfer = this.transferLogManager.getTransferTotal(d1, d2, new String[0]);
            model.addAttribute("totalTransfer", (Object)totalTransfer);
        }
        catch (Exception ex) {}
        return "oc/statis/fund-flow-content";
    }
}
