package com.jiam365.flow.plugins.yunteng;

public class YuntengReport {

	private String orderId;
	private String channelOrderId;
	private String status;
	private String failReason;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChannelOrderId() {
		return channelOrderId;
	}

	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
}
