package com.jiam365.flow.plugins.zhixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.AbstractHandler;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.JSONDataReader;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.utils.StringIdGenerator;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ZhiXinHandler extends AbstractHandler {

	private String MARK = "ZHIXIN";
	private static Logger logger = LoggerFactory.getLogger(ZhiXinHandler.class);
	private String rechargeUrl;
	private String appKey;
	private String appSecret;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
//		logger.debug(MARK + "_recharge_url:" + "productName:"+request.getProductName()+",origiProductId:"+request.getOrigiProductId()+",executeProductId"+request.getExecuteProductId());
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setAppkey(appKey);
		dto.setTimestamp(dateFormat.format(new Date()));
		dto.setPackageid(request.getOrigiProductId());
		dto.setMobiles(request.getMobile());
		String orderId = StringIdGenerator.get();
		dto.setMessageid(orderId);
		dto.generateSignature(appSecret);
		String url="";
		url=rechargeUrl+"?appkey="+dto.getAppkey()+
				"&timestamp="+dto.getTimestamp()+
				"&packageid="+dto.getPackageid()+
				"&mobiles="+dto.getMobiles()+
				"&messageid="+dto.getMessageid()+
				"&sign="+dto.getSign();

		HttpGet method = ClientUtils.getGetMethod(url);
		logger.debug(MARK + "_recharge_url:" + url);

		String o = ClientUtils.getJson(method);
		logger.debug(MARK + "_recharge_url ret:" + o);
		try {
			o = URLDecoder.decode(o,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject object = JSON.parseObject(o);
		ResponseData data = new ResponseData();
		data.setSuccessValue("0");
		data.setResult(object.getString("code"));
		data.setMessage(object.getString("message"));
		data.setRequestNo(orderId);
		return data;
	}

	@Override
	public ResponseData queryReport(RechargeRequest rechargeRequest, String reqNo) throws ChannelConnectionException {
		String json = TradeReportServiceProxy.fetch(reqNo);
		logger.debug(MARK + "_report_json:" + json);
		return callback(json, reqNo);
	}

	public ResponseData callback(String json, String reqNo) {
		ResponseData data = new ResponseData();
		data.setRetryValues(new String[] { "-2"});
		data.setRequestNo(reqNo);
		if (json != null) {
			ZhiXinReport rechargeReport = ClientUtils.getJsonMapper().fromJson(json, ZhiXinReport.class);
			logger.debug(MARK + "_report_bean:" + rechargeReport.toString());
			if(rechargeReport.getData() != null) {
				String ret_msg=rechargeReport.getData().message;
				String ret_code=String.valueOf(rechargeReport.getData().code);
				data.setMessage(ret_msg);
				data.setResult(ret_code);
			} else {
				data.setResult("-2");
				data.setMessage("没有data参数");
			}
			data.setSuccessValue("1");
		} else {
			data.setSuccessValue("0");
			data.setResult("-2");
			data.setMessage("没有回调");
		}
		return data;
	}

	@Override
	public void loadParams(String paramJson) {
		JSONDataReader reader = new JSONDataReader();
		reader.init(paramJson);
		rechargeUrl = reader.read("rechargeUrl");
		appKey = reader.read("appkey");
		appSecret = reader.read("appsecret");
		reader.release();
	}

	@Override
	public String getParamTemplate() {
		return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"appkey\":\"密钥\"," + "\"appsecret\":\"密码\"" + "}";
	}

}
