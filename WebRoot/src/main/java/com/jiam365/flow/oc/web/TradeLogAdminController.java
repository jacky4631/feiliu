// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import com.jiam365.flow.base.utils.DownloadUtils;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import com.jiam365.modules.web.ExporterPageLoader;
import com.jiam365.flow.base.utils.ShiroUtils;
import com.jiam365.modules.utils.FileUtils;
import com.jiam365.modules.tools.ConfigurationHolder;
import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import com.jiam365.flow.base.web.WebResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.server.dto.UnProcessedTradeCount;
import com.jiam365.flow.server.channel.FlowChannel;
import com.jiam365.flow.server.dic.State;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.server.entity.TradeRetry;
import java.util.List;
import com.jiam365.flow.server.entity.TradeLog;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import com.jiam365.flow.server.service.TradeRetryManager;
import com.jiam365.flow.server.service.MobileService;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.TradeLogManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/tradelog" })
public class TradeLogAdminController
{
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private ChannelAdminManager channelAdminManager;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private TradeRetryManager tradeRetryManager;
    
    @RequestMapping(value = { "detail/{id}" }, method = { RequestMethod.GET })
    public String tradeLogForm(@PathVariable("id") final String id, final Model model) {
        final TradeLog log = this.tradeLogManager.get(id);
        model.addAttribute("tradeLog", (Object)log);
        final List<TradeRetry> retries = this.tradeRetryManager.findByTradeId(id);
        model.addAttribute("retries", (Object)retries);
        return "oc/tradelog-view";
    }
    
    @RequestMapping
    public String tradeLog(final Model model) {
        this.listviewProccess(model);
        return "oc/tradelog-list";
    }
    
    @RequestMapping(value = { "/{type}" }, method = { RequestMethod.GET })
    public String tradeLogCust(@PathVariable("type") final int type, final Model model) {
        this.listviewProccess(model);
        return "oc/tradelog-list-" + type;
    }
    
    private void listviewProccess(final Model model) {
        final List<State> states = this.mobileService.findAllStates();
        model.addAttribute("states", (Object)states);
        final List<FlowChannel> channels = this.channelAdminManager.findAll();
        model.addAttribute("channels", (Object)channels);
    }
    
    @RequestMapping(value = { "unprocess" }, produces = { "application/json" })
    @ResponseBody
    public UnProcessedTradeCount unProcessedTradeCount() {
        return this.tradeLogManager.getUnProcessedCount();
    }
    
    @RequestMapping(value = { "save-remark" }, produces = { "application/json" })
    @ResponseBody
    public WebResponse saveRemark(final String tradeId, final String remark) {
        this.tradeLogManager.updateRemark(tradeId, remark);
        return WebResponse.success("更新成功");
    }
    
    @RequestMapping(value = { "message-dlg" }, method = { RequestMethod.GET })
    public String messageDlg() {
        return "oc/tradelog-message";
    }
    
    @RequestMapping(value = { "message" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse tradeLogForm(final String[] ids, final String message) {
        for (final String id : ids) {
            this.tradeLogManager.updateChannelMessage(id, message);
        }
        return WebResponse.success("更新完成");
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<TradeLog> listData(final HttpServletRequest request) {
        final PageParamLoader<TradeLog> pageParamLoader = (PageParamLoader<TradeLog>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<TradeLog> page = (Page<TradeLog>)pageParamLoader.parse();
        page = this.tradeLogManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<TradeLog>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
    
    private String[] createExportFile(final HttpServletRequest request, final boolean isCost, final FunctionForExport<TradeLog> functionForExport) {
        final String path = FileUtils.createPath(ConfigurationHolder.properties().getProperty("download.temp", "/tmp"));
        String fileName = ShiroUtils.currentUser() + (isCost ? "导出上游对账清单" : "导出下游清单") + System.currentTimeMillis() + ".csv";
        String fullFileName = path + "/" + fileName;
        final PageParamLoader<TradeLog> pageParamLoader = (PageParamLoader<TradeLog>)new ExporterPageLoader(request, new String[] { "filter_" });
        final Page<TradeLog> page = (Page<TradeLog>)pageParamLoader.parse();
        functionForExport.apply(fullFileName, page);
        if (FileUtils.sizeOf(fullFileName) >= 1024000L) {
            fullFileName = FileUtils.zipFile(fullFileName, new boolean[0]);
            fileName = FilenameUtils.getName(fullFileName);
        }
        return new String[] { fullFileName, fileName };
    }
    
    @RequestMapping({ "export" })
    public void export(final HttpServletRequest request, final HttpServletResponse response) {
        final String[] fileNames = this.createExportFile(request, false, this.tradeLogManager::dump2CSV);
        DownloadUtils.download(request, response, fileNames[0], fileNames[1]);
    }
    
    @RequestMapping({ "export2" })
    public void export2(final HttpServletRequest request, final HttpServletResponse response) {
        final String[] fileNames = this.createExportFile(request, true, this.tradeLogManager::dumpCostList2CSV);
        DownloadUtils.download(request, response, fileNames[0], fileNames[1]);
    }
    
    @FunctionalInterface
    public interface FunctionForExport<T>
    {
        void apply(final String p0, final Page<T> p1);
    }
}
