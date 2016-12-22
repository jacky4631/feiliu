package com.jiam365.modules.net;

public final class ZFtpClientConfig
{
    private String server;
    private int port = 21;
    private String username;
    private String password;
    private String style = "UNIX";
    private String localEncoding = "UTF-8";
    private String remoteEncoding = "UTF-8";
    private boolean passiveMode = false;
    private boolean binary = true;
    private String rootPath = "";

    public static ZFtpClientConfig create(String server, String username, String password)
    {
        ZFtpClientConfig config = new ZFtpClientConfig();
        config.setServer(server);
        config.setUsername(username);
        config.setPassword(password);
        return config;
    }

    public static ZFtpClientConfig create(String hostname, int port, String username, String password)
    {
        ZFtpClientConfig config = create(hostname, username, password);
        config.setPort(port);
        return config;
    }

    public String getServer()
    {
        return this.server;
    }

    public void setServer(String server)
    {
        this.server = server;
    }

    public int getPort()
    {
        return this.port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getStyle()
    {
        return this.style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    public String getLocalEncoding()
    {
        return this.localEncoding;
    }

    public void setLocalEncoding(String localEncoding)
    {
        this.localEncoding = localEncoding;
    }

    public String getRemoteEncoding()
    {
        return this.remoteEncoding;
    }

    public void setRemoteEncoding(String remoteEncoding)
    {
        this.remoteEncoding = remoteEncoding;
    }

    public boolean isPassiveMode()
    {
        return this.passiveMode;
    }

    public void setPassiveMode(boolean passiveMode)
    {
        this.passiveMode = passiveMode;
    }

    public boolean isBinary()
    {
        return this.binary;
    }

    public void setBinary(boolean binary)
    {
        this.binary = binary;
    }

    public String getRootPath()
    {
        return this.rootPath;
    }

    public void setRootPath(String rootPath)
    {
        this.rootPath = rootPath;
    }
}
