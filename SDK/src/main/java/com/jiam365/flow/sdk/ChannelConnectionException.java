package com.jiam365.flow.sdk;

public class ChannelConnectionException
        extends RuntimeException
{
    private static final long serialVersionUID = -7632316740287456049L;
    private boolean isTemporary = true;

    public ChannelConnectionException() {}

    public ChannelConnectionException(String message, boolean... isTemporary)
    {
        super(message);
        if (isTemporary.length > 0) {
            this.isTemporary = isTemporary[0];
        }
    }

    public ChannelConnectionException(String message, Throwable cause, boolean... isTemporary)
    {
        super(message, cause);
        if (isTemporary.length > 0) {
            this.isTemporary = isTemporary[0];
        }
    }

    public ChannelConnectionException(Throwable cause, boolean... isTemporary)
    {
        super(cause);
        if (isTemporary.length > 0) {
            this.isTemporary = isTemporary[0];
        }
    }

    public boolean isTemporary()
    {
        return this.isTemporary;
    }
}
