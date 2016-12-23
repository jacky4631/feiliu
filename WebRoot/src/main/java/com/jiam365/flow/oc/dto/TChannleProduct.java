// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.dto;

public class TChannleProduct
{
    private String id;
    private String name;
    
    public TChannleProduct(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
