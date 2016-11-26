package com.jiam365.flow.plugins.zhuowei;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO {
	
	private String MARK = "ZhuoWei";
	private static Logger logger = LoggerFactory.getLogger(OrderCreateRequestDTO.class);
	
	private String ec_code;
	private String order_number;
	private String mobile;
	private String product_code;
	private String sign;
	

	public String getEc_code() {
		return ec_code;
	}


	public void setEc_code(String ec_code) {
		this.ec_code = ec_code;
	}


	public String getOrder_number() {
		return order_number;
	}


	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getProduct_code() {
		return product_code;
	}


	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public void generateSignature(String key) {
//		accountVal notifyUrl sign spuId supplierId tbOrderNo
		StringBuilder sb = new StringBuilder();
		sb.append(this.getEc_code()).append(this.getOrder_number()).append(this.getMobile()).append(this.getProduct_code())
		.append(key);
		String data = sb.toString();
		
		System.out.println("sign:"+data);
		
		String _signature=MD5.md5(data).toUpperCase();
		this.setSign(_signature);
	}
	

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
