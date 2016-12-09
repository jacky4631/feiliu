package com.jiam365.flow.plugins.qiweishu;

public class QiweishuReport {

	private String appkey;
	private String timestamp;
	private String sign;
	private DataReport data;

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public DataReport getData() {
		return data;
	}

	public void setData(DataReport data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "QiweishuReport{" +
				"appkey='" + appkey + '\'' +
				", timestamp='" + timestamp + '\'' +
				", sign='" + sign + '\'' +
				", data=" + data +
				'}';
	}

	public class DataReport {
		public String orderid;
		public String mobile;
		public String message;
		public String code;
		public String messageid;

		@Override
		public String toString() {
			return "DataReport{" +
					"orderid='" + orderid + '\'' +
					", mobile='" + mobile + '\'' +
					", message='" + message + '\'' +
					", code='" + code + '\'' +
					", messageid='" + messageid + '\'' +
					'}';
		}
	}
}
