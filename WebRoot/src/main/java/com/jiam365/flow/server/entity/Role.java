// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import com.jiam365.modules.utils.Identities;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_role", noClassnameStored = true)
public class Role implements Serializable
{
    private static final long serialVersionUID = -8138182557579650385L;
    @Id
    private String id;
    private String code;
    private String name;
    private String description;
    
    public Role() {
        this.id = Identities.uuid2();
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
}
