// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

public class DbException extends RuntimeException
{
    private static final long serialVersionUID = -7632316740287456049L;
    
    public DbException() {
    }
    
    public DbException(final String message) {
        super(message);
    }
    
    public DbException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DbException(final Throwable cause) {
        super(cause);
    }
}
