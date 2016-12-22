package com.jiam365.flow.plugins.sswdahan;

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
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DahanHandler extends AbstractHandler {

	private static String MARK = "DaHan";
	private static Logger logger = LoggerFactory.getLogger(DahanHandler.class);
	private static int DEFAULT_TIMEOUT = 1000 * 60;
	private String account;
	private String pwd;
	private String rechargeUrl;
	private String queryUrl;
	
	
	private static Map<String, String> Status = new HashMap<>();
	static {
		Status.put("ERROR01", "手机号的运营商不能识别");
		Status.put("ERROR02", "不能区分省份");
		Status.put("ERROR03", "充值接口没有配置或暂停");
		Status.put("ERROR04", "充值接口失败");
		Status.put("ERROR05", "没有对应流量包");
		Status.put("ERROR06", "余额不足");
		Status.put("ERROR07", "屏蔽该地区");
		Status.put("ERROR13", "该地市暂不服务");
		Status.put("SUCCESS", "充值成功");
	}

	@Override
	public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
		// TODO Auto-generated method stub
		return createOrders(account, pwd, request);
	}

	private ResponseData createOrders(String user, String pwd, RechargeRequest request) {
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setAccount(account);
		String clientOrderId = StringIdGenerator.get();
		dto.setClientOrderId(clientOrderId);
		dto.setMobiles(encryptMobile(request.getMobile()));
		dto.setPackageSize(request.getSize());
		dto.generateSignature(pwd);
		logger.debug(MARK + "_recharge_url:" + rechargeUrl);
		HttpPost method = ClientUtils.getPostMethod(rechargeUrl);
		JSONObject o = ClientUtils.getJson(method,dto);
		logger.debug(MARK + "_recharge_json:" + o.toString());
		String result = o.getString("resultCode");
		String orderid = o.getString("clientOrderId");
		String desc = o.getString("resultMsg");
		ResponseData data = new ResponseData();
		data.setSuccessValue("00");
		data.setResult(result);
		data.setMessage(desc);
		data.setRequestNo(orderid);
		return data;
	}

	public String encryptMobile(String mobile){
		return MD5.encrypt(mobile);
	}

	@Override
	public ResponseData queryReport(RechargeRequest request, String reqNo) throws ChannelConnectionException {
		// TODO Auto-generated method stub
		String json = TradeReportServiceProxy.fetch(reqNo);
		logger.debug(MARK + "_report_json:" + json);
		ResponseData data = new ResponseData();
		data.setSuccessValue("0");
		data.setRetryValues(new String[] {"0000000"});
		data.setRequestNo(reqNo);
		if (json != null) {
			DahanReport report = JSON.parseObject(json, DahanReport.class);
			logger.debug("收到大汉流量银行公司回调Bean clientOrderId {}, mobile: {}, status: {}, errorCode: {}, errorDesc: {}",
					report.getClientOrderId(),
					report.getMobile(), report.getStatus(), report.getErrorCode(), report.getErrorDesc());
			String message=report.getErrorDesc();
			data.setMessage(message);
			data.setResult(report.getStatus());
		} else {
			data.setResult("0000000");
			data.setMessage("没有回调");
		}
		return data;
	}

	@Override
	public void loadParams(String paramJson) {
		Params params = JSON.parseObject(paramJson, Params.class);
		account = params.getAccount();
		pwd = params.getPwd();
		rechargeUrl = params.getRechargeUrl();
		queryUrl = params.getQueryUrl();
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
		return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"queryUrl\":\"查询地址\"," + "\"account\":\"帐号\","
				 + "\"pwd\":\"密码\"" + "}";
	}

	@Override
	protected void loadJsonParams(String paramJson, String... paramNames) {
		// TODO Auto-generated method stub
		super.loadJsonParams(paramJson, paramNames);
	}

}
