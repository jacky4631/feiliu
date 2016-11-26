package com.jiam365.flow.plugins.xinyou;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.AbstractHandler;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.JSONDataReader;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.modules.mapper.JsonMapper;
import com.jiam365.modules.utils.StringIdGenerator;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class XinYouXunHandler extends AbstractHandler {

	private static String MARK = "XinYou";
	private static Logger logger = LoggerFactory.getLogger(XinYouXunHandler.class);
	private static int DEFAULT_TIMEOUT = 1000 * 60;
	private String Account;
	private String key;
	private String rechargeUrl;
	private String queryUrl;
	
	private static Map<String, String> Status = new HashMap<>();
	static {
		Status.put("001", "参数错误");
		Status.put("002", "充值号码不合法");
		Status.put("003", "帐号密码错误");
		Status.put("004", "余额不足");
		Status.put("005", "不存在指定流量包");
		Status.put("006", "不支持该地区");
		Status.put("007", "卡号或者密码错误");
		Status.put("008", "该卡已使用过");
		Status.put("009", "该卡不支持(移动/电信/联通)号码");
		Status.put("010", "协议版本错误");
		Status.put("100", "签名验证错误");
		Status.put("999", "其他错误");
		Status.put("4", "状态成功");
		Status.put("5", "状态失败");
		
		
	}

	@Override
	public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
		// TODO Auto-generated method stub
		return createOrders(Account, key, request);
	}
	private  ResponseData createOrders(String account, String key,RechargeRequest request) {
		 OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
	        dto.setAccount(account);
	        String orderId = StringIdGenerator.get();
	        dto.setOutTradeNo(orderId); //本地订单号或者交易号
	        dto.setMobile(request.getMobile()); //单个号码
	        dto.setV("1.1");
	        dto.setAction("charge");
	        dto.setKey(key);
	        dto.setPackage(request.getOrigiProductId());
	        String range = String.valueOf(ProductIDHelper.isNationProduct(request.getProductId()) ? "0" : "1");
	        dto.setRange(range);
	        dto.generateSignature(key);
	        StringBuilder sb = new StringBuilder();
	        sb.append(rechargeUrl).append("?action="+dto.getAction()).append("&v="+dto.getV()).append("&account="+dto.getAccount())
	        .append("&mobile="+dto.getMobile()).append("&package="+request.getOrigiProductId())
	        .append("&sign="+dto.getSign());
	        logger.debug(MARK+"_recharge_sign:"+dto.getSign());
	        logger.debug(MARK+"_recharge_url:"+sb.toString());
	        HttpPost method = ClientUtils.getPostMethod(sb.toString());
	        JSONObject o = ClientUtils.getJson(method, null);
	        logger.debug(MARK+"_recharge_json:"+o.toString());
	        String code = o.getString("Code");
	        String TaskID=o.getString("TaskID");
	        String message =o.getString("Message");
	        ResponseData data = new ResponseData();
			data.setSuccessValue("0");
			data.setResult(code);
			data.setMessage(message);
			data.setRequestNo(TaskID);
			return data;
    }

	@Override
	public ResponseData queryReport(RechargeRequest rechargeRequest, String reqNo) throws ChannelConnectionException {
		// TODO Auto-generated method stub
		return callback(reqNo,rechargeRequest);
	}
	
	private  ResponseData Query(String account, String key,String reqNo,RechargeRequest rechargeRequest) {
        BaseRequestDTO dto = new BaseRequestDTO();
        dto.setAccount(account);
        dto.setV("1.1");
        dto.setAction("getReports");
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String time=format.format(date);
        dto.generateSignature(account,key);
        StringBuilder sb = new StringBuilder();
        sb.append(queryUrl).append("?v="+dto.getV()).append("&action="+dto.getAction()).append("&account="+dto.getAccount())
        .append("&count=5").append("&sign="+dto.getSign());
        HttpPost method = ClientUtils.getPostMethod(sb.toString());
        JSONObject o = ClientUtils.getJson(method, null);
        logger.debug(MARK+"_queryReport_json:",o.toJSONString());
        int code = o.getIntValue("Code");
        String message =o.getString("Message");
        ResponseData data = new ResponseData();
        JSONArray array = o.getJSONArray("Reports");
        if (array.isEmpty()) {
            //没有已完成但是未通知的
//        	data.setResult("1");
//			data.setMessage("充值中");
        	
        } else {
            //具体到每个号码的详情
        	boolean issuccess=false;
            for (int i = 0; i < array.size(); i++) {
                JSONObject result = array.getJSONObject(i);
                String status = result.getString("status");
                String mobile = result.getString("Mobile");
                message +=mobile+"{"+result.getString("message")+"}"; //ok=false才有意义
                //your code...
                if(status.equals("0")&&mobile.equals(rechargeRequest.getMobile())){
                	issuccess=true;
                }
            }
            if(issuccess){
            	data.setResult("0");
				data.setMessage(message);
            }
        }
		data.setMessage(message);
		data.setRequestNo(reqNo);
		return data;
    }
	
	
	private ResponseData callback(String reqNo,RechargeRequest rechargeRequest){
		String json = TradeReportServiceProxy.fetch(reqNo);
		logger.debug(MARK + "_report_json:" + json);
		JsonMapper mapper = JsonMapper.nonEmptyMapper();
		ResponseData data = new ResponseData();
		data.setSuccessValue("4");
		data.setRetryValues(new String[] {"0"});
		data.setRequestNo(reqNo);
		if (json != null) {
			XinYouXunReport report = mapper.fromJson(json, XinYouXunReport.class);
			logger.debug("云之讯公司bean status: {}, OutTradeNo: {}, TaskID: {}, Mobile: {}, ReportCode: {}", report.getStatus(),
					report.getOutTradeNo(), report.getTaskID(),report.getMobile(),report.getReportCode());
			data.setMessage(report.getReportCode());
			data.setResult(report.getStatus());
			logger.debug("云之讯公司result:"+report.getStatus()+"|"+report.getReportCode());
		} else {
//			return Query(Account, key,reqNo,rechargeRequest);
			data.setMessage("回调接收中");
			data.setResult("0");
		}
		return data;
	}

	@Override
	public void loadParams(String paramJson) {
		// TODO Auto-generated method stub
		JSONDataReader reader = new JSONDataReader();
		reader.init(paramJson);
		Account= reader.read("Account");
		key = reader.read("key");
		rechargeUrl =reader.read("rechargeUrl");
		queryUrl =reader.read("queryUrl");
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
		return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"queryUrl\":\"查询地址\"," + "\"Account\":\"帐号\","
		+ "\"key\":\"云之讯分配的秘钥\"" +"}";
	}

	@Override
	protected void loadJsonParams(String paramJson, String... paramNames) {
		// TODO Auto-generated method stub
		super.loadJsonParams(paramJson, paramNames);
	}

}
