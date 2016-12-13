package com.jiam365.flow.plugins.migu;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO {
	
	private String action = "SingleCharge";
	private String account;
	private String chargetype;
	private String orderno;
	private String mobile;
	private String procode;
	private String chargesign;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getChargetype() {
		return chargetype;
	}

	public void setChargetype(String chargetype) {
		this.chargetype = chargetype;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProcode() {
		return procode;
	}

	public void setProcode(String procode) {
		this.procode = procode;
	}

	public String getChargesign() {
		return chargesign;
	}

	public void setChargesign(String chargesign) {
		this.chargesign = chargesign;
	}

	public void generateSignature(String apiKey) {
		StringBuilder sb = new StringBuilder();
		sb.append("account=")
				.append(account)
				.append("&")
				.append("mobile=")
				.append(mobile)
				.append("&")
				.append("orderno=")
				.append(orderno)
				.append("&")
				.append("procode=")
				.append(procode)
				.append("&")
				.append("key=")
				.append(apiKey);
		String _signature= MD5.md5(sb.toString());
		this.setChargesign(_signature);
	}
	

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
