// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "trade_report", noClassnameStored = true)
public class TradeReport implements Serializable
{
    private static final long serialVersionUID = 2707612433958706221L;
    @Id
    private String key;
    private String json;
    @Indexed
    private long created;
    
    public TradeReport() {
        this.created = System.currentTimeMillis();
    }
    
    public TradeReport(final String key, final String json) {
        this();
        this.key = key;
        this.json = json;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public String getJson() {
        return this.json;
    }
    
    public void setJson(final String json) {
        this.json = json;
    }
    
    public long getCreated() {
        return this.created;
    }
    
    public void setCreated(final long created) {
        this.created = created;
    }
}
