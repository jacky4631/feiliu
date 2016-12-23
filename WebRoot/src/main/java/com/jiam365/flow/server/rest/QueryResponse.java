// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

public class QueryResponse extends RestResponse
{
    private static final long serialVersionUID = -509595120054180231L;
    private String reqNo;
    
    public QueryResponse() {
        this.reqNo = "";
    }
    
    public String getReqNo() {
        return this.reqNo;
    }
    
    public void setReqNo(final String reqNo) {
        this.reqNo = reqNo;
    }
}
