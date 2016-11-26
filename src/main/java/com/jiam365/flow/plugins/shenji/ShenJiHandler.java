package com.jiam365.flow.plugins.shenji;

import com.jiam365.flow.sdk.AbstractHandler;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.JSONDataReader;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.response.XMLDataReader;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.mapper.JsonMapper;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShenJiHandler extends AbstractHandler {

	private String MARK = "ShenJi";
	private static Logger logger = LoggerFactory.getLogger(ShenJiHandler.class);
	private String password;
	private String rechargeUrl;
	private String username;
	private String area;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
		// TODO Auto-generated method stub
		return createOrders(request);
	}

	private ResponseData createOrders(RechargeRequest request) {
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setUsername(username);
		dto.setPhone(request.getMobile());
		dto.setTimestamp(dateFormat.format(new Date()));
		dto.setArea(area);
		dto.setCapacity(String.valueOf(request.getSize()));
		dto.generateSignature(password);
		String url="";
		url=rechargeUrl+"?area="+dto.getArea()+
				"&username="+dto.getUsername()+
				"&phone="+dto.getPhone()+
				"&capacity="+dto.getCapacity()+
				"&timestamp="+dto.getTimestamp()
				+"&sign="+dto.getSign();
				
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
		String ret_code=reader.read("success");
		String ret_msg=reader.read("message");
		String req_order_number=reader.read("partner_order_no");
		ResponseData data = new ResponseData();
		data.setSuccessValue("0");
		data.setResult(ret_code);
		data.setMessage(ret_msg);
		data.setRequestNo(req_order_number);
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
			logger.debug(MARK + "_report_bean:" + rechargeReport.getSuccess()+"|"+rechargeReport.getMessage());
			String ret_code=String.valueOf(rechargeReport.getSuccess());
			String ret_msg=rechargeReport.getMessage();
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
		password = reader.read("password");
		rechargeUrl = reader.read("rechargeUrl");
		username = reader.read("username");
		area = reader.read("area");
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
		return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"username\":\"账号\"," + "\"password\":\"密码\"" +
                "\"area\":\"流量范围(0:全国包 1:省漫游包 2:省内包)\"" + "}";
	}

	@Override
	protected void loadJsonParams(String paramJson, String... paramNames) {
		// TODO Auto-generated method stub
		super.loadJsonParams(paramJson, paramNames);
	}

}
