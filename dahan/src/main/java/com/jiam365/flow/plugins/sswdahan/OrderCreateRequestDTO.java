package com.jiam365.flow.plugins.sswdahan;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO {

	private String account;
	private String mobiles;
	private String sign;
	private int packageSize;
	private String msgTemplateId;
	private String clientOrderId;
	private long timestamp = System.currentTimeMillis();

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getPackageSize() {
		return packageSize;
	}

	public void setPackageSize(int packageSize) {
		this.packageSize = packageSize;
	}

	public String getMsgTemplateId() {
		return msgTemplateId;
	}

	public void setMsgTemplateId(String msgTemplateId) {
		this.msgTemplateId = msgTemplateId;
	}

	public String getClientOrderId() {
		return clientOrderId;
	}

	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void generateSignature(String pwd) {
		StringBuilder sb = new StringBuilder();
		//sign=MD5(account+MD5(pwd)+timestamp+mobiles) 
		sb.append(this.getAccount()).append(MD5.md5(pwd)).append(this.getTimestamp()).append(this.getMobiles()).append(packageSize).append(clientOrderId);
		String data = sb.toString();
		System.out.println("sb:" + data);
		String _signature = MD5.md5(data);
		this.setSign(_signature);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
