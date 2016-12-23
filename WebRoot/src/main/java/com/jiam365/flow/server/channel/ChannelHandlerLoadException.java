// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.channel;

public class ChannelHandlerLoadException extends Exception
{
    private static final long serialVersionUID = -4829294213698489209L;
    
    public ChannelHandlerLoadException() {
    }
    
    public ChannelHandlerLoadException(final String message) {
        super(message);
    }
    
    public ChannelHandlerLoadException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ChannelHandlerLoadException(final Throwable cause) {
        super(cause);
    }
}
