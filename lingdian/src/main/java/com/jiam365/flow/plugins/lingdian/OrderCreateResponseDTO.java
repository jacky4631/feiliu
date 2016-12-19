package com.jiam365.flow.plugins.lingdian;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateResponseDTO {
	
	private boolean ok;
	private String message;
	private int code;

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
