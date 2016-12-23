// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

public class RestException extends RuntimeException
{
    private static final long serialVersionUID = 2589796094144076438L;
    
    public RestException() {
    }
    
    public RestException(final String message) {
        super(message);
    }
    
    public RestException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public RestException(final Throwable cause) {
        super(cause);
    }
}
