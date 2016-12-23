// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import com.jiam365.modules.tools.SpringContext;
import com.jiam365.flow.server.params.ParamsService;
import com.jiam365.flow.server.params.TimeoutParam;

public final class TimeoutPolicy
{
    public static boolean isAutoProcess() {
        final TimeoutParam param = timeoutParam();
        return param.isAutoProcess();
    }
    
    public static int timeoutSeconds() {
        final TimeoutParam param = timeoutParam();
        return param.getTimeout() * 3600;
    }
    
    public static boolean result() {
        final TimeoutParam param = timeoutParam();
        final String strategy = param.getTimeOutStrategy();
        return "success".equalsIgnoreCase(strategy);
    }
    
    private static TimeoutParam timeoutParam() {
        final ParamsService service = (ParamsService)SpringContext.getBean((Class)ParamsService.class);
        return service.loadTimeoutParam();
    }
}
