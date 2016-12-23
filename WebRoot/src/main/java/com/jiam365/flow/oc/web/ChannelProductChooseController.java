// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.oc.dto.TChannleProduct;
import java.util.LinkedList;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import com.jiam365.flow.server.service.FlowPackageManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/channel/product" })
public class ChannelProductChooseController
{
    @Autowired
    private ChannelAdminManager channelAdminManager;
    @Autowired
    private FlowPackageManager flowPackageManager;
    
    @RequestMapping({ "dlg" })
    public String dlg(final Model model) {
        model.addAttribute("channels", (Object)this.channelAdminManager.findAll());
        return "oc/channelprod/channel-product-forchoose";
    }
    
    @RequestMapping({ "detail/{id}" })
    public String detail(@PathVariable("id") final Long channelId, final Model model) {
        model.addAttribute("productGroups", (Object)this.flowPackageManager.findProductGroup(channelId));
        model.addAttribute("channel", (Object)this.channelAdminManager.get(channelId));
        return "oc/channelprod/channel-product-forchoose-detail";
    }
    
    @RequestMapping({ "tags" })
    public String tags(final String allowChannelProducts, final Model model) {
        final List<TChannleProduct> channleProductList = new LinkedList<TChannleProduct>();
        if (StringUtils.isNotBlank((CharSequence)allowChannelProducts)) {
            final String[] split;
            final String[] cpArray = split = allowChannelProducts.split(",");
            for (final String cp : split) {
                final String name = this.flowPackageManager.fullChannelGroupName(cp);
                channleProductList.add(new TChannleProduct(cp, name));
            }
        }
        model.addAttribute("channleProductList", (Object)channleProductList);
        return "oc/channelprod/channel-product-tags";
    }
}
