package com.jiam365.flow.plugins.shenji;

import com.jiam365.flow.sdk.response.XMLDataReader;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.mapper.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

@Controller
@RequestMapping(value = "/report")
public class ShenJiCallbackController {
	
	private static Logger logger = LoggerFactory.getLogger(ShenJiCallbackController.class);
	
	@RequestMapping(value="shenji")
	@ResponseBody
	public String callback(HttpServletRequest request) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			/* report an error */ }
		
		String xml=jb.toString();
		logger.debug("收到安徽卓威公司回调xml:"+xml);
		
		XMLDataReader reader = new XMLDataReader();
		reader.init(xml);
		ShenJiReport report=new ShenJiReport();
		report.setRet_code(reader.read("ret_code"));
		report.setRet_msg(reader.read("ret_msg"));
		report.setOrder_number(reader.read("oder_number"));
		report.setReq_order_number(reader.read("req_order_number"));
		
		logger.debug("收到安徽卓威公司回调报文 ret_code {}, ret_msg: {}, order_number: {}, req_order_number: {}", report.getRet_code(), report.getRet_msg(),
				report.getOrder_number(),report.getReq_order_number());
		if (StringUtils.isNotBlank(report.getOrder_number()) && StringUtils.isNotBlank(report.getRet_code())) {
			JsonMapper mapper = JsonMapper.nonDefaultMapper();
			TradeReportServiceProxy.save(report.getOrder_number(), mapper.toJson(report));
			return "OK";
		} else {
			return "ERR";
		}
	}
}
