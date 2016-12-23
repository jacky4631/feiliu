// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.channel;

public class ChannelRegisterException extends Exception
{
    private static final long serialVersionUID = -2318318798125326347L;
    
    public ChannelRegisterException() {
    }
    
    public ChannelRegisterException(final String message) {
        super(message);
    }
    
    public ChannelRegisterException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ChannelRegisterException(final Throwable cause) {
        super(cause);
    }
}
