package com.jiam365.flow.plugins.shenji;

import com.jiam365.flow.sdk.AbstractHandler;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.JSONDataReader;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.response.XMLDataReader;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.mapper.JsonMapper;
import com.jiam365.modules.utils.StringIdGenerator;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class ShenJiHandler extends AbstractHandler {

	private String MARK = "ShenJi";
	private static Logger logger = LoggerFactory.getLogger(ShenJiHandler.class);
	private static int DEFAULT_TIMEOUT = 1000 * 60;
	private String ec_code;
	private String key;
	private String rechargeUrl;
	private String queryUrl;

	@Override
	public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
		// TODO Auto-generated method stub
		return createOrders(ec_code, key, request);
	}

	private ResponseData createOrders(String ec_code, String key, RechargeRequest request) {
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setEc_code(ec_code);
		String orderId = StringIdGenerator.get();
		dto.setOrder_number(orderId);
		dto.setMobile(request.getMobile());
		dto.setProduct_code(request.getOrigiProductId());
		dto.generateSignature(key);
		String url="";
		url=rechargeUrl+"?ec_code="+dto.getEc_code()+
				"&order_number="+dto.getOrder_number()+
				"&mobile="+dto.getMobile()+
				"&product_code="+dto.getProduct_code()+
				"&sign="+dto.getSign();
				
		HttpGet method = ClientUtils.getGetMethod(url);
		logger.debug(MARK + "_recharge_url:" + url);
		String o = ClientUtils.getJson(method);
		try {
			o = new String(o.getBytes("ISO-8859-1"), "utf8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug(MARK + "_recharge_xml:" + o);
		XMLDataReader reader = new XMLDataReader();
		reader.init(o);
		String ret_code=reader.read("ret_code");
		String ret_msg=reader.read("ret_msg");
		String req_order_number=reader.read("req_order_number");
		ResponseData data = new ResponseData();
		data.setSuccessValue("0");
		data.setResult(ret_code);
		data.setMessage(ret_msg);
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
		data.setSuccessValue("0");
		data.setRetryValues(new String[] { "-2"});
		data.setRequestNo(reqNo);
		if (json != null) {
			ShenJiReport rechargeReport = mapper.fromJson(json, ShenJiReport.class);
			logger.debug(MARK + "_report_bean:" + rechargeReport.getRet_code()+"|"+rechargeReport.getRet_msg());
			String ret_code=rechargeReport.getRet_code();
			String ret_msg=rechargeReport.getRet_msg();
			data.setMessage(ret_msg);
			data.setResult(ret_code);
		} else {
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
		ec_code = reader.read("ec_code");
		key = reader.read("key");
		rechargeUrl = reader.read("rechargeUrl");
		queryUrl = reader.read("queryUrl");
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
		return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"queryUrl\":\"订购结果批查询地址\"," + "\"ec_code\":\"商户号\","
				+ "\"key\":\"秘钥\"" + "}";
	}

	@Override
	protected void loadJsonParams(String paramJson, String... paramNames) {
		// TODO Auto-generated method stub
		super.loadJsonParams(paramJson, paramNames);
	}

}
