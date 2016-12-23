package com.jiam365.flow.base.web;

import java.io.Serializable;

public class WebResponse
        implements Serializable
{
    private static final long serialVersionUID = -5987366255505295857L;
    private String status;
    private String message;
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";

    public static WebResponse success(String message)
    {
        return new WebResponse("success", message);
    }

    public static WebResponse fail(String message)
    {
        return new WebResponse("fail", message);
    }

    public WebResponse() {}

    public WebResponse(String status, String message)
    {
        this.status = status;
        this.message = message;
    }

    public void makeSuccess()
    {
        setStatus("success");
        setMessage("SUCEESS");
    }

    public void makeFail()
    {
        setStatus("fail");
        setMessage("FAIL");
    }

    public String getStatus()
    {
        return this.status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
