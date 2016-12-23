// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import java.util.Arrays;
import java.util.Iterator;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import java.util.ArrayList;
import com.jiam365.flow.server.params.ProductSelectionParam;
import com.jiam365.flow.server.channel.ChannelConnection;
import com.jiam365.flow.server.product.FlowPackage;
import com.jiam365.flow.server.dto.ChoosedProduct;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.server.engine.route.Condition;
import java.util.List;
import com.jiam365.flow.server.params.ParamsService;
import com.jiam365.flow.server.service.FlowPackageManager;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.channel.ChannelConnectionManager;

public class ChannelChooser
{
    @Autowired
    private ChannelConnectionManager channelConnectionManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private FlowPackageManager flowPackageManager;
    @Autowired
    private ParamsService paramsService;
    private List<Condition> conditions;
    private SelectionHelper selectionHelper;
    
    public ChannelChooser() {
        this.selectionHelper = new SelectionHelper();
    }
    
    public ChoosedProduct choose(final RechargeRequest request, final ChooseRestrict... restricts) {
        final ChoosedProduct choosedProduct = this.chooseFlowPackage(request, restricts);
        if (choosedProduct == null || choosedProduct.isNull()) {
            throw new TradeException(request.getProductId() + "\u4e0d\u5b58\u5728\u6216\u672a\u6388\u6743\u4f7f\u7528");
        }
        return choosedProduct;
    }
    
    private ChoosedProduct chooseFlowPackage(final RechargeRequest request, final ChooseRestrict... restricts) {
        final String productId = request.getProductId();
        final List<FlowPackage> flowPackages = this.findFlowPackages(productId, request.getMobileInfo().getStateCode());
        if (flowPackages.size() == 0) {
            return null;
        }
        final int[] filterWithUserChannelsAndSort;
        final int[] filtedFlowPackages = filterWithUserChannelsAndSort = this.filterWithUserChannelsAndSort(request, flowPackages);
        for (final int idx : filterWithUserChannelsAndSort) {
            final FlowPackage flowPackage = flowPackages.get(idx);
            final ChannelConnection connection = this.channelConnectionManager.pickOnlineChannleConnection(flowPackage.getFlowChannelId());
            final ChoosedProduct choosedProduct = new ChoosedProduct(connection, flowPackage);
            for (int i = 0, j = this.conditions.size(); i < j; ++i) {
                final Condition condition = this.conditions.get(i);
                if (!condition.pass(request, choosedProduct, restricts)) {
                    break;
                }
                if (i == j - 1) {
                    this.selectionHelper.markSelection(productId, idx);
                    return choosedProduct;
                }
            }
        }
        return null;
    }
    
    private List<FlowPackage> findFlowPackages(final String productId, final String stateCode) {
        final ProductSelectionParam param = this.paramsService.loadProductSelectionParam();
        List<FlowPackage> flowPackages;
        if (param.isEnableReplacement()) {
            flowPackages = this.flowPackageManager.findCompatiableFlowPackages(productId, stateCode, true);
        }
        else {
            flowPackages = this.flowPackageManager.findAttachedFlowPackages(productId, true);
        }
        return flowPackages;
    }
    
    private int[] filterWithUserChannelsAndSort(final RechargeRequest request, final List<FlowPackage> flowPackages) {
        final List<String> userChannelProducts = this.userManager.getUserChannels(request.getUsername());
        final List<String> protectedProducts = this.userManager.getGrantedProtectedProducts(request.getUsername());
        int[] connections = null;
        if (userChannelProducts.size() > 0) {
            connections = this.filterPackages(flowPackages, userChannelProducts, protectedProducts);
        }
        else {
            final ProductSelectionParam param = this.paramsService.loadProductSelectionParam();
            switch (param.getSelectionMode()) {
                case 1: {
                    connections = this.selectionHelper.inTurnIndex(request.getProductId(), flowPackages.size());
                    break;
                }
                default: {
                    connections = this.selectionHelper.normalIndex(flowPackages.size());
                    break;
                }
            }
        }
        return connections;
    }
    
    private int[] filterPackages(final List<FlowPackage> flowPackageList, final List<String> userChannelProducts, final List<String> grantedProtectedProducts) {
        final List<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < flowPackageList.size(); ++i) {
            final FlowPackage flowPackage = flowPackageList.get(i);
            final Long channelId = flowPackage.getFlowChannelId();
            final String groupCode = ProductIDHelper.productGroup(flowPackage.getProductId());
            if (userChannelProducts.contains(channelId + "-ALL") || userChannelProducts.contains(channelId + "-" + groupCode) || grantedProtectedProducts.contains(channelId + "-" + groupCode)) {
                ret.add(i);
            }
        }
        final int[] values = new int[ret.size()];
        int j = 0;
        for (final int value : ret) {
            values[j++] = value;
        }
        return values;
    }
    
    public void setConditions(final Condition... conditions) {
        this.conditions = Arrays.asList(conditions);
    }
}
