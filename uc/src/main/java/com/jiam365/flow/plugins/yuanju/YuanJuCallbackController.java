package com.jiam365.flow.plugins.yuanju;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.mapper.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

@Controller
@RequestMapping(value = "/report")
public class YuanJuCallbackController {
	
	private static Logger logger = LoggerFactory.getLogger(YuanJuCallbackController.class);
	
	@RequestMapping(value="charge")
	@ResponseBody
	public String callback(HttpServletRequest request) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			/* report an error */
		}

		String json=jb.toString().trim();
		logger.debug("收到缘聚公司回调报文 {}", json);
		if(json != null && !json.equals("")) {
			JSONObject object = JSON.parseObject(json);
			YuanJuReport report = new YuanJuReport();
			report.setRes(object.getString("res"));
			report.setResmsg(object.getString("resmsg"));
			report.setResdata(object.getString("resdata"));
			JsonMapper mapper = JsonMapper.nonDefaultMapper();
			TradeReportServiceProxy.save(report.getResdata(), mapper.toJson(report));
			return "OK";
		} else {
			return "ERR";
		}
	}
}
