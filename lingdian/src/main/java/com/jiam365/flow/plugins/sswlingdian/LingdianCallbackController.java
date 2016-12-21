package com.jiam365.flow.plugins.sswlingdian;

import com.alibaba.fastjson.JSON;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
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
public class LingdianCallbackController {

	private static Logger logger = LoggerFactory.getLogger(LingdianCallbackController.class);

	@RequestMapping(value="lingdian")
	@ResponseBody
	public String callback(HttpServletRequest request) {
		LingdianReport report=new LingdianReport();
		report.setTradeNo(request.getParameter("tradeNo"));
		report.setOk(request.getParameter("ok"));
		report.setMobile(request.getParameter("mobile"));
		report.setResult(request.getParameter("result"));
		report.setSignature(request.getParameter("signature"));
		return parse(report);

	}

	public String parse(LingdianReport report) {
		logger.debug("收到零点回调报文 {}", report.toString());
		if (StringUtils.isNotBlank(report.getTradeNo()) && StringUtils.isNotBlank(report.getResult())) {
			TradeReportServiceProxy.save(report.getTradeNo(), JSON.toJSONString(report));
			return "true";
		} else {
			return "false";
		}
	}
}
