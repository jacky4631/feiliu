// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import com.jiam365.modules.utils.Identities;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_smmemo", noClassnameStored = true)
public class SmMemo implements Serializable
{
    private static final long serialVersionUID = -1382131239886841007L;
    @Id
    private String id;
    private String owner;
    private String spcode;
    private String smTemplate;
    private String desc;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created;
    private String creator;
    
    public SmMemo() {
        this.id = Identities.uuid2();
        this.created = new Date();
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    public void setOwner(final String owner) {
        this.owner = owner;
    }
    
    public String getSmTemplate() {
        return this.smTemplate;
    }
    
    public void setSmTemplate(final String smTemplate) {
        this.smTemplate = smTemplate;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    public String getSpcode() {
        return this.spcode;
    }
    
    public void setSpcode(final String spcode) {
        this.spcode = spcode;
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
}
