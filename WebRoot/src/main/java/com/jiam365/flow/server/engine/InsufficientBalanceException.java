// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

public class InsufficientBalanceException extends RuntimeException
{
    private static final long serialVersionUID = 5951033673630683235L;
    
    public InsufficientBalanceException() {
    }
    
    public InsufficientBalanceException(final String message) {
        super(message);
    }
    
    public InsufficientBalanceException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public InsufficientBalanceException(final Throwable cause) {
        super(cause);
    }
}
