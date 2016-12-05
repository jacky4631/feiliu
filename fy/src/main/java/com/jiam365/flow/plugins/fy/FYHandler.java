package com.jiam365.flow.plugins.fy;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FYHandler extends AbstractHandler {

	private String MARK = "FY";
	private static Logger logger = LoggerFactory.getLogger(FYHandler.class);
	private String rechargeUrl;
	private String username;
	private String password;
	private String key;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
//		logger.debug(MARK + "_recharge_url:" + "productName:"+request.getProductName()+",origiProductId:"+request.getOrigiProductId()+",executeProductId"+request.getExecuteProductId());
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setUsername(username);
		dto.setPassword(password);
		String orderId = StringIdGenerator.get();
		dto.setEchostr(orderId);
		dto.setOrderid(orderId);
		dto.setTimestamp(dateFormat.format(new Date()));
		dto.setPhone(request.getMobile());
		dto.setProduct(request.getOrigiProductId());
		dto.generateSignature(key);
		HttpPost method = ClientUtils.getPostMethod(rechargeUrl);

		String o = ClientUtils.getJson(method, dto);
		logger.debug(MARK + "_recharge_url ret:" + o);
		try {
			o = new String(o.getBytes("ISO-8859-1"), "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject object = JSON.parseObject(o);
		ResponseData data = new ResponseData();
		data.setSuccessValue("0000000");
		data.setResult(object.getString("code"));
		data.setMessage(object.getString("msg"));
		data.setRequestNo(orderId);
		return data;
	}

	@Override
	public ResponseData queryReport(RechargeRequest rechargeRequest, String reqNo) throws ChannelConnectionException {
		String json = TradeReportServiceProxy.fetch(reqNo);
		logger.debug(MARK + "_report_json:" + json);
		ResponseData data = new ResponseData();
		data.setRetryValues(new String[] { "-2"});
		data.setRequestNo(reqNo);
		if (json != null) {
			FYReport rechargeReport = ClientUtils.getJsonMapper().fromJson(json, FYReport.class);
			logger.debug(MARK + "_report_bean:" + rechargeReport.toString());
			String ret_msg=rechargeReport.getMsg();
			String ret_code=String.valueOf(rechargeReport.getCode());
			data.setMessage(ret_msg);
			data.setResult(ret_code);
			data.setSuccessValue("111111");
		} else {
			data.setSuccessValue("111111");
			data.setResult("999999");
			data.setMessage("没有回调");
		}
		return data;
	}

	@Override
	public void loadParams(String paramJson) {
		JSONDataReader reader = new JSONDataReader();
		reader.init(paramJson);
		rechargeUrl = reader.read("rechargeUrl");
		username = reader.read("username");
		password = reader.read("password");
		key = reader.read("key");
		reader.release();
	}

	@Override
	public String getParamTemplate() {
		return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"username\":\"用户名\"," + "\"password\":\"密码\"," + "\"key\":\"密钥\"" + "}";
	}

}
