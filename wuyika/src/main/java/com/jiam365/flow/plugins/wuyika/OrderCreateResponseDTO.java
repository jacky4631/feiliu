package com.jiam365.flow.plugins.wuyika;

/**
 * Created by ken on 15/12/13.
 */
public class OrderCreateResponseDTO {
	
	private String OrderID;
	private String TranStat;
	private String TranInfo;

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getTranStat() {
		return TranStat;
	}

	public void setTranStat(String tranStat) {
		TranStat = tranStat;
	}

	public String getTranInfo() {
		return TranInfo;
	}

	public void setTranInfo(String tranInfo) {
		TranInfo = tranInfo;
	}
}
