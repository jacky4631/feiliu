package com.jiam365.flow.plugins.wuyika;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.AbstractHandler;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.JSONDataReader;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.response.XMLDataReader;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.modules.mapper.JsonMapper;
import com.jiam365.modules.utils.StringIdGenerator;

public class WuYiKaHandler extends AbstractHandler {

	private String MARK = "YouNaKang";
	private static Logger logger = LoggerFactory.getLogger(WuYiKaHandler.class);
	private static int DEFAULT_TIMEOUT = 1000 * 60;
	private String MerID;
	private String key;
	private String rechargeUrl;
	private String queryUrl;

	private static Map<String, String> Status = new HashMap<>();
	static {
		Status.put("0", "等待充值");
		Status.put("1", "充值成功");
		Status.put("2", "充值中");
		Status.put("9", "充值失败已退款");
	}

	@Override
	public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
		// TODO Auto-generated method stub
		return createOrders(MerID, key, request);
	}

	private ResponseData createOrders(String MerID, String key, RechargeRequest request) {
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setCommand("1");
		dto.setMerID(MerID);
		String orderId = StringIdGenerator.get();
		dto.setOrderID(orderId);
		dto.setChgMobile(request.getMobile());
		dto.setFlowAmount(request.getSize()+"");
		request.getMobileInfo().getProvider().getCode();
		dto.setISP("运营商");
		dto.setReplyFormat("xml");
		dto.setInterfaceNumber("2.1.0.1");
		dto.setMerURL("http://120.55.71.93/report/wuyika");
		String range = String.valueOf(ProductIDHelper.isNationProduct(request.getProductId()) ? "1" : "2");
		dto.setFlowType(range);
		dto.setAttach("ssw");
		dto.generateSignature(key);
		HttpPost method = ClientUtils.getPostMethod(rechargeUrl);
		logger.debug(MARK + "_recharge_url:" + rechargeUrl);
		String o = ClientUtils.getJson(method, dto);
		logger.debug(MARK + "_recharge_xml:" + o);
		XMLDataReader reader = new XMLDataReader();
		reader.init(o);
		String resultno=reader.read("resultno");
		ResponseData data = new ResponseData();
		data.setSuccessValue("0");
		data.setResult(resultno);
		data.setMessage(Status.get(resultno));
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
		data.setSuccessValue("1");
//		data.setRetryValues(new String[] { "0","2"});
		data.setRequestNo(reqNo);
		if (json != null) {
			WuYiKaReport rechargeReport = mapper.fromJson(json, WuYiKaReport.class);
			logger.debug(MARK + "_report_bean:" + rechargeReport.getOrderID()+"|"+rechargeReport.getOrderStatus());
			String resultno=rechargeReport.getOrderStatus();
			data.setMessage(Status.get(resultno));
			data.setResult(resultno);
		} else {
//			data.setResult("0");
//			data.setMessage("没有回调");
//			return Query(userid, key, reqNo);
		}
		return data;
	}

	@Override
	public void loadParams(String paramJson) {
		// TODO Auto-generated method stub
		JSONDataReader reader = new JSONDataReader();
		reader.init(paramJson);
		MerID = reader.read("userid");
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
		return "{" + "\"rechargeUrl\":\"订购接口地址\"," + "\"queryUrl\":\"订购结果批查询地址\"," + "\"userid\":\"商户号\","
				+ "\"key\":\"秘钥\"" + "}";
	}

	@Override
	protected void loadJsonParams(String paramJson, String... paramNames) {
		// TODO Auto-generated method stub
		super.loadJsonParams(paramJson, paramNames);
	}

}
