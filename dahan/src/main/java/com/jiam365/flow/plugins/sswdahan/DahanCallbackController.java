package com.jiam365.flow.plugins.sswdahan;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.mapper.JsonMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping(value="/report")
public class DaHanCallbackController {

	private static Logger logger = LoggerFactory.getLogger(DaHanCallbackController.class);

	@RequestMapping(value="dahan")
	@ResponseBody
	public String callback(HttpServletRequest request) {
		
		
		StringBuffer _sb = new StringBuffer();
		try{
		InputStream _input = request.getInputStream();
		InputStreamReader _reader = new InputStreamReader(_input);
		BufferedReader _buff = new BufferedReader(_reader);
		String _b = null;
		while ((_b = _buff.readLine()) != null) {
			_sb.append(_b + System.getProperty("line.separator"));
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
		logger.debug("收到大汉流量银行公司回调json:"+_sb);
		
		if(_sb==null||_sb.equals("")){
			return "{" + "\"resultCode\":\"1111\","+ "\"resultMsg\":\"处理失败！\"" + "}";
		}

		JSONObject jsonObject = JSONObject.parseObject(_sb.toString());
		DahanReport report = new DahanReport();
		try{
		if(jsonObject.containsKey("clientOrderId"))
		report.setClientOrderId(jsonObject.getString("clientOrderId"));
		
		if(jsonObject.containsKey("mobile"))
		report.setMobile(jsonObject.getString("mobile"));
		
		if(jsonObject.containsKey("reportTime"))
		report.setReportTime(jsonObject.getString("reportTime"));
		
		if(jsonObject.containsKey("callBackTime"))
		report.setCallBackTime(jsonObject.getString("callBackTime"));
		
		if(jsonObject.containsKey("status"))
		report.setStatus(jsonObject.getString("status"));
		
		if(jsonObject.containsKey("errorCode"))
		report.setErrorCode(jsonObject.getString("errorCode"));
		
		if(jsonObject.containsKey("errorDesc"))
		report.setErrorDesc(jsonObject.getString("errorDesc"));
		
		if(jsonObject.containsKey("intervalTime"))
		report.setIntervalTime(jsonObject.getString("intervalTime"));
		
		if(jsonObject.containsKey("clientSubmitTime"))
		report.setClientSubmitTime(jsonObject.getString("clientSubmitTime"));
		
		if(jsonObject.containsKey("discount"))
		report.setDiscount(jsonObject.getString("discount"));
		
		if(jsonObject.containsKey("costMoney"))
		report.setCostMoney(jsonObject.getString("costMoney"));
		}catch(Exception e){
			
		}
		
		logger.debug("收到大汉流量银行公司回调报文 clientOrderId {}, mobile: {}, status: {}, errorCode: {}, errorDesc: {}",
				report.getClientOrderId(),
				report.getMobile(), report.getStatus(), report.getErrorCode(), report.getErrorDesc());
		if (StringUtils.isNotBlank(report.getStatus()) && StringUtils.isNotBlank(report.getClientOrderId())) {
			JsonMapper mapper = JsonMapper.nonDefaultMapper();
			TradeReportServiceProxy.save(report.getClientOrderId(), mapper.toJson(report));
			
			return "{" + "\"resultCode\":\"0000\","+ "\"resultMsg\":\"处理成功！\"" + "}";
		} else {
			return "{" + "\"resultCode\":\"1111\","+ "\"resultMsg\":\"处理失败！\"" + "}";
		}
	}

}
