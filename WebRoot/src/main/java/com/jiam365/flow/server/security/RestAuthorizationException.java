// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.security;

import com.jiam365.flow.server.rest.RestException;

public class RestAuthorizationException extends RestException
{
    private static final long serialVersionUID = 2589796094144076438L;
    
    public RestAuthorizationException() {
    }
    
    public RestAuthorizationException(final String message) {
        super(message);
    }
    
    public RestAuthorizationException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public RestAuthorizationException(final Throwable cause) {
        super(cause);
    }
}
