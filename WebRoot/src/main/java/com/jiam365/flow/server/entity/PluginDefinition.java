// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import com.jiam365.modules.utils.Identities;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_plugin", noClassnameStored = true)
public class PluginDefinition implements Serializable
{
    private static final long serialVersionUID = 8971813355143562834L;
    @Id
    private String id;
    private String handlerClass;
    @Indexed
    private String title;
    private String callbackUrl;
    private String version;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
    private String author;
    private String description;
    
    public PluginDefinition() {
        this.id = Identities.uuid2();
        this.created = new Date();
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getHandlerClass() {
        return this.handlerClass;
    }
    
    public void setHandlerClass(final String handlerClass) {
        this.handlerClass = handlerClass;
    }
    
    public String getCallbackUrl() {
        return this.callbackUrl;
    }
    
    public void setCallbackUrl(final String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(final String version) {
        this.version = version;
    }
    
    public Date getCreated() {
        return this.created;
    }
    
    public void setCreated(final Date created) {
        this.created = created;
    }
    
    public String getAuthor() {
        return this.author;
    }
    
    public void setAuthor(final String author) {
        this.author = author;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
}
