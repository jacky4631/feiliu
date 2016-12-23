// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import com.jiam365.modules.utils.Identities;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.mongodb.morphia.annotations.Indexed;
import java.util.Date;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;

@Entity(value = "sys_oplog", noClassnameStored = true)
public class OperationLog
{
    @Id
    private String id;
    @Indexed
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date created;
    @Indexed
    private String username;
    @Indexed
    private String description;
    
    public OperationLog() {
        this.id = Identities.uuid2();
        this.created = new Date();
    }
    
    public OperationLog(final String username, final String description) {
        this();
        this.username = username;
        this.description = description;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public Date getCreated() {
        return this.created;
    }
    
    public void setCreated(final Date created) {
        this.created = created;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
}
