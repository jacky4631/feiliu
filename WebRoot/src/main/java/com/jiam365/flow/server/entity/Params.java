// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_params", noClassnameStored = true)
public class Params implements Serializable
{
    private static final long serialVersionUID = -2815902035844097319L;
    @Id
    private String id;
    private String objJson;
    
    public Params() {
    }
    
    public Params(final String id, final String objJson) {
        this.id = id;
        this.objJson = objJson;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getObjJson() {
        return this.objJson;
    }
    
    public void setObjJson(final String objJson) {
        this.objJson = objJson;
    }
}
