// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import java.util.ArrayList;
import java.util.List;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_channelproduct_profile", noClassnameStored = true)
public class ChannelProductGroupProfile implements Serializable
{
    private static final long serialVersionUID = -3931834801220381939L;
    @Id
    private String id;
    private String name;
    @Indexed
    private Long channelId;
    private boolean canReplaceNA;
    private boolean roamable;
    @Indexed
    private boolean needProtected;
    private List<String> restrictStates;
    
    public ChannelProductGroupProfile() {
        this.name = "";
        this.canReplaceNA = true;
        this.roamable = true;
        this.needProtected = false;
        this.restrictStates = new ArrayList<String>();
    }
    
    public ChannelProductGroupProfile(final String id) {
        this.name = "";
        this.canReplaceNA = true;
        this.roamable = true;
        this.needProtected = false;
        this.restrictStates = new ArrayList<String>();
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
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
    
    public List<String> getRestrictStates() {
        return this.restrictStates;
    }
    
    public void setRestrictStates(final List<String> restrictStates) {
        this.restrictStates = restrictStates;
    }
    
    public Long getChannelId() {
        return this.channelId;
    }
    
    public void setChannelId(final Long channelId) {
        this.channelId = channelId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
