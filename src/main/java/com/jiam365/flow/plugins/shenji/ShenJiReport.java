package com.jiam365.flow.plugins.shenji;

public class ShenJiReport {
	
	private int success;
	private String message;
	private int group;
	private int code;
	private String reason;
	private String partner_order_no;

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPartner_order_no() {
		return partner_order_no;
	}

	public void setPartner_order_no(String partner_order_no) {
		this.partner_order_no = partner_order_no;
	}
}
