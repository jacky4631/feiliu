package com.jiam365.flow.plugins.lingdian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.*;
import com.jiam365.flow.sdk.response.JSONDataReader;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.response.XMLDataReader;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.modules.mapper.JsonMapper;
import com.jiam365.modules.net.HttpClients;
import com.jiam365.modules.utils.Digests;
import com.jiam365.modules.utils.Encodes;
import com.jiam365.modules.utils.StringIdGenerator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Date;

public class LingdianHandler extends AbstractHandler {

	private String MARK="LingDian";
	private static Logger logger = LoggerFactory.getLogger(LingdianHandler.class);
	private String username;
	private String key;
	private String rechargeUrl;
	private String callbackUrl;

	@Override
	public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
		HttpPost method = ClientUtils.getPostMethod(rechargeUrl);
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setUsername(username);
		String orderId = StringIdGenerator.get();
		dto.setTradeNo(orderId); //本地订单号或者交易号
		dto.setMobiles(request.getMobile()); //单个号码
		//dto.setMobiles("18684410000,18684410001,186844100031,13841188888"); //多个号码
		dto.setSpec(request.getSize()+"M");
		dto.setAreaType(getArea(request.getProductId()));
		dto.setEffectiveType("tm");
		if(StringUtils.isEmpty(callbackUrl)) {
			callbackUrl = "http://120.55.71.93/report/lingdian";
		}
		dto.setUrl(callbackUrl);
		dto.generateSignature(key);
		String o = ClientUtils.getJson(method, dto);
		logger.debug(MARK + "_recharge_url ret:" + o);

		OrderCreateResponseDTO res = JSON.parseObject(o, OrderCreateResponseDTO.class);
		ResponseData data = new ResponseData();
		data.setSuccessValue("0");
		data.setResult(String.valueOf(res.getCode()));
		data.setMessage(res.getMessage());
		data.setRequestNo(orderId);
		return data;
	}

	public String getArea(String productId) {
		if(productId.endsWith("$")) {
			return "P";//非漫游包
		} else {
			return "C";//漫游包
		}
	}

	@Override
	public ResponseData queryReport(RechargeRequest rechargeRequest, String reqNo) throws ChannelConnectionException {
		String json = TradeReportServiceProxy.fetch(reqNo);
		logger.debug(MARK + "_report_json:" + json);
		return callback(json, reqNo);
	}

	 public ResponseData callback(String json, String reqNo){
		 if (TextUtils.isEmpty(json)) {
			 throw new ChannelConnectionException("无法获取零点公司回调报文");
		 } else {
			 ResponseData data = new ResponseData();
			 LingdianReport rechargeReport = JSON.parseObject(json, LingdianReport.class);
			 data.setRequestNo(reqNo);
			 String ret_msg = rechargeReport.getResult();
			 String ret_code = rechargeReport.getResult();
			 data.setMessage(ret_msg);
			 data.setResult(ret_code);
			 data.setSuccessValue("s");
			 return data;
		 }
		}

	@Override
	public void loadParams(String paramJson) {
		Params params = JSON.parseObject(paramJson, Params.class);
		username = params.getUsername();
		key = params.getKey();
		rechargeUrl = params.getRechargeUrl();
		callbackUrl = params.getCallbackUrl();
	}

	@Override
	public String getParamTemplate() {
		return "{" +
				"\"rechargeUrl\": \"充值地址\"," +
				"\"username\": \"用户ID\"," +
				"\"key\": \"认证KEY\"," +
				"\"callbackUrl\": \"回调地址\"" +
				"}";
	}

}
