package com.jiam365.flow.plugins.uc;

public class UCReport {
	
	private int phone;
	private int group;
	private int result;
	private String remark;
	private String partner_order_no;

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPartner_order_no() {
		return partner_order_no;
	}

	public void setPartner_order_no(String partner_order_no) {
		this.partner_order_no = partner_order_no;
	}

	@Override
	public String toString() {
		return "UCReport{" +
				"phone=" + phone +
				", group=" + group +
				", result=" + result +
				", remark='" + remark + '\'' +
				", partner_order_no='" + partner_order_no + '\'' +
				'}';
	}
}
