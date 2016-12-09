package com.jiam365.flow.plugins.qiweishu;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO {
	
	private String customerOrderId;
	private String enterpriseCode;
	private String productCode;
	private String mobile;
	private String orderTime;
	private String sign;

	public String getCustomerOrderId() {
		return customerOrderId;
	}

	public void setCustomerOrderId(String customerOrderId) {
		this.customerOrderId = customerOrderId;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void generateSignature(String password) {
		StringBuilder sb = new StringBuilder();
		sb.append("enterpriseCode=")
				.append(enterpriseCode)
				.append("&")
				.append("productCode=")
				.append(productCode)
				.append("&")
				.append("mobile=")
				.append(mobile)
				.append("&")
				.append("orderTime=")
				.append(orderTime)
				.append("&")
				.append("password=")
				.append(password);
		String _signature= MD5.md5(sb.toString());
		this.setSign(_signature);
	}
	

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
