// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.event;

import org.springframework.context.ApplicationEvent;

public class RechargeFailEvent extends ApplicationEvent
{
    private static final long serialVersionUID = 6464972732444711178L;
    
    public RechargeFailEvent(final String mobile) {
        super((Object)mobile);
    }
}
