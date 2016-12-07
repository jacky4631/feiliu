package com.jiam365.flow.plugins.xinkedang;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO {
	
	private String mid;
	private String data;

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void generateSignature(String date, String phone, String snum, String flowType, String size, String notifyUrl, String secretKey) {
		DesDto desDto = new DesDto();
		desDto.mid = this.mid;
		desDto.date = date;
		desDto.phone = phone;
		desDto.snum = snum;
		desDto.flowType = flowType;
		desDto.size = size;
		desDto.notifyUrl = notifyUrl;
		String desStr = ClientUtils.getJsonMapper().toJson(desDto);
		this.setData(DESUtils.encrypt(desStr, secretKey));
	}
	public class DesDto{
		public String mid;
		public String date;
		public String phone;
		public String snum;
		public String flowType;
		public String size;
		public String notifyUrl;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
