package com.jiam365.flow.plugins.migu;

public class MiguReport {

	private String orderId;
	private String customerOrderId;
	private String status;
	private String reason;
	private String sign;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustomerOrderId() {
		return customerOrderId;
	}

	public void setCustomerOrderId(String customerOrderId) {
		this.customerOrderId = customerOrderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
