package com.jiam365.flow.sdk.response;

public abstract interface DataReader
{
  public abstract void init(String paramString);

  public abstract String read(String paramString);

  public abstract void release();
}
