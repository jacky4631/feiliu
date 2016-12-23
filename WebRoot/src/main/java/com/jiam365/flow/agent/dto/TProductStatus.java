//
// Decompiled by Procyon v0.5.30
//

package com.jiam365.flow.agent.dto;

import com.jiam365.flow.server.product.UserProduct;
import java.util.Iterator;
import com.jiam365.flow.server.product.FlowProduct;
import com.jiam365.modules.telco.Telco;
import java.util.ArrayList;
import java.util.List;

public class TProductStatus
{
    private String providerName;
    private String providerCode;
    private List<TFlowProduct> packages;
    private List<TFlowProduct> statePackages;

    public TProductStatus() {
        this.packages = new ArrayList<TFlowProduct>();
        this.statePackages = new ArrayList<TFlowProduct>();
    }

    public TProductStatus(final Telco provider, final List<FlowProduct> flowProducts, final List<FlowProduct> stateProducts) {
        this.packages = new ArrayList<TFlowProduct>();
        this.statePackages = new ArrayList<TFlowProduct>();
        this.providerName = provider.getName();
        this.providerCode = provider.getCode();
        if (flowProducts != null) {
            for (final FlowProduct flowProduct : flowProducts) {
                this.packages.add(new TFlowProduct(flowProduct));
            }
        }
        for (final FlowProduct product : stateProducts) {
            this.statePackages.add(new TFlowProduct(product));
        }
    }

    public void init(final Telco provider, final List<UserProduct> userProducts, final List<UserProduct> stateProducts) {
        this.providerName = provider.getName();
        this.providerCode = provider.getCode();
        if (userProducts != null) {
            for (final UserProduct userProduct : userProducts) {
                this.packages.add(new TFlowProduct(userProduct));
            }
        }
        for (final UserProduct product : stateProducts) {
            this.statePackages.add(new TFlowProduct(product));
        }
    }

    public String getProviderName() {
        return this.providerName;
    }

    public void setProviderName(final String providerName) {
        this.providerName = providerName;
    }

    public String getProviderCode() {
        return this.providerCode;
    }

    public void setProviderCode(final String providerCode) {
        this.providerCode = providerCode;
    }

    public List<TFlowProduct> getPackages() {
        return this.packages;
    }

    public void setPackages(final List<TFlowProduct> packages) {
        this.packages = packages;
    }

    public List<TFlowProduct> getStatePackages() {
        return this.statePackages;
    }

    public void setStatePackages(final List<TFlowProduct> statePackages) {
        this.statePackages = statePackages;
    }
}
