package com.jiam365.flow.plugins.xinkedang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.AbstractHandler;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.JSONDataReader;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.utils.StringIdGenerator;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class XinkedangHandler extends AbstractHandler {

	private String MARK = "XINKEDANG";
	private static Logger logger = LoggerFactory.getLogger(XinkedangHandler.class);
	private String rechargeUrl;
	private String mid;
	private String secretKey;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String notifyUrl;

	@Override
	public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setMid(mid);
		String orderId = mid + StringIdGenerator.get().substring(0, 19);
		if(TextUtils.isEmpty(notifyUrl)) {
			notifyUrl = "http://120.55.71.93/report/xinkedang";
		}
		try {
			dto.generateSignature(dateFormat.format(new Date()),
                    request.getMobile(),
                    orderId,
                    getArea(request.getProductId()),
                    String.valueOf(request.getSize()),
                    notifyUrl,
                    secretKey);
		} catch (Exception e) {
			logger.debug(MARK + "_generateSignature:" + e.getMessage());
		}

		HttpPost method = ClientUtils.getPostMethod(rechargeUrl);
		logger.debug(MARK + "_recharge_url:" + rechargeUrl);

		String o = ClientUtils.getJson(method, dto);
		logger.debug(MARK + "_recharge_url ret:" + o);

		JSONObject object = JSON.parseObject(o);
		ResponseData data = new ResponseData();
		data.setSuccessValue("0000");
		data.setResult(object.getString("result"));
		data.setMessage(object.getString("msg"));
		data.setRequestNo(orderId);
		return data;
	}

	@Override
	public ResponseData queryReport(RechargeRequest rechargeRequest, String reqNo) throws ChannelConnectionException {
		String json = TradeReportServiceProxy.fetch(reqNo);
		logger.debug(MARK + "_report_json:" + json);
		if(TextUtils.isEmpty(json)){
			throw new ChannelConnectionException("");
		} else {
			ResponseData data = new ResponseData();
			data.setRequestNo(reqNo);
			XinkedangReport rechargeReport = ClientUtils.getJsonMapper().fromJson(json, XinkedangReport.class);
			logger.debug(MARK + "_report_bean:" + rechargeReport.toString());
			String ret_msg=rechargeReport.getFs_message();
			String ret_code=String.valueOf(rechargeReport.getFs_code());
			data.setMessage(ret_msg);
			data.setResult(ret_code);
			data.setSuccessValue("0000");
			return data;
		}


	}

	@Override
	public void loadParams(String paramJson) {
		JSONDataReader reader = new JSONDataReader();
		reader.init(paramJson);
		rechargeUrl = reader.read("rechargeUrl");
		mid = reader.read("mid");
		secretKey = reader.read("Secretkey");
		notifyUrl = reader.read("notifyUrl");
		reader.release();
	}

	@Override
	public String getParamTemplate() {
		return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"mid\":\"mid\"," + "\"Secretkey\":\"密钥\"," + "\"notifyUrl\":\"\"" + "}";
	}
	public String getArea(String productId) {
		if(productId.endsWith("$")) {
			return "1";
		} else {
			return "0";
		}
	}

}
