// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.event;

import com.jiam365.flow.server.entity.OperationLog;
import org.springframework.context.ApplicationEvent;

public class SystemEvent extends ApplicationEvent
{
    private static final long serialVersionUID = -5104761489442568322L;
    
    public SystemEvent(final String username, final String description) {
        this(new OperationLog(username, description));
    }
    
    public SystemEvent(final OperationLog source) {
        super((Object)source);
    }
}
