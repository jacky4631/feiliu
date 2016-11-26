package com.jiam365.flow.plugins.shenji;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO {
	
	private String MARK = "ShenJi";
	private static Logger logger = LoggerFactory.getLogger(OrderCreateRequestDTO.class);
	
	private String username;
	private String phone;
	private String timestamp;
	private String sign;
	private String area;
	private String capacity;
	private String available_length;
	private String clear_type;
	private String partner_order_no;

	public String getMARK() {
		return MARK;
	}

	public void setMARK(String MARK) {
		this.MARK = MARK;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		OrderCreateRequestDTO.logger = logger;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getAvailable_length() {
		return available_length;
	}

	public void setAvailable_length(String available_length) {
		this.available_length = available_length;
	}

	public String getClear_type() {
		return clear_type;
	}

	public void setClear_type(String clear_type) {
		this.clear_type = clear_type;
	}

	public String getPartner_order_no() {
		return partner_order_no;
	}

	public void setPartner_order_no(String partner_order_no) {
		this.partner_order_no = partner_order_no;
	}

	public void generateSignature(String key) {
//		accountVal notifyUrl sign spuId supplierId tbOrderNo
		StringBuilder sb = new StringBuilder();
		sb.append(getArea())
				.append(",")
				.append(getUsername())
				.append(",")
				.append(getPhone())
				.append(",")
				.append(getCapacity())
				.append(",")
				.append(getTimestamp())
				.append(",")
				.append(MD5.SHA1(key));
		String _signature= MD5.SHA1(sb.toString());
		this.setSign(_signature);
	}
	

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
