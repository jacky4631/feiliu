package com.jiam365.flow.plugins.sswlingdian;

public class LingdianReport {
	
	private String tradeNo;
	private String mobile;
	private String ok;
	private String result;
	private String signature;
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOk() {
		return ok;
	}
	public void setOk(String ok) {
		this.ok = ok;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
	public String toString() {
		return "LingdianReport{" +
				"tradeNo='" + tradeNo + '\'' +
				", mobile='" + mobile + '\'' +
				", ok='" + ok + '\'' +
				", result='" + result + '\'' +
				", signature='" + signature + '\'' +
				'}';
	}
}
