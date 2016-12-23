// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dic;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "mobile_state", noClassnameStored = true)
public class State implements Serializable
{
    public static final String NATION_CODE = "NA";
    private static final long serialVersionUID = -1190124209503087261L;
    @Id
    private String code;
    private String name;
    private int sort;
    
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
    
    public int getSort() {
        return this.sort;
    }
    
    public void setSort(final int sort) {
        this.sort = sort;
    }
}
