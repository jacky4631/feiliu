package com.jiam365.flow.plugins.yuanju;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.AbstractHandler;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.JSONDataReader;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.mapper.JsonMapper;
import com.jiam365.modules.utils.StringIdGenerator;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class YuanJuHandler extends AbstractHandler {

	private String MARK = "YUANJU";
	private static Logger logger = LoggerFactory.getLogger(YuanJuHandler.class);
	private String rechargeUrl;
	private String username;
	private String key;
	private String secret;

	@Override
	public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
//		logger.debug(MARK + "_recharge_url:" + "productName:"+request.getProductName()+",origiProductId:"+request.getOrigiProductId()+",executeProductId"+request.getExecuteProductId());
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setUsername(username);
		dto.setKey(key);
		String orderId = StringIdGenerator.get();
		dto.setClientid(orderId);
		dto.setProductids(request.getOrigiProductId());
		dto.setMobile(request.getMobile());
		dto.generateSignature(secret);
		dto.setNotifyurl("http://120.55.71.93/report/charge");
		dto.setFlowtype(getArea(request.getProductId()));
		String url="";
		url=rechargeUrl+"?username="+dto.getUsername()+
				"&key="+dto.getKey()+
				"&clientid="+dto.getClientid()+
				"&productids="+dto.getProductids()+
				"&mobile="+dto.getMobile()+
				"&sign="+dto.getSign()+
				"&notifyurl="+dto.getNotifyurl()+
				"&flowtype="+dto.getFlowtype();

		HttpGet method = ClientUtils.getGetMethod(url);
		logger.debug(MARK + "_recharge_url:" + url);

		String o = ClientUtils.getJson(method);
		logger.debug(MARK + "_recharge_url ret:" + o);
		JSONObject object = JSON.parseObject(o);
		ResponseData data = new ResponseData();
		data.setSuccessValue("1");
		data.setResult(object.getString("res"));
		data.setMessage(object.getString("resmsg"));
		data.setRequestNo(orderId);
		return data;
	}

	@Override
	public ResponseData queryReport(RechargeRequest rechargeRequest, String reqNo) throws ChannelConnectionException {
		String json = TradeReportServiceProxy.fetch(reqNo);
		logger.debug(MARK + "_report_json:" + json);
		JsonMapper mapper = JsonMapper.nonEmptyMapper();
		ResponseData data = new ResponseData();
		data.setRetryValues(new String[] { "-2"});
		data.setRequestNo(reqNo);
		if (json != null) {
			YuanJuReport rechargeReport = mapper.fromJson(json, YuanJuReport.class);
			logger.debug(MARK + "_report_bean:" + rechargeReport.toString());
			String ret_msg=rechargeReport.getResmsg();
			String ret_code=String.valueOf(rechargeReport.getRes());
			data.setMessage(ret_msg);
			data.setResult(ret_code);
			data.setSuccessValue("2");
		} else {
			data.setSuccessValue("0");
			data.setResult("-2");
			data.setMessage("没有回调");
		}
		return data;
	}

	@Override
	public void loadParams(String paramJson) {
		// TODO Auto-generated method stub
		JSONDataReader reader = new JSONDataReader();
		reader.init(paramJson);
		rechargeUrl = reader.read("rechargeUrl");
		username = reader.read("username");
		key = reader.read("key");
		secret = reader.read("secret");
		reader.release();
	}

	@Override
	public String getParamTemplate() {
		// TODO Auto-generated method stub
		return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"username\":\"账号\"," + "\"key\":\"密钥\"," + "\"secret\":\"密码\"" + "}";
	}

	public String getArea(String productId) {
		if(productId.endsWith("$")) {
			return "2";
		} else {
			return "1";
		}
	}

}
