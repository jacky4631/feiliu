// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.utils.Identities;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_blacklist", noClassnameStored = true)
public class Blacklist implements Serializable
{
    private static final long serialVersionUID = -2243599916210048969L;
    @Id
    private String id;
    private String mobiles;
    private boolean enableDynamic;
    private int allowFailTimes;
    private int monitorPeriod;
    
    public Blacklist() {
        this.enableDynamic = true;
        this.allowFailTimes = 2;
        this.monitorPeriod = 24;
        this.id = Identities.uuid2();
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getMobiles() {
        return this.mobiles;
    }
    
    public void setMobiles(final String mobiles) {
        final String cnComma = "\uff0c";
        this.mobiles = ((mobiles == null) ? "" : StringUtils.deleteWhitespace(mobiles.replaceAll("\uff0c", ",")));
    }
    
    public boolean isEnableDynamic() {
        return this.enableDynamic;
    }
    
    public void setEnableDynamic(final boolean enableDynamic) {
        this.enableDynamic = enableDynamic;
    }
    
    public int getAllowFailTimes() {
        return this.allowFailTimes;
    }
    
    public void setAllowFailTimes(final int allowFailTimes) {
        this.allowFailTimes = allowFailTimes;
    }
    
    public int getMonitorPeriod() {
        return this.monitorPeriod;
    }
    
    public void setMonitorPeriod(final int monitorPeriod) {
        this.monitorPeriod = monitorPeriod;
    }
}
