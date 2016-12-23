// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.product;

import org.mongodb.morphia.annotations.Indexed;
import com.jiam365.modules.telco.Telco;
import org.hibernate.validator.constraints.NotBlank;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "flow_product", noClassnameStored = true)
public class FlowProduct implements Serializable
{
    private static final long serialVersionUID = -3388112879179439495L;
    @Id
    @NotBlank
    private String id;
    private String name;
    private String shortName;
    private double price;
    @Indexed
    private Telco provider;
    @Indexed
    private String scope;
    @Indexed
    private int size;
    private boolean enabled;
    
    public FlowProduct() {
        this.price = 0.0;
        this.size = 10;
        this.enabled = true;
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
    
    public String getShortName() {
        return this.shortName;
    }
    
    public void setShortName(final String shortName) {
        this.shortName = shortName;
    }
    
    public double getPrice() {
        return this.price;
    }
    
    public void setPrice(final double price) {
        this.price = price;
    }
    
    public Telco getProvider() {
        return this.provider;
    }
    
    public void setProvider(final Telco provider) {
        this.provider = provider;
    }
    
    public String getScope() {
        return this.scope;
    }
    
    public void setScope(final String scope) {
        this.scope = scope;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public void setSize(final int size) {
        this.size = size;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
