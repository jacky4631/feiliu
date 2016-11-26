//package com.jiam365.flow.plugins.uc;
//
//import com.jiam365.flow.sdk.response.ResponseData;
//import com.jiam365.flow.sdk.response.XMLDataReader;
//import org.apache.http.client.methods.HttpGet;
//
//import java.io.UnsupportedEncodingException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class Test {
//	private static String MARK = "UC";
//	private static String rechargeUrl = "https://www.51liuliang.cc/api/accounts/chargeSingleNumber";
//	private static String password = "12345678";
//	private static String username = "13151176880";
//	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//
//	public static void main(String[] args) {
//		createOrders("13402565476", dateFormat.format(new Date()), "0", "10");
//
////		String sign = MD5.SHA1("aaaaa,15939393939|13878787878,10,20,20160223120445,7b21848ac9af35be0ddb2d6b9fc3851934db8420");
////		logger.info(MARK, sign);
//	}
//
//	private static ResponseData createOrders(String phone, String timestamp,String area, String capacity) {
//		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
//		dto.setUsername(username);
//		dto.setPhone(phone);
//		dto.setTimestamp(timestamp);
//		dto.setArea(area);
//		dto.setCapacity(capacity);
//		dto.generateSignature(password);
//		String url="";
//		url=rechargeUrl+"?area="+dto.getArea()+
//				"&username="+dto.getUsername()+
//				"&phone="+dto.getPhone()+
//				"&capacity="+dto.getCapacity()+
//				"&timestamp="+dto.getTimestamp()
//				+"&sign="+dto.getSign();
//
//		HttpGet method = ClientUtils.getGetMethod(url);
//		System.out.println(MARK + "_recharge_url:" + url);
//		String o = ClientUtils.getJson(method);
//
//		try {
//			o = new String(o.getBytes("ISO-8859-1"), "utf8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		System.out.println(MARK + "_recharge_xml:" + o);
//		XMLDataReader reader = new XMLDataReader();
//		reader.init(o);
//		String ret_code = reader.read("success");
//		String ret_msg = reader.read("message");
//		String req_order_number = reader.read("partner_order_no");
//		ResponseData data = new ResponseData();
//		data.setSuccessValue("0");
//		data.setResult(ret_code);
//		data.setMessage(ret_msg);
//		System.out.println(ret_msg);
//		data.setRequestNo(req_order_number);
//		return data;
//	}
//
//}
