// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.utils.Identities;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_smconfig", noClassnameStored = true)
public class SmConfig implements Serializable
{
    private static final long serialVersionUID = -4426600807702870924L;
    @Id
    private String id;
    private boolean sendSm;
    private String disabledForTelco;
    private String template;
    private String smsApiUrl;
    private String smsAccount;
    private String smsPass;
    
    public SmConfig() {
        this.sendSm = false;
        this.disabledForTelco = "";
        this.id = Identities.uuid2();
    }
    
    public boolean disabledTelco(final String telcoId) {
        return this.disabledForTelco.contains(telcoId);
    }
    
    public boolean isValid() {
        return this.sendSm && StringUtils.isNotBlank((CharSequence)this.smsApiUrl) && StringUtils.isNotBlank((CharSequence)this.smsAccount) && StringUtils.isNotBlank((CharSequence)this.smsPass);
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public boolean isSendSm() {
        return this.sendSm;
    }
    
    public void setSendSm(final boolean sendSm) {
        this.sendSm = sendSm;
    }
    
    public String getTemplate() {
        return this.template;
    }
    
    public void setTemplate(final String template) {
        this.template = template;
    }
    
    public String getSmsApiUrl() {
        return this.smsApiUrl;
    }
    
    public void setSmsApiUrl(final String smsApiUrl) {
        this.smsApiUrl = smsApiUrl;
    }
    
    public String getSmsAccount() {
        return this.smsAccount;
    }
    
    public void setSmsAccount(final String smsAccount) {
        this.smsAccount = smsAccount;
    }
    
    public String getSmsPass() {
        return this.smsPass;
    }
    
    public void setSmsPass(final String smsPass) {
        this.smsPass = smsPass;
    }
    
    public String getDisabledForTelco() {
        return this.disabledForTelco;
    }
    
    public void setDisabledForTelco(final String disabledForTelco) {
        this.disabledForTelco = disabledForTelco;
    }
}
