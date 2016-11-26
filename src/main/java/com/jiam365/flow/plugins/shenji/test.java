package com.jiam365.flow.plugins.shenji;

import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.response.XMLDataReader;
import com.jiam365.modules.utils.StringIdGenerator;
import org.apache.http.client.methods.HttpGet;

import java.io.UnsupportedEncodingException;

public class test {

	private static String MARK = "yunsheng";
	private static String agentid = "ec_20161031001";
	private static String key = "123456";
	private static String rechargeUrl = "http://120.76.75.38:8090/interface/flow_recharge.do";
	private static String queryUrl = "http://charge.go10010.cn/recharge/partnerapi/flow/queryOrder.do";

	public static void main(String[] args) {
		createOrders(agentid, key);

		// Field[] fields=OrderCreateRequestDTO.class.getDeclaredFields();
		// String[] fieldNames=new String[fields.length];
		// for(int i=0;i<fields.length;i++){
		// fieldNames[i]=fields[i].getName();
		// }
		// Arrays.sort(fieldNames);
		//
		// for(int i=0;i<fieldNames.length;i++){
		// System.out.println(fieldNames[i]);
		// }

	}

	private static ResponseData createOrders(String ec_code, String key) {
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setEc_code(ec_code);
		String orderId = StringIdGenerator.get();
		dto.setOrder_number(orderId);
		dto.setMobile("18518678763");
		dto.setProduct_code("flow_test");
		dto.generateSignature(key);
		String url = "";
		url = rechargeUrl + "?ec_code=" + dto.getEc_code() + "&order_number=" + dto.getOrder_number() + "&mobile="
				+ dto.getMobile() + "&product_code=" + dto.getProduct_code() + "&sign=" + dto.getSign();

		HttpGet method = ClientUtils.getGetMethod(url);
		System.out.println(MARK + "_recharge_url:" + url);
		String o = ClientUtils.getJson(method);

		try {
			o = new String(o.getBytes("ISO-8859-1"), "utf8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(MARK + "_recharge_xml:" + o);
		XMLDataReader reader = new XMLDataReader();
		reader.init(o);
		String ret_code = reader.read("ret_code");
		String ret_msg = reader.read("ret_msg");
		String req_order_number = reader.read("req_order_number");
		ResponseData data = new ResponseData();
		data.setSuccessValue("0");
		data.setResult(ret_code);
		data.setMessage(ret_msg);
		System.out.println(ret_msg);
		data.setRequestNo(orderId);
		return data;
	}

}
