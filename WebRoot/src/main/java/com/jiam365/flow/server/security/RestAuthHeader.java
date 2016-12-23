// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.security;

public class RestAuthHeader
{
    private String username;
    private String timestamp;
    private String sign;
    
    public RestAuthHeader(final String username, final String timestamp, final String sign) {
        this.username = username;
        this.timestamp = timestamp;
        this.sign = sign;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getSign() {
        return this.sign;
    }
    
    public void setSign(final String sign) {
        this.sign = sign;
    }
}
