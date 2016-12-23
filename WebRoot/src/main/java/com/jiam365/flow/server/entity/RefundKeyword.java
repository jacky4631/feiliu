// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import java.util.HashSet;
import java.util.Set;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_refundkeyword", noClassnameStored = true)
public class RefundKeyword implements Serializable
{
    private static final long serialVersionUID = 6366464908064727229L;
    @Id
    private String id;
    private Set<String> keywords;
    
    public RefundKeyword() {
        this.keywords = new HashSet<String>();
    }
    
    public void addKeyword(final String keyword) {
        this.keywords.add(keyword);
    }
    
    public void removeKeyword(final String keyword) {
        this.keywords.remove(keyword);
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public Set<String> getKeywords() {
        return this.keywords;
    }
    
    public void setKeywords(final Set<String> keywords) {
        this.keywords = keywords;
    }
}
