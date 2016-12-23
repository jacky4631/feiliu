// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.channel;

import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class ConnectionParam
{
    @Indexed
    private String handlerClass;
    private String paramJson;
    
    public ConnectionParam() {
        this.handlerClass = "";
        this.paramJson = "";
    }
    
    public String getHandlerClass() {
        return this.handlerClass;
    }
    
    public void setHandlerClass(final String handlerClass) {
        this.handlerClass = handlerClass;
    }
    
    public String getParamJson() {
        return this.paramJson;
    }
    
    public void setParamJson(final String paramJson) {
        if (paramJson != null) {
            final String v = paramJson.replaceAll("\r|\n", "");
            this.paramJson = v.replaceAll(" ", "");
        }
        else {
            this.paramJson = null;
        }
    }
}
