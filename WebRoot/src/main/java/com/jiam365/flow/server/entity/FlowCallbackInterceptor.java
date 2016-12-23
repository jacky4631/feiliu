// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import java.util.Arrays;
import com.jiam365.modules.telco.Telco;
import com.jiam365.flow.sdk.MobileInfo;
import com.jiam365.flow.server.engine.Trade;
import org.mongodb.morphia.annotations.Entity;

@Entity(value = "flow_callback_interceptor", noClassnameStored = true)
public class FlowCallbackInterceptor extends FlowInterceptor
{
    public static String NEXT_AUTO_SELECT;
    public static String NEXT_MANUAL;
    private String nextStep;
    private boolean priceProtected;
    private int retryTimes;
    private boolean status;
    
    public FlowCallbackInterceptor() {
        this.nextStep = FlowCallbackInterceptor.NEXT_AUTO_SELECT;
        this.priceProtected = true;
        this.retryTimes = 0;
        this.status = false;
    }
    
    public boolean match(final Trade trade) {
        if (this.isEnabled()) {
            final MobileInfo info = trade.getRequest().getMobileInfo();
            final Telco telco = info.getProvider();
            switch (telco) {
                case CMCC: {
                    if (this.cmcc.contains("NA") || this.cmcc.contains(info.getStateCode())) {
                        return true;
                    }
                    break;
                }
                case TELECOM: {
                    if (this.telecom.contains("NA") || this.telecom.contains(info.getStateCode())) {
                        return true;
                    }
                    break;
                }
                case UNICOM: {
                    if (this.unicom.contains("NA") || this.unicom.contains(info.getStateCode())) {
                        return true;
                    }
                    break;
                }
            }
            return this.users.contains(trade.getRequest().getUsername());
        }
        return false;
    }
    
    public boolean nextAuto() {
        return FlowCallbackInterceptor.NEXT_AUTO_SELECT.equals(this.nextStep);
    }
    
    public boolean isEnabled() {
        return this.status;
    }
    
    public String getNextStep() {
        return this.nextStep;
    }
    
    public void setNextStep(final String nextStep) {
        this.nextStep = nextStep;
    }
    
    public boolean isPriceProtected() {
        return this.priceProtected;
    }
    
    public void setPriceProtected(final boolean priceProtected) {
        this.priceProtected = priceProtected;
    }
    
    public int getRetryTimes() {
        return this.retryTimes;
    }
    
    public void setRetryTimes(final int retryTimes) {
        this.retryTimes = ((retryTimes < 0) ? 0 : retryTimes);
    }
    
    public boolean isStatus() {
        return this.status;
    }
    
    public void setStatus(final boolean status) {
        this.status = status;
    }
    
    @Override
    public String description() {
        final StringBuilder sb = new StringBuilder(256);
        sb.append(super.description());
        if (this.users.size() > 0) {
            sb.append(" \u62e6\u622a\u4ee3\u7406\u5546 ").append(Arrays.toString(this.users.toArray()));
        }
        sb.append(" \u4e0b\u4e00\u6b65\u52a8\u4f5c").append(FlowCallbackInterceptor.NEXT_AUTO_SELECT.equals(this.nextStep) ? " \u81ea\u52a8\u5904\u7406" : " \u4eba\u5de5\u5904\u7406");
        sb.append(" \u72b6\u6001:").append(this.status ? "\u542f\u7528" : "\u7981\u7528");
        return sb.toString();
    }
    
    static {
        FlowCallbackInterceptor.NEXT_AUTO_SELECT = "AUTO";
        FlowCallbackInterceptor.NEXT_MANUAL = "MANUAL";
    }
}
