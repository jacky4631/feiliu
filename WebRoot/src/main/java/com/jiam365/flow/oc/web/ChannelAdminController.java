// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import com.jiam365.flow.server.channel.ChannelConnection;
import java.util.Iterator;
import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import java.util.List;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import com.jiam365.modules.json.JsonFormatTools;
import com.jiam365.flow.sdk.ChannelHandler;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.server.channel.HandlerContainer;
import com.jiam365.flow.server.channel.ConnectionParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.base.web.WebResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.oc.utils.EventUtils;
import com.jiam365.flow.base.utils.ShiroUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import com.jiam365.flow.server.channel.FlowChannel;
import org.springframework.ui.Model;
import com.jiam365.flow.server.service.MobileService;
import com.jiam365.flow.server.service.ChannelAccountManager;
import com.jiam365.flow.server.service.PluginDefinitionManager;
import com.jiam365.flow.server.product.FlowProductManager;
import com.jiam365.flow.server.channel.ChannelConnectionManager;
import com.jiam365.flow.server.service.FlowPackageManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/channel" })
public class ChannelAdminController
{
    private static Logger logger;
    @Autowired
    private ChannelAdminManager channelAdminManager;
    @Autowired
    private FlowPackageManager flowPackageManager;
    @Autowired
    private ChannelConnectionManager channelConnectionManager;
    @Autowired
    private FlowProductManager flowProductManager;
    @Autowired
    private PluginDefinitionManager pluginDefinitionManager;
    @Autowired
    private ChannelAccountManager channelAccountManager;
    @Autowired
    private MobileService mobileService;
    
    @RequestMapping
    public String list() {
        return "oc/channel-list";
    }
    
    @RequestMapping({ "create" })
    public String createForm(final Model model) {
        model.addAttribute("channel", (Object)new FlowChannel());
        model.addAttribute("plugins", (Object)this.pluginDefinitionManager.findAll());
        model.addAttribute("action", (Object)"create");
        return "oc/channel-form";
    }
    
    @RequestMapping(value = { "create" }, method = { RequestMethod.POST })
    public String create(@Valid final FlowChannel channel, final RedirectAttributes redirectAttributes) {
        channel.setCreator(ShiroUtils.currentUsername());
        this.channelAdminManager.createNew(channel);
        EventUtils.publishLogEvent("登记供应商: " + channel.getName());
        redirectAttributes.addFlashAttribute("message", (Object)(" 保存供应商" + channel.getName() + "成功"));
        return "redirect:/oc/channel/update/" + channel.getId();
    }
    
    @RequestMapping(value = { "update/{id}" }, method = { RequestMethod.GET })
    public String updateForm(@PathVariable("id") final Long id, final Model model) {
        final FlowChannel flowChannel = this.channelAdminManager.get(id);
        if (flowChannel != null) {
            model.addAttribute("channel", (Object)this.channelAdminManager.get(id));
        }
        else {
            model.addAttribute("channel", (Object)new FlowChannel());
        }
        model.addAttribute("plugins", (Object)this.pluginDefinitionManager.findAll());
        model.addAttribute("products", (Object)this.flowProductManager.findAll());
        model.addAttribute("productGroups", (Object)this.flowPackageManager.findProductGroup(id));
        model.addAttribute("action", (Object)"update");
        return "oc/channel-form";
    }
    
    @RequestMapping(value = { "product/{id}/{group}" }, method = { RequestMethod.GET })
    public String channelProduct(@PathVariable("id") final Long id, @PathVariable("group") final String groupCode, final Model model) {
        model.addAttribute("packages", (Object)this.flowPackageManager.findByGroupCode(id, groupCode));
        model.addAttribute("profile", (Object)this.flowPackageManager.getGroupProfile(id + "-" + groupCode));
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        return "oc/channel-product";
    }
    
    @RequestMapping(value = { "update" }, method = { RequestMethod.POST })
    public String update(@ModelAttribute("channel") final FlowChannel channel, final RedirectAttributes redirectAttributes) {
        this.channelAdminManager.save(channel);
        EventUtils.publishLogEvent("更新供应商信息: " + channel.getName());
        redirectAttributes.addFlashAttribute("message", (Object)"更新成功");
        return "redirect:/oc/channel/update/" + channel.getId();
    }
    
