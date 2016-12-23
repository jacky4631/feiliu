// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import java.util.Arrays;
import com.jiam365.modules.utils.Identities;
import java.util.ArrayList;
import java.util.List;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;

@Entity(value = "flow_interceptor", noClassnameStored = true)
public class FlowInterceptor
{
    @Id
    private String id;
    private String name;
    private int idx;
    protected List<String> cmcc;
    protected List<String> telecom;
    protected List<String> unicom;
    protected List<String> users;
    protected String productIdPrefix;
    
    public FlowInterceptor() {
        this.idx = 0;
        this.cmcc = new ArrayList<String>();
        this.telecom = new ArrayList<String>();
        this.unicom = new ArrayList<String>();
        this.users = new ArrayList<String>();
        this.id = Identities.uuid2();
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
    
    public int getIdx() {
        return this.idx;
    }
    
    public void setIdx(final int idx) {
        this.idx = idx;
    }
    
    public List<String> getCmcc() {
        return this.cmcc;
    }
    
    public void setCmcc(final List<String> cmcc) {
        this.cmcc = cmcc;
    }
    
    public List<String> getTelecom() {
        return this.telecom;
    }
    
    public void setTelecom(final List<String> telecom) {
        this.telecom = telecom;
    }
    
    public List<String> getUnicom() {
        return this.unicom;
    }
    
    public void setUnicom(final List<String> unicom) {
        this.unicom = unicom;
    }
    
    public List<String> getUsers() {
        return this.users;
    }
    
    public void setUsers(final List<String> users) {
        this.users = users;
    }
    
    public String getProductIdPrefix() {
        return this.productIdPrefix;
    }
    
    public void setProductIdPrefix(final String productIdPrefix) {
        this.productIdPrefix = productIdPrefix;
    }
    
    public String description() {
        final StringBuilder sb = new StringBuilder(256);
        sb.append(" \u6392\u5e8f").append(this.idx);
        sb.append(", \u62e6\u622a ");
        if (this.cmcc.size() > 0) {
            sb.append("\u79fb\u52a8:").append(Arrays.toString(this.cmcc.toArray()));
        }
        if (this.unicom.size() > 0) {
            sb.append("\u79fb\u52a8:").append(Arrays.toString(this.unicom.toArray()));
        }
        if (this.telecom.size() > 0) {
            sb.append("\u79fb\u52a8:").append(Arrays.toString(this.telecom.toArray()));
        }
        return sb.toString();
    }
}
