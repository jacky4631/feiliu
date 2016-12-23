package com.jiam365.flow.sdk;

import com.jiam365.flow.sdk.response.DataReader;
import com.jiam365.flow.sdk.response.JSONDataReader;
import com.jiam365.modules.utils.ReflectionUtils;

public abstract class AbstractHandler
        implements ChannelHandler
{
    public void loadParams(String paramJson) {}

    public void init() {}

    public void release() {}

    public String getParamTemplate()
    {
        return "{}";
    }

    protected void loadJsonParams(String paramJson, String... paramNames)
    {
        DataReader reader = new JSONDataReader();
        reader.init(paramJson);
        if (paramNames.length > 0) {
            for (String paramName : paramNames) {
                ReflectionUtils.setFieldValue(this, paramName, reader.read(paramName));
            }
        }
        reader.release();
    }
}