    @RequestMapping(value = { "batch-del" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse batchDel(final Long[] ids) {
        int count = 0;
        for (final long id : ids) {
            Label_0067: {
                try {
                    this.channelAdminManager.removeChannel(id);
                }
                catch (Exception e) {
                    ChannelAdminController.logger.warn("删除Id为{}的供应商失败, {}", (Object)id, (Object)e.getMessage());
                    break Label_0067;
                }
                ++count;
            }
        }
        if (count == 0) {
            return WebResponse.fail("删除失败, 有交易记录以及合同额不为0的通道不能删除");
        }
        EventUtils.publishLogEvent("删除了" + count + "个通道");
        return WebResponse.success("成功删除了" + count + "个通道, 有交易记录以及合同额不为0的通道不能删除");
    }
    
    @RequestMapping(value = { "update-params" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse updateConnectionParam(final long channelId, final String handlerClass, final String params) {
        final FlowChannel channel = this.channelAdminManager.get(channelId);
        if (channel != null) {
            final ConnectionParam param = new ConnectionParam();
            param.setHandlerClass(handlerClass);
            param.setParamJson(params);
            channel.setChannelConnectionParam(param);
            this.channelAdminManager.save(channel);
            HandlerContainer.clear(channelId);
            this.channelConnectionManager.deActiveChannel(channelId);
            this.channelConnectionManager.activeChannel(Long.valueOf(channelId));
            EventUtils.publishLogEvent("更新了" + channel.getName() + "的高级连接参数信息");
            return WebResponse.success("更新成功");
        }
        return WebResponse.fail("更新失败, 通道未发现");
    }
    
    @RequestMapping(value = { "active" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse active(final Long[] ids, final boolean permanent) {
        for (final long id : ids) {
            if (permanent) {
                this.channelConnectionManager.updateStatus(id, 0);
            }
            this.channelConnectionManager.activeChannel(Long.valueOf(id));
            EventUtils.publishLogEvent("启用通道" + id);
        }
        return WebResponse.success("操作完成");
    }
    
    @RequestMapping(value = { "deactive" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse deActive(final Long[] ids, final boolean permanent) {
        for (final long id : ids) {
            this.channelConnectionManager.deActiveChannel(id);
            if (permanent) {
                this.channelConnectionManager.updateStatus(id, -1);
            }
            EventUtils.publishLogEvent("关停通道" + id);
        }
        return WebResponse.success("操作完成");
    }
    
    @RequestMapping(value = { "flow-params" }, method = { RequestMethod.POST })
    @ResponseBody
    public String flowParams(final String channelClassname) {
        if (StringUtils.isNotBlank((CharSequence)channelClassname)) {
            try {
                final Class c = Class.forName(channelClassname);
                final ChannelHandler handler = (ChannelHandler)c.newInstance();
                return JsonFormatTools.formatJson(handler.getParamTemplate());
            }
            catch (Exception ex) {}
        }
        return "{}";
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<FlowChannel> listData(final HttpServletRequest request) {
        final PageParamLoader<FlowChannel> pageParamLoader = (PageParamLoader<FlowChannel>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<FlowChannel> page = (Page<FlowChannel>)pageParamLoader.parse();
        page = this.channelAdminManager.searchPage(page);
        this.readStatusAndBalance(page.getResult());
        final String draw = request.getParameter("draw");
        return (DataTable<FlowChannel>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
    
    private void readStatusAndBalance(final List<FlowChannel> channels) {
        final int TEMP_STATUS_ENABLED = 8;
        final int TEMP_STATUS_DISABLED = 9;
        for (final FlowChannel channel : channels) {
            final ChannelConnection connection = this.channelConnectionManager.pickOnlineChannleConnection(channel.getId());
            if (connection != null) {
                if (channel.getStatus() == -1) {
                    channel.setStatus(8);
                }
            }
            else if (channel.getStatus() == 0) {
                channel.setStatus(9);
            }
            channel.setBalance(this.channelAccountManager.getBalance(channel.getId()));
        }
    }
    
    @ModelAttribute
    public void getChannel(@RequestParam(value = "id", defaultValue = "-1") final Long id, final Model model) {
        if (id != -1L) {
            model.addAttribute("channel", (Object)this.channelAdminManager.get(id));
        }
    }
    
    static {
        ChannelAdminController.logger = LoggerFactory.getLogger((Class)ChannelAdminController.class);
    }
}
