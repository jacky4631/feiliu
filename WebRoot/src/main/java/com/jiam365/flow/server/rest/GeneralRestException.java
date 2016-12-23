// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

public class GeneralRestException extends RestException
{
    private static final long serialVersionUID = -4496552292891550390L;
    private String resultCode;
    private String resultMessage;
    
    public GeneralRestException() {
    }
    
    public GeneralRestException(final String code) {
        this.resultCode = code;
        this.resultMessage = Result.msg(this.resultCode);
    }
    
    public GeneralRestException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public GeneralRestException(final Throwable cause) {
        super(cause);
    }
    
    public String getResultCode() {
        return this.resultCode;
    }
    
    public void setResultCode(final String resultCode) {
        this.resultCode = resultCode;
    }
    
    public String getResultMessage() {
        return this.resultMessage;
    }
    
    public void setResultMessage(final String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
