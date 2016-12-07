package com.jiam365.flow.plugins.xinkedang;

public class XinkedangReport {

	private String serial_number;
	private String order_id;
	private String fs_code;
	private String fs_message;

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getFs_code() {
		return fs_code;
	}

	public void setFs_code(String fs_code) {
		this.fs_code = fs_code;
	}

	public String getFs_message() {
		return fs_message;
	}

	public void setFs_message(String fs_message) {
		this.fs_message = fs_message;
	}

	@Override
	public String toString() {
		return "XinkedangReport{" +
				"serial_number='" + serial_number + '\'' +
				", order_id='" + order_id + '\'' +
				", fs_code='" + fs_code + '\'' +
				", fs_message='" + fs_message + '\'' +
				'}';
	}
}
