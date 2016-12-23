// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import java.io.Serializable;

public class RestResponse implements Serializable
{
    private static final long serialVersionUID = 6849306454201485238L;
    private String status;
    private String message;
    
    public RestResponse() {
    }
    
    public RestResponse(final String status) {
        this.status = status;
        this.message = Result.msg(status);
    }
    
    public RestResponse(final String status, final String message) {
        this.status = status;
        this.message = message;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatusCascade(final String status) {
        this.status = status;
        this.message = Result.msg(status);
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
}
