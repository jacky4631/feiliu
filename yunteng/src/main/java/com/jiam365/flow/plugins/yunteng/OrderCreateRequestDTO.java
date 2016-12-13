package com.jiam365.flow.plugins.yunteng;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Base64;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO {
	
	private String channelOrderId;
	private int amount;
	private String mobile;
	private String callbackUrl;

	public String getChannelOrderId() {
		return channelOrderId;
	}

	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String generateSignature(String apiKey) {
		StringBuilder sb = new StringBuilder();
		sb.append(apiKey);
		sb.append(Base64.getEncoder().encodeToString(JSON.toJSONBytes(this)));
		sb.append(apiKey);
		return MD5.md5(sb.toString());

	}
	

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
