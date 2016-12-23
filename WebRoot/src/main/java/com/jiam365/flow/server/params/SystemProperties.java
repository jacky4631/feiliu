// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.params;

import com.jiam365.modules.tools.ConfigurationHolder;

public class SystemProperties
{
    public static boolean isDebug() {
        return ConfigurationHolder.properties().getBoolean("debug", false);
    }
}
