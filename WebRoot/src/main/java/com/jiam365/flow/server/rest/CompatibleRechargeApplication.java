// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompatibleRechargeApplication
{
    @NotNull
    @Mobile
    private String mobile;
    @NotNull
    private Integer packet;
    private Boolean nationwide;
    private String username;
    
    public CompatibleRechargeApplication() {
        this.nationwide = true;
    }
    
    public void username(final String username) {
        this.username = username;
    }
    
    public String username() {
        return this.username;
    }
    
    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }
    
    public Integer getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Integer packet) {
        this.packet = packet;
    }
    
    public Boolean getNationwide() {
        return this.nationwide;
    }
    
    public void setNationwide(final Boolean nationwide) {
        this.nationwide = nationwide;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    @Override
    public String toString() {
        return "{mobile='" + this.mobile + '\'' + ", packet=" + this.packet + ", nationwide=" + this.nationwide + '}';
    }
}
