package com.jiam365.flow.plugins.xinyou;

import java.io.BufferedReader;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.mapper.JsonMapper;

@Controller
@RequestMapping(value = "/report")
public class XinYouCallbackController {

	private static Logger logger = LoggerFactory.getLogger(XinYouCallbackController.class);
	
	@RequestMapping(value = "xinyou")
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
		
		String json=jb.toString();
		logger.debug("收到信游互联公司回调json:"+json);

		JSONArray array = JSONArray.parseArray(jb.toString());
		int size=array.size();
		ArrayList<XinYouXunReport> report_array=new ArrayList<>();
		for(int i=0;i<size;i++){
		JSONObject jsonObject=array.getJSONObject(i);
		XinYouXunReport report = new XinYouXunReport();
		report.setStatus(jsonObject.getString("Status"));
		report.setTaskID(jsonObject.getString("TaskID"));
		report.setMobile(jsonObject.getString("Mobile"));
		report.setReportTime(jsonObject.getString("ReportTime"));
		report.setReportCode(jsonObject.getString("ReportCode"));
		report.setOutTradeNo(jsonObject.getString("OutTradeNo"));
		logger.debug("收到信游互联公司回调报文{"+i+"} status {}, OutTradeNo: {}, TaskID: {}, Mobile: {}, ReportCode: {}", report.getStatus(),
				report.getOutTradeNo(), report.getTaskID(),report.getMobile(),report.getReportCode());
		if (StringUtils.isNotBlank(report.getTaskID()) && StringUtils.isNotBlank(report.getStatus())) {
			JsonMapper mapper = JsonMapper.nonDefaultMapper();
			TradeReportServiceProxy.save(report.getTaskID(), mapper.toJson(report));
		} else {
		}
		report_array.add(report);
		}
		
		if (StringUtils.isNotBlank(json)) {
			return "ok";
		} else {
			return "error";
		}
	}

}
