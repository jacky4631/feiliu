// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.channel;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

class ChannelConnectionRegistry
{
    private Map<Long, ChannelConnection> onlineConnections;
    private Map<Long, ChannelConnection> allConnections;
    
    ChannelConnectionRegistry() {
        this.onlineConnections = new ConcurrentHashMap<Long, ChannelConnection>();
        this.allConnections = new ConcurrentHashMap<Long, ChannelConnection>();
    }
    
    public synchronized void register(final ChannelConnection connection) {
        if (0 == connection.getChannel().getStatus()) {
            this.onlineConnections.put(connection.channelId(), connection);
        }
        this.allConnections.put(connection.channelId(), connection);
    }
    
    public synchronized void unRegister(final ChannelConnection connection) {
        this.onlineConnections.remove(connection.channelId());
    }
    
    public synchronized ChannelConnection unRegister(final Long flowChannelId) {
        return this.onlineConnections.remove(flowChannelId);
    }
    
    public ChannelConnection getOnlineConnection(final Long flowChannelId) {
        return this.onlineConnections.get(flowChannelId);
    }
    
    public ChannelConnection getConnection(final Long flowChannelId) {
        return this.allConnections.get(flowChannelId);
    }
    
    public boolean isEmpty() {
        return this.onlineConnections.isEmpty();
    }
    
    public Set<Long> onlineChannles() {
        return Collections.unmodifiableSet((Set<? extends Long>)this.onlineConnections.keySet());
    }
    
    public Map<Long, ChannelConnection> getAllConnections() {
        return this.allConnections;
    }
    
    public boolean isOnline(final long channleId) {
        return this.onlineConnections.get(channleId) != null;
    }
}
