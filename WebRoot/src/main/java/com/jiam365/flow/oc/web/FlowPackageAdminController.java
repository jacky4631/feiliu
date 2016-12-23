// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import java.util.Iterator;
import java.util.List;
import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.flow.base.web.WebResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.oc.utils.EventUtils;
import javax.servlet.http.HttpServletResponse;
import com.jiam365.flow.server.product.FlowPackage;
import com.jiam365.flow.server.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.FlowPackageManager;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/flowpackage" })
public class FlowPackageAdminController
{
    private static Logger logger;
    @Autowired
    private FlowPackageManager flowPackageManager;
    @Autowired
    private MobileService mobileService;
    
    @RequestMapping(value = { "save" }, method = { RequestMethod.POST })
    @ResponseBody
    public FlowPackage create(final FlowPackage flowPackage, final HttpServletResponse response) {
        try {
            this.flowPackageManager.saveOrUpdate(flowPackage);
            EventUtils.publishLogEvent("保存通道" + flowPackage.getFlowChannelId() + "产品" + flowPackage.getTitle() + " 折扣:" + flowPackage.getDiscount() + " 状态: " + (flowPackage.isEnabled() ? "启用" : "禁用"));
        }
        catch (Exception e) {
            FlowPackageAdminController.logger.error("保存通道流量产品错误", (Throwable)e);
            response.setStatus(400);
        }
        return flowPackage;
    }
    
    @RequestMapping(value = { "remove" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse remove(final String id) {
        final FlowPackage flowPackage = this.flowPackageManager.removeFlowPackage(id);
        if (flowPackage != null) {
            EventUtils.publishLogEvent("删除通道" + flowPackage.getFlowChannelId() + "产品" + flowPackage.getTitle());
        }
        return WebResponse.success("成功删除");
    }
    
    @RequestMapping(value = { "chstatus" }, method = { RequestMethod.POST })
    @ResponseBody
    public StatusResponse chstatus(final String id) {
        final FlowPackage flowPackage = this.flowPackageManager.changeStatus(id);
        EventUtils.publishLogEvent("改变通道" + flowPackage.getFlowChannelId() + "产品" + flowPackage.getTitle() + "状态为" + (flowPackage.isEnabled() ? "启用" : "禁用"));
        final StatusResponse response = new StatusResponse();
        response.makeSuccess();
        response.setEnabled(flowPackage.isEnabled());
        return response;
    }
    
    @RequestMapping(value = { "grp-create" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse grpCreate(final long channelId, final String state, final Boolean roamable, final String telco, final int idMethod, final int prior, final double discount) {
        try {
            this.flowPackageManager.createGroup(channelId, state, roamable, telco, idMethod, prior, discount);
        }
        catch (RuntimeException e) {
            return WebResponse.fail(e.getMessage());
        }
        EventUtils.publishLogEvent("在通道" + channelId + "中添加产品组" + state + telco + (roamable ? "(可漫游)" : "(不漫游)"));
        return WebResponse.success("成功添加产品组");
    }
    
    @RequestMapping(value = { "grp-del" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse grpDel(final long channelId, final String groupCode) {
        this.flowPackageManager.removeGroup(channelId, groupCode);
        EventUtils.publishLogEvent("删除通道" + channelId + "产品组" + groupCode);
        return WebResponse.success("成功删除了" + groupCode);
    }
    
    @RequestMapping(value = { "grp-enable" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse grpEnable(final long channelId, final String groupCode) {
        this.setStatus(channelId, groupCode, true);
        EventUtils.publishLogEvent("启用通道" + channelId + "产品组" + groupCode);
        return WebResponse.success("成功启用了" + groupCode);
    }
    
    @RequestMapping(value = { "grp-disable" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse grpDisable(final long channelId, final String groupCode) {
        this.setStatus(channelId, groupCode, false);
        EventUtils.publishLogEvent("禁用通道" + channelId + "产品组" + groupCode);
        return WebResponse.success("成功禁用了" + groupCode);
    }
    
    @RequestMapping(value = { "grp-discount" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse grpDiscount(final long channelId, final String groupCode, final double discount) {
        final List<FlowPackage> flowPackages = this.flowPackageManager.findByGroupCode(channelId, groupCode);
        for (final FlowPackage flowPackage : flowPackages) {
            flowPackage.setDiscount(discount);
            flowPackage.setBillAmount(DoubleUtils.round(DoubleUtils.mul(discount, flowPackage.getPrice()), 2));
            this.flowPackageManager.saveOrUpdate(flowPackage);
        }
        EventUtils.publishLogEvent("保存通道" + channelId + "产品组" + groupCode + "折扣为" + discount);
        return WebResponse.success("成功修改了" + groupCode + "折扣为" + discount);
    }
    
    @RequestMapping(value = { "grp-prior" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse grpPrior(final long channelId, final String groupCode, final int prior) {
        final List<FlowPackage> flowPackages = this.flowPackageManager.findByGroupCode(channelId, groupCode);
        for (final FlowPackage flowPackage : flowPackages) {
            flowPackage.setPriority(prior);
            this.flowPackageManager.saveOrUpdate(flowPackage);
        }
        EventUtils.publishLogEvent("保存通道" + channelId + "产品组" + groupCode + "优先级为" + prior);
        return WebResponse.success("成功修改了" + groupCode + "优先级为" + prior);
    }
    
    @RequestMapping(value = { "grp-dlg" }, method = { RequestMethod.GET })
    public String grpDlg(final Model model) {
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        return "oc/channel-product-create";
    }
    
    private void setStatus(final long channelId, final String groupCode, final boolean enabled) {
        final List<FlowPackage> flowPackages = this.flowPackageManager.findByGroupCode(channelId, groupCode);
        for (final FlowPackage flowPackage : flowPackages) {
            flowPackage.setEnabled(enabled);
            this.flowPackageManager.saveOrUpdate(flowPackage);
        }
    }
    
    static {
        FlowPackageAdminController.logger = LoggerFactory.getLogger((Class)FlowPackageAdminController.class);
    }
    
    class StatusResponse extends WebResponse
    {
        private static final long serialVersionUID = 3205375448599278324L;
        private boolean enabled;
        
        public boolean isEnabled() {
            return this.enabled;
        }
        
        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
        }
    }
}
