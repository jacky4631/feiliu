package com.jiam365.flow.sdk;

public class ChannelRateLimitException
        extends ChannelConnectionException
{
    private int suggestDelaySeconds = 60;
    private static final long serialVersionUID = -8331788373090709174L;

    public ChannelRateLimitException() {}

    public ChannelRateLimitException(String message)
    {
        super(message, new boolean[0]);
    }

    public ChannelRateLimitException(String message, Throwable cause)
    {
        super(message, cause, new boolean[0]);
    }

    public ChannelRateLimitException(Throwable cause)
    {
        super(cause, new boolean[0]);
    }

    public int getSuggestDelaySeconds()
    {
        return this.suggestDelaySeconds;
    }

    public void setSuggestDelaySeconds(int suggestDelaySeconds)
    {
        this.suggestDelaySeconds = suggestDelaySeconds;
    }
}
