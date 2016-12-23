package com.jiam365.flow.sdk;

import com.jiam365.flow.sdk.response.ResponseData;

public abstract interface ChannelHandler
{
  public abstract String getParamTemplate();

  public abstract void loadParams(String paramString);

  public abstract void init();

  public abstract void release();

  public abstract ResponseData recharge(RechargeRequest paramRechargeRequest)
          throws ChannelConnectionException;

  public abstract ResponseData queryReport(RechargeRequest paramRechargeRequest, String paramString)
          throws ChannelConnectionException;
}
