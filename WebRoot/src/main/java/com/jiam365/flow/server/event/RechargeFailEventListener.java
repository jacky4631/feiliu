// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.engine.FailTimesLimitator;
import org.springframework.stereotype.Component;
import org.springframework.context.event.SmartApplicationListener;

@Component
public class RechargeFailEventListener implements SmartApplicationListener
{
    @Autowired
    private FailTimesLimitator failTimesLimitator;
    
    public boolean supportsEventType(final Class<? extends ApplicationEvent> eventType) {
        return eventType == RechargeFailEvent.class;
    }
    
    public boolean supportsSourceType(final Class<?> sourceType) {
        return sourceType == String.class;
    }
    
    public void onApplicationEvent(final ApplicationEvent event) {
        final Object source = event.getSource();
        if (source instanceof String) {
            this.failTimesLimitator.addFailMobile((String)source);
        }
    }
    
    public int getOrder() {
        return 0;
    }
}
