// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.channel;

import com.jiam365.flow.sdk.ChannelHandler;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.RechargeRequest;
import java.util.Objects;

public class ChannelConnection
{
    protected FlowChannel channel;
    private ConnectionParam param;
    
    public Long channelId() {
        return this.channel.getId();
    }
    
    public FlowChannel getChannel() {
        return this.channel;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChannelConnection)) {
            return false;
        }
        final ChannelConnection that = (ChannelConnection)o;
        return Objects.equals(this.channelId(), that.channelId());
    }
    
    @Override
    public int hashCode() {
        if (this.channelId() != null) {
            return this.channelId().hashCode();
        }
        return super.hashCode();
    }
    
    public ChannelConnection(final FlowChannel channel) {
        this.channel = channel;
        this.param = channel.getChannelConnectionParam();
    }
    
    public ConnectionParam getParam() {
        return this.param;
    }
    
    public ResponseData recharge(final RechargeRequest rechargeRequest) throws ChannelConnectionException {
        try {
            final ChannelHandler command = HandlerContainer.fetch(this.channel.getId(), this.param);
            return command.recharge(rechargeRequest);
        }
        catch (ChannelHandlerLoadException e) {
            throw new ChannelConnectionException(this.param.getHandlerClass() + " " + e.getCause().getMessage(), (Throwable)e, new boolean[] { false });
        }
    }
    
    public ResponseData queryReport(final RechargeRequest rechargeRequest, final String reqNo) throws ChannelConnectionException {
        try {
            final ChannelHandler command = HandlerContainer.fetch(this.channel.getId(), this.param);
            return command.queryReport(rechargeRequest, reqNo);
        }
        catch (ChannelHandlerLoadException e) {
            throw new ChannelConnectionException(this.param.getHandlerClass() + " " + e.getCause().getMessage(), (Throwable)e, new boolean[] { false });
        }
    }
    
    public void init() throws ChannelRegisterException {
        try {
            final Class clazz = Class.forName(this.param.getHandlerClass());
            final ChannelHandler command = (ChannelHandler)clazz.newInstance();
            command.init();
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new ChannelRegisterException("Plugin " + this.param.getHandlerClass() + " exception.", e);
        }
        catch (Exception ex3) {}
    }
    
    public void destroy() {
        try {
            final Class clazz = Class.forName(this.param.getHandlerClass());
            final ChannelHandler command = (ChannelHandler)clazz.newInstance();
            command.release();
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new ChannelConnectionException("Custom class" + this.param.getHandlerClass() + " exception.", (Throwable)e, new boolean[] { false });
        }
    }
}
