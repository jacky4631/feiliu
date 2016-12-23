// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.event;

import com.jiam365.flow.server.entity.OperationLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.OperationLogManager;
import org.springframework.stereotype.Component;
import org.springframework.context.event.SmartApplicationListener;

@Component
public class SystemEventListener implements SmartApplicationListener
{
    @Autowired
    private OperationLogManager operationLogManager;
    
    public boolean supportsEventType(final Class<? extends ApplicationEvent> eventType) {
        return eventType == SystemEvent.class;
    }
    
    public boolean supportsSourceType(final Class<?> sourceType) {
        return sourceType == OperationLog.class;
    }
    
    public void onApplicationEvent(final ApplicationEvent event) {
        final Object source = event.getSource();
        if (source instanceof OperationLog) {
            this.operationLogManager.save((OperationLog)source);
        }
    }
    
    public int getOrder() {
        return 0;
    }
}
