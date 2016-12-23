// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

public class CompatibleRechargeResponse extends RestResponse
{
    private static final long serialVersionUID = -3547283740752648443L;
    private String serial;
    
    public CompatibleRechargeResponse() {
    }
    
    public CompatibleRechargeResponse(final String status, final String message, final String serial) {
        super(status, message);
        this.serial = serial;
    }
    
    public String getSerial() {
        return this.serial;
    }
    
    public void setSerial(final String serial) {
        this.serial = serial;
    }
}
