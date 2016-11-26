package com.jiam365.flow.plugins.wuyika;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.response.XMLDataReader;
import com.jiam365.modules.utils.StringIdGenerator;

public class test {
	
	private static String MARK="yunsheng";
	private static String agentid="10001653";
	private static String key="etrt497w65t6ew8t9awqrq";
	private static String rechargeUrl="http://180.96.21.204:8082/onlinepay.do";
	private static String queryUrl="http://180.96.21.204:8082/searchpay.do";
	
	private static Map<String ,String>  Status=new HashMap<>();
	static{
		Status.put("00000", "订购成功");
		Status.put("00001", "订购失败");
		Status.put("00002", "任务处理中");
		Status.put("20000", "签名认证失败");
		Status.put("20009", "客户余额不足");
		Status.put("20010", "产品不存在");
	}
	
	

	public static void main(String[] args) {
//		createOrders(agentid,key);
	}
	
	
	
	

}
