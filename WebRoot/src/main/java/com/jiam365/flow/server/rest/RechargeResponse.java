// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

public class RechargeResponse extends RestResponse
{
    private static final long serialVersionUID = -3547283740752648443L;
    private String reqNo;
    
    public RechargeResponse() {
        this.reqNo = "";
    }
    
    public RechargeResponse(final String status) {
        super(status);
        this.reqNo = "";
    }
    
    public RechargeResponse(final String status, final String message) {
        super(status, message);
        this.reqNo = "";
    }
    
    public RechargeResponse(final String status, final String message, final String reqNo) {
        super(status, message);
        this.reqNo = "";
        this.reqNo = reqNo;
    }
    
    public String getReqNo() {
        return this.reqNo;
    }
    
    public void setReqNo(final String reqNo) {
        this.reqNo = reqNo;
    }
}
