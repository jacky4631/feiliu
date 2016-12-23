// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import com.jiam365.modules.utils.Identities;
import java.util.LinkedList;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_channelgroup", noClassnameStored = true)
public class ChannelGroup implements Serializable
{
    private static final long serialVersionUID = -8631348825772684161L;
    @Id
    private String id;
    private String title;
    private List<String> allowChannelProducts;
    private String desc;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created;
    private String creator;
    
    public ChannelGroup() {
        this.allowChannelProducts = new LinkedList<String>();
        this.id = Identities.uuid2();
        this.created = new Date();
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
    
    public String getDesc() {
        return this.desc;
    }
    
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    public Date getCreated() {
        return this.created;
    }
    
    public void setCreated(final Date created) {
        this.created = created;
    }
    
    public String getCreator() {
        return this.creator;
    }
    
    public void setCreator(final String creator) {
        this.creator = creator;
    }
    
    public String[] getAllowChannelProducts() {
        if (this.allowChannelProducts.size() > 0) {
            final String[] array = new String[this.allowChannelProducts.size()];
            return this.allowChannelProducts.toArray(array);
        }
        return new String[0];
    }
    
    public List<String> getAllowChannelProductList() {
        return this.allowChannelProducts;
    }
    
    public void setAllowChannelProducts(final List<String> allowChannelProducts) {
        this.allowChannelProducts = allowChannelProducts;
    }
}
