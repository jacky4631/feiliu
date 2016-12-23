// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import com.jiam365.modules.utils.Identities;
import java.util.ArrayList;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_notify", noClassnameStored = true)
public class BalanceNotify implements Serializable
{
    private static final long serialVersionUID = 7517837829621834753L;
    @Id
    private String id;
    private String title;
    private List<Long> channels;
    private double threshold;
    private String mobiles;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
    private boolean status;
    
    public BalanceNotify() {
        this.channels = new ArrayList<Long>();
        this.status = true;
        this.id = Identities.uuid2();
        this.created = new Date();
    }
    
    public static long getSerialVersionUID() {
        return 7517837829621834753L;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public List<Long> getChannels() {
        return this.channels;
    }
    
    public void setChannels(final List<Long> channels) {
        this.channels = channels;
    }
    
    public boolean isStatus() {
        return this.status;
    }
    
    public void setStatus(final boolean status) {
        this.status = status;
    }
    
    public double getThreshold() {
        return this.threshold;
    }
    
    public void setThreshold(final double threshold) {
        this.threshold = threshold;
    }
    
    public String getMobiles() {
        return this.mobiles;
    }
    
    public void setMobiles(final String mobiles) {
        this.mobiles = mobiles;
    }
    
    public Date getCreated() {
        return this.created;
    }
    
    public void setCreated(final Date created) {
        this.created = created;
    }
}
