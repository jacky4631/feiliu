package com.jiam365.flow.plugins.yuanju;

public class YuanJuReport {

	private String res;
	private String resmsg;
	private String resdata;

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getResmsg() {
		return resmsg;
	}

	public void setResmsg(String resmsg) {
		this.resmsg = resmsg;
	}

	public String getResdata() {
		return resdata;
	}

	public void setResdata(String resdata) {
		this.resdata = resdata;
	}

	@Override
	public String toString() {
		return "YuanJuReport{" +
				"res='" + res + '\'' +
				", resmsg='" + resmsg + '\'' +
				", resdata='" + resdata + '\'' +
				'}';
	}
}
