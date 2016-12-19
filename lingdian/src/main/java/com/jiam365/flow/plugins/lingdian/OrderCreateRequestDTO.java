package com.jiam365.flow.plugins.lingdian;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO extends BaseRequestDTO {

    /**
     * 交易号,客户提交
     */
    private String tradeNo;

    /**
     * 电话号码,半角逗号分割无空格
     */
    private String mobiles;


    /**
     * 流量规格,100M, 500M, 1G，等等
     */
    private String spec;

    /**
     * 国内c(country), 省内p(province)
     */
    private String areaType;

    /**
     * 本月tm(this month), 下月nm(next month)
     */
    private String effectiveType;

    /**
     * 回调网址
     */
    private String url;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getEffectiveType() {
        return effectiveType;
    }

    public void setEffectiveType(String effectiveType) {
        this.effectiveType = effectiveType;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public void generateSignature(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getTimestamp()).append(this.getTradeNo()).append(this.getMobiles()).append(this.getSpec());
        if (!StringUtils.isBlank(this.getUrl())) {
            sb.append(this.getUrl());
        }
        String data = sb.append(key).toString();
        System.out.println("sb:"+data);
        String _signature = MD5.md5(data);
        this.setSignature(_signature);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
