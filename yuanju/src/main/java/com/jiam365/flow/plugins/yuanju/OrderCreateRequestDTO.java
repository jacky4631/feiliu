package com.jiam365.flow.plugins.yuanju;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO {
	
	private String MARK = "YUANJU";
	private static Logger logger = LoggerFactory.getLogger(OrderCreateRequestDTO.class);
	
	private String username;
	private String key;
	private String clientid;
	private String productids;
	private String mobile;
	private String sign;
	private String notifyurl;
	private String flowtype;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getProductids() {
		return productids;
	}

	public void setProductids(String productids) {
		this.productids = productids;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNotifyurl() {
		return notifyurl;
	}

	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

	public String getFlowtype() {
		return flowtype;
	}

	public void setFlowtype(String flowtype) {
		this.flowtype = flowtype;
	}

	public void generateSignature(String secret) {
		StringBuilder sb = new StringBuilder();
		sb.append(getUsername())
				.append(getKey())
				.append(secret)
				.append(getClientid());
		String _signature= MD5.md5(sb.toString());
		this.setSign(_signature);
	}
	

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
