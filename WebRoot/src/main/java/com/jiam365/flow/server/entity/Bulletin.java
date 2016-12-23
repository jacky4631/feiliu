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

@Entity(value = "sys_bulletin", noClassnameStored = true)
public class Bulletin implements Serializable
{
    private static final long serialVersionUID = -5415793714813127790L;
    @Id
    private String id;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
    
    public Bulletin() {
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
    
    public String getContent() {
        return this.content;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    public Date getCreated() {
        return this.created;
    }
    
    public void setCreated(final Date created) {
        this.created = created;
    }
}
