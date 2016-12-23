// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

public class TradeException extends RuntimeException
{
    private static final long serialVersionUID = -7632316740287456049L;
    
    public TradeException() {
    }
    
    public TradeException(final String message) {
        super(message);
    }
    
    public TradeException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public TradeException(final Throwable cause) {
        super(cause);
    }
}
