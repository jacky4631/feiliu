package com.jiam365.flow.plugins.qiweishu;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateResponseDTO {
	
	private boolean success;
	private String error;
	private String orderId;
	private int price;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
