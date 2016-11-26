package com.jiam365.flow.plugins.xinyou;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO extends BaseRequestDTO {

    
    /**
     * 号码
     */
    private String Mobile;
    
    
    /**
     * 商户订单号 商户系统内部的订单号,64个字符内、可包含字母，可为空
     */
    private String OutTradeNo;
    
    
    /**
     * 套餐 流量包大小
     */
    private String Package;
    
    
    /**
     * 流量类型 0 全国流量 1省内流量，不带改参数时默认为0
     */
    private String Range;
    
    
    


	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getOutTradeNo() {
		return OutTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		OutTradeNo = outTradeNo;
	}

	public String getPackage() {
		return Package;
	}

	public void setPackage(String package1) {
		Package = package1;
	}

	public String getRange() {
		return Range;
	}

	public void setRange(String range) {
		Range = range;
	}

	@Override
    public void generateSignature(String key) {
        StringBuilder sb = new StringBuilder();
//        "account=szhh&mobile=13815261656&package=10&key=a426da9b82444c8bb7b415670c50e18f"
        sb.append("account="+this.getAccount()).append("&mobile="+this.getMobile())
        .append("&package="+this.getPackage()).append("&key="+key);
        String data = sb.toString();
        System.out.println("sb:"+data);
        String _signature = MD5.md5(data);
        this.setSign(_signature);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
