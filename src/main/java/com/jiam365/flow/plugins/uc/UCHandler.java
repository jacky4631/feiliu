package com.jiam365.flow.plugins.uc;

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
import java.text.SimpleDateFormat;
import java.util.Date;

public class UCHandler extends AbstractHandler {

	private String MARK = "UC";
	private static Logger logger = LoggerFactory.getLogger(UCHandler.class);
	private String password;
	private String rechargeUrl;
	private String username;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
		// TODO Auto-generated method stub
		return createOrders(request);
	}
	public String getArea(String productId) {
		if(productId.startsWith("NA")) {
			return "0";
		} else if(productId.endsWith("$")) {
			return "2";
		} else {
			return "1";
		}
	}

	private ResponseData createOrders(RechargeRequest request) {
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setUsername(username);
		dto.setPhone(request.getMobile());
		dto.setTimestamp(dateFormat.format(new Date()));
		dto.setArea(getArea(request.getProductId()));
		dto.setCapacity(String.valueOf(request.getSize()));
		dto.generateSignature(password);
		String orderId = StringIdGenerator.get();
		dto.setPartner_order_no(orderId);
		String url="";
		url=rechargeUrl+"?area="+dto.getArea()+
				"&username="+dto.getUsername()+
				"&phone="+dto.getPhone()+
				"&capacity="+dto.getCapacity()+
				"&timestamp="+dto.getTimestamp()+
                "&sign="+dto.getSign()+
				"&partner_order_no="+dto.getPartner_order_no();
				
		HttpGet method = ClientUtils.getGetMethod(url);
		logger.debug(MARK + "_recharge_url:" + url);

		String o = ClientUtils.getJson(method);
		try {
			o = new String(o.getBytes("ISO-8859-1"), "utf8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug(MARK + "_recharge_url ret:" + o);
		JSONObject object = JSON.parseObject(o);
		int success = object.getIntValue("success");
		String message = null;
		String code = null;
		if(success == 1) {
			code = String.valueOf(success);
			message = object.getString("message");
		} else {
			code = object.getString("code");
			message =object.getString("reason");
		}
		ResponseData data = new ResponseData();
		data.setSuccessValue(String.valueOf(success));
		data.setResult(code);
		data.setMessage(message);
		data.setRequestNo(orderId);
		return data;
	}

	@Override
	public ResponseData queryReport(RechargeRequest rechargeRequest, String reqNo) throws ChannelConnectionException {
		// TODO Auto-generated method stub
		return callback(reqNo);
	}
	
	
	private ResponseData callback(String reqNo){
		String json = TradeReportServiceProxy.fetch(reqNo);
		logger.debug(MARK + "_report_json:" + json);
		JsonMapper mapper = JsonMapper.nonEmptyMapper();
		ResponseData data = new ResponseData();
		data.setRetryValues(new String[] { "-2"});
		data.setRequestNo(reqNo);
		if (json != null) {
			UCReport rechargeReport = mapper.fromJson(json, UCReport.class);
			logger.debug(MARK + "_report_bean:" + rechargeReport.getResult()+"|"+rechargeReport.getRemark());
			String ret_msg=rechargeReport.getRemark();
			String ret_code=String.valueOf(rechargeReport.getResult());
			data.setMessage(ret_msg);
			data.setResult(ret_code);
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
		// TODO Auto-generated method stub
		JSONDataReader reader = new JSONDataReader();
		reader.init(paramJson);
		password = reader.read("password");
		rechargeUrl = reader.read("rechargeUrl");
		username = reader.read("username");
		reader.release();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		super.release();
	}

	@Override
	public String getParamTemplate() {
		// TODO Auto-generated method stub
		return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"username\":\"账号\"," + "\"password\":\"密码\"" + "}";
	}

	@Override
	protected void loadJsonParams(String paramJson, String... paramNames) {
		// TODO Auto-generated method stub
		super.loadJsonParams(paramJson, paramNames);
	}

}
