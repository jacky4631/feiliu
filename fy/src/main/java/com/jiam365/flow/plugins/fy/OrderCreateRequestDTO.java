package com.jiam365.flow.plugins.fy;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO {
	private String username;
	private String password;
	private String echostr;
	private String orderid;
	private String timestamp;
	private String phone;
	private String type = "1";
	private String product;
	private String sign;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEchostr() {
		return echostr;
	}

	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void generateSignature(String key) {
		StringBuilder sb = new StringBuilder();
		sb.append(getUsername())
				.append(getPassword())
				.append(getEchostr())
				.append(getOrderid())
				.append(getTimestamp())
				.append(getPhone())
				.append(getType())
				.append(getProduct())
				.append(key);
		String _signature= MD5.md5(sb.toString());
		this.setSign(_signature);
	}
	

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
