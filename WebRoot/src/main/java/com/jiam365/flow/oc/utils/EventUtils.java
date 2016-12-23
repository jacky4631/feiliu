// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.utils;

import org.springframework.context.ApplicationEvent;
import com.jiam365.modules.tools.SpringContext;
import com.jiam365.flow.server.event.SystemEvent;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.base.utils.ShiroUtils;

public class EventUtils
{
    public static void publishLogEvent(final String message) {
        final String username = ShiroUtils.currentUsername();
        SpringContext.fireEvent((ApplicationEvent)new SystemEvent(StringUtils.isBlank((CharSequence)username) ? "System" : username, message));
    }
}
