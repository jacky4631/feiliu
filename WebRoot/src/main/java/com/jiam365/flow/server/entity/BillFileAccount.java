// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import org.apache.commons.lang3.StringUtils;

public class BillFileAccount
{
    private String protocol;
    private String hostname;
    private Integer port;
    private String username;
    private String password;
    private boolean passiveMode;
    private String remoteEncoding;
    private String style;
    private String rootPath;
    
    public BillFileAccount() {
        this.passiveMode = false;
        this.remoteEncoding = "UTF-8";
        this.style = "UNIX";
    }
    
    public boolean isValid() {
        return "FTP".equals(this.protocol) && StringUtils.isNotBlank((CharSequence)this.username) && StringUtils.isNotBlank((CharSequence)this.password);
    }
    
    public String getProtocol() {
        return this.protocol;
    }
    
    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }
    
    public String getHostname() {
        return this.hostname;
    }
    
    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }
    
    public int getPort() {
        return (this.port == null || this.port.equals(0)) ? this.defaultPort() : this.port;
    }
    
    public void setPort(final Integer port) {
        this.port = port;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public boolean isPassiveMode() {
        return this.passiveMode;
    }
    
    public void setPassiveMode(final boolean passiveMode) {
        this.passiveMode = passiveMode;
    }
    
    public String getRemoteEncoding() {
        return this.remoteEncoding;
    }
    
    public void setRemoteEncoding(final String remoteEncoding) {
        this.remoteEncoding = remoteEncoding;
    }
    
    public String getStyle() {
        return this.style;
    }
    
    public void setStyle(final String style) {
        this.style = style;
    }
    
    public String getRootPath() {
        return this.rootPath;
    }
    
    public void setRootPath(final String rootPath) {
        this.rootPath = rootPath;
    }
    
    private int defaultPort() {
        if ("FTP".equalsIgnoreCase(this.protocol)) {
            return 21;
        }
        if ("SFTP".equalsIgnoreCase(this.protocol)) {
            return 22;
        }
        return 80;
    }
}
