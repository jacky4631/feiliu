// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.channel;

import java.util.concurrent.ConcurrentHashMap;
import com.jiam365.flow.sdk.ChannelHandler;
import java.util.Map;

public final class HandlerContainer
{
    private static Map<Long, ChannelHandler> handlers;
    
    public static synchronized ChannelHandler fetch(final long channelId, final ConnectionParam param) throws ChannelHandlerLoadException {
        ChannelHandler handler = HandlerContainer.handlers.get(channelId);
        if (handler == null) {
            handler = load(channelId, param.getHandlerClass(), param.getParamJson());
        }
        return handler;
    }
    
    public static synchronized void clear(final long channelId) {
        HandlerContainer.handlers.remove(channelId);
    }
    
    private static ChannelHandler load(final long channelId, final String className, final String paramJson) throws ChannelHandlerLoadException {
        try {
            final Class clazz = Class.forName(className);
            final ChannelHandler command = (ChannelHandler)clazz.newInstance();
            if (paramJson != null) {
                command.loadParams(paramJson);
            }
            HandlerContainer.handlers.put(channelId, command);
            return command;
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new ChannelHandlerLoadException(e);
        }
    }
    
    static {
        HandlerContainer.handlers = new ConcurrentHashMap<Long, ChannelHandler>();
    }
}
