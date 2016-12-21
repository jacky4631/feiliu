package com.jiam365.flow.plugins.wuyika;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.core.annotation.Order;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateRequestDTO {
	
	private String Command = "1";
	private String MerID;
	private String OrderID;
	private String ChgMobile;
	private String FlowAmount;
	private String ISP;
	private String ReplyFormat = "xml";
	private String InterfaceNumber = "2.1.0.1";
	private String Datetime;
	private String MerURL;
	private String FlowType;
	private String Attach = "hengha";
	private String Sign;

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

	public void setISP(String ISP) {
		this.ISP = ISP;
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

	public String getDatetime() {
		return Datetime;
	}

	public void setDatetime(String datetime) {
		Datetime = datetime;
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
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

	public void generateSignature(String key) {
		StringBuilder sb = new StringBuilder();
		sb.append("Command=")
				.append(Command)
				.append("&")
				.append("MerID=")
				.append(MerID)
				.append("&")
				.append("OrderID=")
				.append(OrderID)
				.append("&")
				.append("ChgMobile=")
				.append(ChgMobile)
				.append("&")
				.append("FlowAmount=")
				.append(FlowAmount)
				.append("&")
				.append("ISP=")
				.append(ISP)
				.append("&")
				.append("ReplyFormat=")
				.append(ReplyFormat)
				.append("&")
				.append("InterfaceNumber=")
				.append(InterfaceNumber)
				.append("&")
				.append("Datetime=")
				.append(Datetime)
				.append("&")
				.append("Key=")
				.append(key);
		String _signature= MD5.md5(sb.toString()).toUpperCase();
		this.setSign(_signature);
	}

	public String generateRequestString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Command=")
				.append(Command)
				.append("&")
				.append("MerID=")
				.append(MerID)
				.append("&")
				.append("OrderID=")
				.append(OrderID)
				.append("&")
				.append("ChgMobile=")
				.append(ChgMobile)
				.append("&")
				.append("FlowAmount=")
				.append(FlowAmount)
				.append("&")
				.append("ISP=")
				.append(ISP)
				.append("&")
				.append("ReplyFormat=")
				.append(ReplyFormat)
				.append("&")
				.append("InterfaceNumber=")
				.append(InterfaceNumber)
				.append("&")
				.append("Datetime=")
				.append(Datetime)
				.append("&")
				.append("MerURL=")
				.append(MerURL)
				.append("&")
				.append("FlowType =")
				.append(FlowType)
				.append("&")
				.append("Attach=")
				.append(Attach)
				.append("&")
				.append("Sign=")
				.append(Sign);
		return sb.toString();
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
