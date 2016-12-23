package com.jiam365.flow.sdk.response;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ResponseData
{
    private String requestNo;
    private String result;
    private String message;
    List<String> extMessage = new ArrayList();
    private String successValue;
    private String[] retryValues;
    private boolean dummy = false;

    public static ResponseData newSuccess(String requestNo)
    {
        ResponseData data = new ResponseData();
        data.setRequestNo(requestNo);
        data.setResult("7777");
        data.setMessage("success");
        data.setSuccessValue("7777");
        return data;
    }

    public static ResponseData newFail(String requestNo)
    {
        ResponseData data = new ResponseData();
        data.setRequestNo(requestNo);
        data.setResult("9999");
        data.setMessage("fail");
        data.setSuccessValue("0000");
        return data;
    }

    public ResponseData addRequestNoPrefix(String pre)
    {
        if (this.requestNo != null) {
            this.requestNo = (pre + this.requestNo);
        }
        return this;
    }

    public boolean isSuccess()
    {
        if (this.successValue == null) {
            throw new RuntimeException("Success value is null");
        }
        String[] values;
        if (this.successValue.contains(",")) {
            values = this.successValue.split(",");
        } else {
            values = new String[] { this.successValue };
        }
        for (String value : values) {
            if (value.trim().equalsIgnoreCase(this.result)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFail()
    {
        return (!isSuccess()) && (!canRetry());
    }

    public boolean canRetry()
    {
        if ((this.retryValues != null) && (this.retryValues.length > 0)) {
            for (String value : this.retryValues) {
                if (value.equalsIgnoreCase(this.result)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addExtMessage(String str)
    {
        this.extMessage.add(str);
    }

    public String getRequestNo()
    {
        return this.requestNo;
    }

    public void setRequestNo(String requestNo)
    {
        this.requestNo = requestNo;
    }

    public String getResult()
    {
        return this.result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getMessage()
    {
        String moreMessage = "";
        for (String msg : this.extMessage) {
            moreMessage = moreMessage + " " + msg;
        }
        String ret;
        if (StringUtils.isNotBlank(moreMessage))
        {
            ret = this.message + moreMessage;
        }
        else
        {
            if (StringUtils.isBlank(this.message)) {
                ret = isSuccess() ? "��������" : "��������";
            } else {
                ret = this.message;
            }
        }
        return ret;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<String> getExtMessage()
    {
        return this.extMessage;
    }

    public String getSuccessValue()
    {
        return this.successValue;
    }

    public void setSuccessValue(String successValue)
    {
        this.successValue = successValue;
    }

    public String[] getRetryValues()
    {
        return this.retryValues;
    }

    public void setRetryValues(String[] retryValues)
    {
        this.retryValues = retryValues;
    }

    public boolean isDummy()
    {
        return this.dummy;
    }

    public void setDummy(boolean dummy)
    {
        this.dummy = dummy;
    }

    public String toString()
    {
        return "{requestNo='" + this.requestNo + '\'' + ", result='" + this.result + '\'' + ", message='" + this.message + '\'' + ", extMessage=" + this.extMessage + '}';
    }
}
