// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.agent.web;

import com.jiam365.flow.server.product.FlowProduct;
import com.jiam365.flow.sdk.MobileInfo;
import java.util.Collection;
import com.jiam365.modules.mapper.BeanMapper;
import java.util.Collections;
import com.jiam365.flow.agent.dto.TFlowProduct;
import java.util.List;
import com.jiam365.modules.telco.Telco;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.server.rest.RechareApplication;
import org.apache.shiro.SecurityUtils;
import com.jiam365.flow.base.service.account.ShiroDbRealm;
import com.jiam365.flow.server.rest.RestResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.server.engine.FlowRouter;
import com.jiam365.flow.server.product.UserProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.MobileService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/agent" })
public class WebRechargeController
{
    @Autowired
    private MobileService mobileService;
    @Autowired
    private UserProductManager userProductManager;
    @Autowired
    private FlowRouter flowRouter;
    
    @RequestMapping(value = { "recharge" }, method = { RequestMethod.GET })
    public String recharge() {
        return "agent/recharge";
    }
    
    @RequestMapping(value = { "recharge" }, method = { RequestMethod.POST })
    @ResponseBody
    public RestResponse recharge(final String mobile, final String product) {
        final ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        final RechareApplication application = new RechareApplication(user.username, mobile, product);
        this.flowRouter.route(application);
        return new RestResponse("10000", "\u63d0\u4ea4\u6210\u529f, \u5145\u503c\u7ed3\u679c\u4ece\u8fd0\u8425\u5546\u8fd4\u56de\u540e, \u60a8\u53ef\u4ee5\u5728\u6d88\u8d39\u8bb0\u5f55\u4e2d\u67e5\u770b.");
    }
    
    @RequestMapping(value = { "packages/{mobile}/{flag}" }, method = { RequestMethod.GET }, produces = { "application/json" })
    @ResponseBody
    public MixedPackage packages(@PathVariable("mobile") final String mobile, @PathVariable("flag") final int flag) {
        final MobileInfo info = this.mobileService.mobileInfo(mobile);
        final String mobileType = info.getArea() + info.getMobileType();
        final Telco provider = info.getProvider();
        if (provider == null) {
            return new MixedPackage(null, mobileType, null, null);
        }
        final ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        final String username = user.username;
        final List<FlowProduct> nationPackages = this.userProductManager.findUserOnlineFlowProduct(username, info, true);
        List<FlowProduct> statePackages = Collections.emptyList();
        if (flag == 1) {
            statePackages = this.userProductManager.findUserOnlineFlowProduct(username, info, false);
        }
        return new MixedPackage(provider, mobileType, BeanMapper.mapList((Collection)nationPackages, (Class)TFlowProduct.class), BeanMapper.mapList((Collection)statePackages, (Class)TFlowProduct.class));
    }
    
    public static class MixedPackage
    {
        private String vendor;
        private String mobileType;
        private List<TFlowProduct> statePackages;
        private List<TFlowProduct> nationPackages;
        
        public MixedPackage(final Telco provider, final String mobileType, final List<TFlowProduct> nationPackages, final List<TFlowProduct> statePackages) {
            this.statePackages = Collections.emptyList();
            this.nationPackages = Collections.emptyList();
            this.vendor = ((provider == null) ? "" : provider.getName());
            this.mobileType = mobileType;
            if (nationPackages != null) {
                this.nationPackages = nationPackages;
            }
            if (statePackages != null) {
                this.statePackages = statePackages;
            }
        }
        
        public List<TFlowProduct> getStatePackages() {
            return this.statePackages;
        }
        
        public void setStatePackages(final List<TFlowProduct> statePackages) {
            this.statePackages = statePackages;
        }
        
        public List<TFlowProduct> getNationPackages() {
            return this.nationPackages;
        }
        
        public void setNationPackages(final List<TFlowProduct> nationPackages) {
            this.nationPackages = nationPackages;
        }
        
        public String getVendor() {
            return this.vendor;
        }
        
        public void setVendor(final String vendor) {
            this.vendor = vendor;
        }
        
        public String getMobileType() {
            return this.mobileType;
        }
        
        public void setMobileType(final String mobileType) {
            this.mobileType = mobileType;
        }
    }
}
