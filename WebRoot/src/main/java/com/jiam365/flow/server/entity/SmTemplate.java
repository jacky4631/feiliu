// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_smtemplate", noClassnameStored = true)
public class SmTemplate implements Serializable
{
    private static final long serialVersionUID = 2240169998088918120L;
    @Id
    private Long userId;
    private boolean sendSms;
    private String content;
    
    public SmTemplate() {
        this.sendSms = false;
    }
    
    public SmTemplate(final long userId) {
        this();
        this.userId = userId;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    
    public void setUserId(final Long userId) {
        this.userId = userId;
    }
    
    public boolean isSendSms() {
        return this.sendSms;
    }
    
    public void setSendSms(final boolean sendSms) {
        this.sendSms = sendSms;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
}
