// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dto;

import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.flow.server.service.MobileService;
import com.jiam365.flow.server.entity.ChannelProductGroupProfile;

public class ProductGroup
{
    private long flowChannelId;
    private long packagesCount;
    private long enabledCount;
    private String name;
    private String groupCode;
    private boolean canReplaceNA;
    private boolean roamable;
    private boolean needProtected;
    private boolean hasRestrict;
    
    public ProductGroup() {
        this.name = "";
        this.canReplaceNA = true;
        this.roamable = true;
        this.needProtected = false;
    }
    
    public static ProductGroup create(final Long channelId, final String groupCode, final long totalCount, final long enabledCount, final ChannelProductGroupProfile profile, final MobileService service) {
        final ProductGroup group = new ProductGroup();
        group.setFlowChannelId(channelId);
        group.setGroupCode(groupCode);
        group.setPackagesCount(totalCount);
        final boolean roamable = !groupCode.endsWith("$");
        final String flag = roamable ? String.valueOf(groupCode.charAt(groupCode.length() - 1)) : String.valueOf(groupCode.charAt(groupCode.length() - 2));
        final String stateName = service.getStateNameByCode(groupCode.substring(0, 2));
        group.setName(ProductIDHelper.telcoFlag2Name(flag) + "-" + stateName + (roamable ? "" : "$"));
        group.setEnabledCount(enabledCount);
        group.setCanReplaceNA(profile.isCanReplaceNA());
        group.setHasRestrict(profile.getRestrictStates().size() > 0);
        group.setRoamable(profile.isRoamable());
        group.setNeedProtected(profile.isNeedProtected());
        return group;
    }
    
    public boolean isNA() {
        return this.groupCode.startsWith("NA");
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("TChannelProduct's name can't be null");
        }
        this.name = name;
    }
    
    public String getGroupCode() {
        return this.groupCode;
    }
    
    public void setGroupCode(final String groupCode) {
        this.groupCode = groupCode;
    }
    
    public long getFlowChannelId() {
        return this.flowChannelId;
    }
    
    public void setFlowChannelId(final long flowChannelId) {
        this.flowChannelId = flowChannelId;
    }
    
    public long getPackagesCount() {
        return this.packagesCount;
    }
    
    public void setPackagesCount(final long packagesCount) {
        this.packagesCount = packagesCount;
    }
    
    public long getEnabledCount() {
        return this.enabledCount;
    }
    
    public void setEnabledCount(final long enabledCount) {
        this.enabledCount = enabledCount;
    }
    
    public boolean isCanReplaceNA() {
        return this.canReplaceNA;
    }
    
    public void setCanReplaceNA(final boolean canReplaceNA) {
        this.canReplaceNA = canReplaceNA;
    }
    
    public boolean isRoamable() {
        return this.roamable;
    }
    
    public void setRoamable(final boolean roamable) {
        this.roamable = roamable;
    }
    
    public boolean isNeedProtected() {
        return this.needProtected;
    }
    
    public void setNeedProtected(final boolean needProtected) {
        this.needProtected = needProtected;
    }
    
    public boolean isHasRestrict() {
        return this.hasRestrict;
    }
    
    public void setHasRestrict(final boolean hasRestrict) {
        this.hasRestrict = hasRestrict;
    }
}
