package com.jiam365.flow.plugins.wuyika;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO {
	
	private String MARK = "WuYiKa";
	private static Logger logger = LoggerFactory.getLogger(OrderCreateRequestDTO.class);
	
	private String Command="1";
	private String MerID;
	private String OrderID;
	private String ChgMobile;
	private String FlowAmount;
	private String ISP;
	private String ReplyFormat;
	private String InterfaceNumber;
	private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
	private String Datatime= df.format(new Date());
	private String MerURL;
	private String FlowType;
	private String Attach;
	private String sign;

	public String getCommand() {
		return Command;
	}

	public void setCommand(String command) {
		Command = command;
	}

	public String getMerID() {
		return MerID;
	}

	public void setMerID(String merID) {
		MerID = merID;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getChgMobile() {
		return ChgMobile;
	}

	public void setChgMobile(String chgMobile) {
		ChgMobile = chgMobile;
	}

	public String getFlowAmount() {
		return FlowAmount;
	}

	public void setFlowAmount(String flowAmount) {
		FlowAmount = flowAmount;
	}

	public String getISP() {
		return ISP;
	}

	public void setISP(String iSP) {
		ISP = iSP;
	}

	public String getReplyFormat() {
		return ReplyFormat;
	}

	public void setReplyFormat(String replyFormat) {
		ReplyFormat = replyFormat;
	}

	public String getInterfaceNumber() {
		return InterfaceNumber;
	}

	public void setInterfaceNumber(String interfaceNumber) {
		InterfaceNumber = interfaceNumber;
	}

	public String getDatatime() {
		return Datatime;
	}

	public void setDatatime(String datatime) {
		Datatime = datatime;
	}

	public String getMerURL() {
		return MerURL;
	}

	public void setMerURL(String merURL) {
		MerURL = merURL;
	}

	public String getFlowType() {
		return FlowType;
	}

	public void setFlowType(String flowType) {
		FlowType = flowType;
	}

	public String getAttach() {
		return Attach;
	}

	public void setAttach(String attach) {
		Attach = attach;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void generateSignature(String key) {
		
		//Command=xxx&MerID=xxx&OrderID=xxx&ChgMobile=xxx&FlowAmount=xxx&ISP=xxx&ReplyFormat=xxx&InterfaceNumber=xxx&Datetime=xxx& Key=xxx
		
		StringBuilder sb = new StringBuilder();
		sb.append("Command="+this.getCommand()).append("&MerID="+this.getMerID()).append("&OrderID="+this.getOrderID())
		.append("&ChgMobile="+this.getChgMobile()).append("&FlowAmount="+this.getFlowAmount()).append("&ISP="+this.getISP())
		.append("&ReplyFormat="+this.getReplyFormat()).append("&InterfaceNumber="+this.getInterfaceNumber())
		.append("&Datetime="+this.getDatatime()).append("&key="+key);
		String data = sb.toString();
		logger.debug(MARK + "_sign:" + data);
		String _signature = MD5.md5(data);
		this.setSign(_signature);

	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
