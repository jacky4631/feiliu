package com.jiam365.modules.persistent;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MongoClientFactory
{
    private String host;
    private int port;
    private String connectStr;
    private String description;
    private int connectionsPerHost = 100;
    private int threadsAllowedToBlockForConnectionMultiplier = 5;
    private int maxWaitTime = 120000;
    private int connectTimeout = 10000;
    private int socketTimeout = 0;
    private boolean socketKeepAlive = false;
    private boolean cursorFinalizerEnabled = true;
    private boolean alwaysUseMBeans = false;
    private boolean salveOk = true;

    public MongoClientFactory() {}

    public MongoClientFactory(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public MongoClient create()
            throws UnknownHostException
    {
        MongoClientOptions.Builder builder = MongoClientOptions.builder();
        builder
                .description(this.description)
                .connectionsPerHost(this.connectionsPerHost)
                .threadsAllowedToBlockForConnectionMultiplier(this.threadsAllowedToBlockForConnectionMultiplier)
                .maxWaitTime(this.maxWaitTime)
                .connectTimeout(this.connectTimeout)
                .socketTimeout(this.socketTimeout)
                .socketKeepAlive(this.socketKeepAlive)
                .cursorFinalizerEnabled(this.cursorFinalizerEnabled)
                .readPreference(this.salveOk ? ReadPreference.secondaryPreferred() : ReadPreference.primaryPreferred())
                .alwaysUseMBeans(this.alwaysUseMBeans);

        List<ServerAddress> addressList = new ArrayList();
        if (this.connectStr == null)
        {
            addressList.add(new ServerAddress(this.host, this.port));
        }
        else
        {
            String[] servers = this.connectStr.split(",");
            for (String server : servers)
            {
                String[] params = server.split(":");

                int port = params.length == 1 ? 27017 : Integer.parseInt(params[1].trim());
                addressList.add(new ServerAddress(params[0], port));
            }
        }
        return new MongoClient(addressList, builder.build());
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getConnectionsPerHost()
    {
        return this.connectionsPerHost;
    }

    public void setConnectionsPerHost(int connectionsPerHost)
    {
        this.connectionsPerHost = connectionsPerHost;
    }

    public int getThreadsAllowedToBlockForConnectionMultiplier()
    {
        return this.threadsAllowedToBlockForConnectionMultiplier;
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier)
    {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    public int getMaxWaitTime()
    {
        return this.maxWaitTime;
    }

    public void setMaxWaitTime(int maxWaitTime)
    {
        this.maxWaitTime = maxWaitTime;
    }

    public int getConnectTimeout()
    {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout)
    {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout()
    {
        return this.socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout)
    {
        this.socketTimeout = socketTimeout;
    }

    public boolean isSocketKeepAlive()
    {
        return this.socketKeepAlive;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive)
    {
        this.socketKeepAlive = socketKeepAlive;
    }

    public boolean isCursorFinalizerEnabled()
    {
        return this.cursorFinalizerEnabled;
    }

    public void setCursorFinalizerEnabled(boolean cursorFinalizerEnabled)
    {
        this.cursorFinalizerEnabled = cursorFinalizerEnabled;
    }

    public boolean isAlwaysUseMBeans()
    {
        return this.alwaysUseMBeans;
    }

    public void setAlwaysUseMBeans(boolean alwaysUseMBeans)
    {
        this.alwaysUseMBeans = alwaysUseMBeans;
    }

    public String getHost()
    {
        return this.host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getPort()
    {
        return this.port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getConnectStr()
    {
        return this.connectStr;
    }

    public void setConnectStr(String connectStr)
    {
        this.connectStr = connectStr;
    }

    public boolean isSalveOk()
    {
        return this.salveOk;
    }

    public void setSalveOk(boolean salveOk)
    {
        this.salveOk = salveOk;
    }
}
