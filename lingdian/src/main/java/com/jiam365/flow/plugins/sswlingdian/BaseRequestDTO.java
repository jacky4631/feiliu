package com.jiam365.flow.plugins.sswlingdian;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Created by ken on 15/12/16.
 */
public class BaseRequestDTO {

    /**
     * 用户名
     */
    protected String username;

    /**
     * 把当前时间提交上来, 到秒的毫秒数字,和服务器当前时间差距不可以超过60s
     */
    protected long timestamp = System.currentTimeMillis();

    /**
     * 签名
     */
    protected String signature;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * 如果不一样可以重写,否则直接用
     */
    public void generateSignature(String key) {
        String data = Long.toString(timestamp);
        String _signature = MD5.md5(data + key);
        this.setSignature(_signature);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}

