// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.agent.web;

import com.jiam365.flow.server.product.UserProduct;
import com.jiam365.flow.server.product.FlowProduct;
import java.util.List;
import java.util.HashMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jiam365.flow.agent.dto.TProductStatus;
import com.jiam365.modules.telco.Telco;
import java.util.Map;
import com.jiam365.flow.base.utils.ShiroUtils;
import org.springframework.ui.Model;
import com.jiam365.flow.server.params.ParamsService;
import com.jiam365.flow.server.product.UserProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.product.FlowProductManager;
import org.springframework.stereotype.Controller;

@Controller
public class FlowpacksController
{
    @Autowired
    private FlowProductManager flowProductManager;
    @Autowired
    private UserProductManager userProductManager;
    @Autowired
    private ParamsService paramsService;
    
    @RequestMapping({ "/agent/userflow" })
    public String flowpacks(final Model model) {
        Map<Telco, TProductStatus> overviews;
        if (this.paramsService.loadFunctionLimitParam().showAllProducts()) {
            overviews = this.allProducts();
        }
        else {
            final String username = ShiroUtils.currentUsername();
            overviews = this.authedProducts(username);
        }
        model.addAttribute("overviews", (Object)overviews);
        return "agent/userflow";
    }
    
    private Map<Telco, TProductStatus> allProducts() {
        final Map<Telco, TProductStatus> overviews = new HashMap<Telco, TProductStatus>();
        for (final Telco provider : Telco.values()) {
            final List<FlowProduct> flowProducts = this.flowProductManager.findNationProducts(provider);
            final List<FlowProduct> stateProducts = this.flowProductManager.findStateProducts(provider);
            final TProductStatus overview = new TProductStatus(provider, flowProducts, stateProducts);
            overviews.put(provider, overview);
        }
        return overviews;
    }
    
    private Map<Telco, TProductStatus> authedProducts(final String username) {
        final Map<Telco, TProductStatus> overviews = new HashMap<Telco, TProductStatus>();
        for (final Telco provider : Telco.values()) {
            final List<UserProduct> userProducts = this.userProductManager.findNationProducts(username, provider);
            final List<UserProduct> userStateProducts = this.userProductManager.findStateProducts(username, provider);
            final TProductStatus overview = new TProductStatus();
            overview.init(provider, userProducts, userStateProducts);
            overviews.put(provider, overview);
        }
        return overviews;
    }
}
