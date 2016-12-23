// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.InitBinder;
import java.beans.PropertyEditor;
import com.jiam365.flow.base.web.TelcoConverter;
import org.springframework.web.bind.WebDataBinder;
import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.server.product.FlowProduct;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.oc.utils.EventUtils;
import com.jiam365.modules.telco.Telco;
import com.jiam365.flow.base.web.WebResponse;
import com.jiam365.flow.server.entity.User;
import com.jiam365.flow.server.product.UserProduct;
import com.jiam365.flow.oc.dto.TProductGranted;
import com.jiam365.flow.server.channel.FlowChannel;
import java.util.Iterator;
import java.util.List;
import com.jiam365.flow.server.product.FlowPackage;
import com.jiam365.flow.oc.dto.TFlowPackage;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import com.jiam365.flow.server.service.UserManager;
import com.jiam365.flow.server.service.MobileService;
import com.jiam365.flow.server.product.UserProductManager;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import com.jiam365.flow.server.product.FlowProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.FlowPackageManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/product" })
public class ProductAdminController
{
    @Autowired
    private FlowPackageManager flowPackageManager;
    @Autowired
    private FlowProductManager flowProductManager;
    @Autowired
    private ChannelAdminManager channelAdminManager;
    @Autowired
    private UserProductManager userProductManager;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private UserManager userManager;
    
    @RequestMapping
    public String list(final Model model) {
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        return "oc/product-list";
    }
    
    @RequestMapping({ "package/{productId}" })
    public String listAllFlowpackage(@PathVariable("productId") final String productId, final Model model) {
        final List<FlowPackage> packages = this.flowPackageManager.findAttachedFlowPackages(productId, new boolean[0]);
        final List<TFlowPackage> beans = new ArrayList<TFlowPackage>();
        for (final FlowPackage flowPackage : packages) {
            final TFlowPackage bean = new TFlowPackage(flowPackage);
            final FlowChannel channel = this.channelAdminManager.get(flowPackage.getFlowChannelId());
            if (channel != null) {
                bean.setChannel(channel.getName());
                bean.setChannelStatus(channel.getStatus() == 0);
            }
            beans.add(bean);
        }
        model.addAttribute("flowpackages", (Object)beans);
        return "oc/product-package";
    }
    
    @RequestMapping({ "sale/{productId}" })
    public String listAllGrantedUsers(@PathVariable("productId") final String productId, final Model model) {
        final List<UserProduct> userProducts = this.userProductManager.findUserProducts(productId);
        final List<TProductGranted> beans = new ArrayList<TProductGranted>();
        for (final UserProduct userProduct : userProducts) {
            final User user = this.userManager.loadUserByUsername(userProduct.getUsername());
            if (user != null) {
                beans.add(new TProductGranted(user, userProduct));
            }
        }
        model.addAttribute("saleProducts", (Object)beans);
        return "oc/product-sale";
    }
    
    @RequestMapping({ "forchoose" })
    public String choose(final Model model) {
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        return "oc/product-forchoose";
    }
    
    @RequestMapping({ "clonedlg" })
    public String cloneDlg(final Model model) {
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        return "oc/product-clone";
    }
    
    @RequestMapping({ "clone" })
    @ResponseBody
    public WebResponse doClone(final String provider, final String state) {
        final Telco telco = Telco.valueOf(provider);
        final boolean isSuc = this.flowProductManager.cloneProductGroup(telco, state);
        if (isSuc) {
            EventUtils.publishLogEvent("克隆基础产品组" + provider + " " + state);
        }
        return isSuc ? WebResponse.success("克隆成功, 请到产品库列表中查看或编辑") : WebResponse.fail("全网产品没有定义, 无法克隆到省级产品中.");
    }
    
    @RequestMapping(value = { "create" }, method = { RequestMethod.GET })
    public String create(final Model model) {
        model.addAttribute("product", (Object)new FlowProduct());
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        return "oc/product-form";
    }
    
    @RequestMapping(value = { "create" }, method = { RequestMethod.POST })
    @ResponseBody
    public FlowProduct create(final FlowProduct flowProduct, final HttpServletResponse response) {
        try {
            final FlowProduct oldProduct = this.flowProductManager.get(flowProduct.getId());
            if (oldProduct != null && Math.abs(oldProduct.getPrice() - flowProduct.getPrice()) > 0.01) {
                this.flowPackageManager.updateProductPrice(flowProduct.getId(), flowProduct.getPrice());
                this.userProductManager.updateProductPrice(flowProduct.getId(), flowProduct.getPrice());
            }
            this.flowProductManager.save(flowProduct);
            EventUtils.publishLogEvent("保存基础产品" + flowProduct.getName() + " 单价" + flowProduct.getPrice());
        }
        catch (Exception e) {
            response.setStatus(406);
        }
        return flowProduct;
    }
    
    @RequestMapping(value = { "remove" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse remove(final String prdId) {
        this.flowProductManager.remove(prdId);
        EventUtils.publishLogEvent("删除基础产品" + prdId);
        return WebResponse.success("执行成功");
    }
    
    @RequestMapping(value = { "chgstatus" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse changeStatus(final String prdId) {
        final FlowProduct product = this.flowProductManager.get(prdId);
        product.setEnabled(!product.isEnabled());
        this.flowProductManager.save(product);
        EventUtils.publishLogEvent("改变基础产品" + prdId + "状态为" + (product.isEnabled() ? "启用" : "关闭"));
        return WebResponse.success("执行成功");
    }
    
    @RequestMapping(value = { "update/{id}" }, method = { RequestMethod.GET })
    public String updateForm(@PathVariable("id") final String id, final Model model) {
        model.addAttribute("product", (Object)this.flowProductManager.get(id));
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        return "oc/product-form";
    }
    
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", defaultValue = "") final String id, final Model model) {
        if (StringUtils.isNotBlank((CharSequence)id)) {
            final FlowProduct product = this.flowProductManager.get(id);
            if (product != null) {
                model.addAttribute("flowProduct", (Object)product);
            }
        }
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<FlowProduct> listData(final HttpServletRequest request) {
        final PageParamLoader<FlowProduct> pageParamLoader = (PageParamLoader<FlowProduct>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<FlowProduct> page = (Page<FlowProduct>)pageParamLoader.parse();
        page = this.flowProductManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<FlowProduct>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
    
    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor((Class)Telco.class, (PropertyEditor)new TelcoConverter());
    }
}
