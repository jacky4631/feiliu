package com.jiam365.flow.sdk;

import com.jiam365.modules.telco.Telco;

public class MobileInfo
{
    private Telco provider;
    private String stateName = "";
    private String stateCode = "";
    private String area = "";
    private String mobileType = "";

    public String getMobileDetail()
    {
        String providerName = this.provider == null ? "未知" : this.provider.getName();
        return this.stateName + providerName;
    }

    public String getStateName()
    {
        return this.stateName;
    }

    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }

    public String getStateCode()
    {
        return this.stateCode;
    }

    public void setStateCode(String stateCode)
    {
        this.stateCode = stateCode;
    }

    public String getArea()
    {
        return this.area;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getMobileType()
    {
        return this.mobileType;
    }

    public void setMobileType(String mobileType)
    {
        this.mobileType = mobileType;
    }

    public Telco getProvider()
    {
        return this.provider;
    }

    public void setProvider(Telco provider)
    {
        this.provider = provider;
    }
}
