// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.agent.web;

import com.jiam365.flow.base.utils.DownloadUtils;
import org.apache.commons.io.FilenameUtils;
import com.jiam365.modules.web.ExporterPageLoader;
import com.jiam365.modules.utils.FileUtils;
import com.jiam365.modules.tools.ConfigurationHolder;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;
import com.jiam365.modules.utils.SimpleDateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.server.entity.TradeLog;
import com.jiam365.modules.persistent.PageParamLoader;
import org.apache.commons.lang3.StringUtils;
import java.util.Collection;
import com.jiam365.modules.mapper.BeanMapper;
import com.jiam365.modules.persistent.Page;
import com.jiam365.modules.persistent.SearchFilter;
import com.jiam365.modules.web.DataTablePageLoader;
import org.apache.shiro.SecurityUtils;
import com.jiam365.flow.base.service.account.ShiroDbRealm;
import com.jiam365.flow.agent.dto.TTradeLog;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import com.jiam365.flow.server.dic.State;
import java.util.List;
import org.springframework.ui.Model;
import com.jiam365.flow.server.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.TradeLogManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/agent" })
public class TradeLogController
{
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private MobileService mobileService;
    
    @RequestMapping({ "consumption-records" })
    public String rechargeHistory(final Model model) {
        final List<State> states = this.mobileService.findAllStates();
        model.addAttribute("states", (Object)states);
        return "agent/consumption-records";
    }
    
    @RequestMapping(value = { "consumption-records/data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<TTradeLog> listData(final HttpServletRequest request) {
        final ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        final PageParamLoader<TradeLog> pageParamLoader = (PageParamLoader<TradeLog>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<TradeLog> page = (Page<TradeLog>)pageParamLoader.parse();
        page.getSearchFilters().add(new SearchFilter("EQ_username", user.username));
        page = this.tradeLogManager.searchPage(page);
        final String draw = request.getParameter("draw");
        final Page<TTradeLog> page2 = (Page<TTradeLog>)new Page();
        page2.setCurrentPage(page.getCurrentPage());
        page2.setPageSize(page.getPageSize());
        page2.setTotalCount(page.getTotalCount());
        page2.setResult(BeanMapper.mapList((Collection)page.getResult(), (Class)TTradeLog.class));
        return (DataTable<TTradeLog>)DataTable.createTable((Page)page2, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
    
    @RequestMapping({ "month/{type}" })
    @ResponseBody
    public String[] export(@PathVariable("type") final int type) {
        Date[] period = null;
        switch (type) {
            case 1: {
                period = SimpleDateUtils.getMonthPeriod(SimpleDateUtils.getPastMonth());
                break;
            }
            case 2: {
                period = SimpleDateUtils.getMonthPeriod(SimpleDateUtils.get2MonthAgo());
                break;
            }
            default: {
                period = SimpleDateUtils.getMonthPeriod(SimpleDateUtils.getCurMonth());
                break;
            }
        }
        final String d1 = DateFormatUtils.format(period[0], "yyyy-MM-dd");
        final String d2 = DateFormatUtils.format(period[1], "yyyy-MM-dd");
        return new String[] { d1, d2 };
    }
    
    @RequestMapping({ "export-bill" })
    public String export(final HttpServletRequest request, final HttpServletResponse response) {
        final ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        final String path = FileUtils.createPath(ConfigurationHolder.properties().getProperty("download.temp", "/tmp"));
        String fileName = user.username + "\u67e5\u8be2\u5bfc\u51fa\u6e05\u5355.csv";
        String fullFileName = path + "/" + fileName;
        final PageParamLoader<TradeLog> pageParamLoader = (PageParamLoader<TradeLog>)new ExporterPageLoader(request, new String[] { "filter_" });
        final Page<TradeLog> page = (Page<TradeLog>)pageParamLoader.parse();
        page.getSearchFilters().add(new SearchFilter("EQ_username", user.username));
        this.tradeLogManager.dump2CSV(fullFileName, page);
        if (FileUtils.sizeOf(fullFileName) >= 1024000L) {
            fullFileName = FileUtils.zipFile(fullFileName, new boolean[0]);
            fileName = FilenameUtils.getName(fullFileName);
        }
        DownloadUtils.download(request, response, fullFileName, fileName);
        return null;
    }
}
