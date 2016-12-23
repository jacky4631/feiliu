// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.channel;

import org.slf4j.LoggerFactory;
import com.jiam365.flow.server.product.FlowPackage;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import com.jiam365.flow.server.dao.DbException;
import java.util.Collections;
import java.util.List;
import com.jiam365.flow.server.dao.FlowPackageDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.FlowChannelDao;
import org.slf4j.Logger;

public class ChannelConnectionManager
{
    private static Logger logger;
    private ChannelConnectionRegistry registry;
    @Autowired
    private FlowChannelDao flowChannelDao;
    @Autowired
    private FlowPackageDao flowPackageDao;
    
    public ChannelConnection pickOnlineChannleConnection(final long channelId) {
        return this.registry.getOnlineConnection(channelId);
    }
    
    public ChannelConnection getConnection(final long channelId) {
        return this.registry.getConnection(channelId);
    }
    
    public boolean isOnline(final long channelId) {
        return this.registry.isOnline(channelId);
    }
    
    public List<FlowChannel> findChannels() {
        try {
            return this.flowChannelDao.findChannels();
        }
        catch (DbException e) {
            ChannelConnectionManager.logger.error("Cann't load flowchannels, {}", (Object)e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public void init() {
        this.registry = new ChannelConnectionRegistry();
        ChannelConnectionManager.logger.info("开始注册已配置通道");
        final List<FlowChannel> channels = this.findChannels();
        channels.forEach(this::activeChannel);
        ChannelConnectionManager.logger.info("{}个供应商已经被注册", (Object)channels.size());
    }
    
    public void shutdown() {
        final Map<Long, ChannelConnection> connections = this.registry.getAllConnections();
        for (final Long id : connections.keySet()) {
            final ChannelConnection connection = connections.get(id);
            ChannelConnectionManager.logger.debug("{} unregistered", (Object)connection.getChannel().getName());
            connection.destroy();
        }
    }
    
    public void insertChannel(final FlowChannel flowChannel) {
        this.flowChannelDao.insertNew(flowChannel);
    }
    
    public void updateChannel(final FlowChannel channel) {
        if (channel.getId() == null) {
            throw new RuntimeException("Invalid update with a new channel");
        }
        this.flowChannelDao.save(channel);
    }
    
    public void updateStatus(final long channleId, final int status) {
        this.flowChannelDao.updateChannelStatus(channleId, status);
    }
    
    public void activeChannel(final FlowChannel channel) {
        if (!channel.configCompleted()) {
            return;
        }
        final ChannelConnection connection = new ChannelConnection(channel);
        try {
            connection.init();
        }
        catch (ChannelRegisterException e) {
            ChannelConnectionManager.logger.error("acitive channel {} error, {}", (Object)channel.getName(), (Object)e.getMessage());
            return;
        }
        this.registry.register(connection);
        if (0 == channel.getStatus()) {
            ChannelConnectionManager.logger.debug("{} registered as online", (Object)channel.getName());
        }
    }
    
    public void activeChannel(final Long flowChannelId) {
        final FlowChannel channel = (FlowChannel)this.flowChannelDao.get(flowChannelId);
        if (channel != null) {
            this.activeChannel(channel);
        }
    }
    
    public void deActiveChannel(final Long flowChannelId) {
        final ChannelConnection connection = this.registry.unRegister(flowChannelId);
        if (connection != null) {
            ChannelConnectionManager.logger.debug("{} unregistered as offline", (Object)connection.getChannel().getName());
        }
    }
    
    public FlowPackage getFlowPackage(final long flowChannelId, final int packetSize) {
        return this.flowPackageDao.getFlowPackage(Long.valueOf(flowChannelId), packetSize);
    }
    
    public FlowPackage getFlowPackage(final String id) {
        return (FlowPackage)this.flowPackageDao.get(id);
    }
    
    public List<FlowPackage> findFlowPackages(final long flowChannelId) {
        return this.flowPackageDao.findFlowPackages(flowChannelId);
    }
    
    static {
        ChannelConnectionManager.logger = LoggerFactory.getLogger((Class)ChannelConnectionManager.class);
    }
}
