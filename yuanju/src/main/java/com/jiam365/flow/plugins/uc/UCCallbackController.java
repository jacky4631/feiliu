package com.jiam365.flow.plugins.uc;

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
public class UCCallbackController {
	
	private static Logger logger = LoggerFactory.getLogger(UCCallbackController.class);
	
	@RequestMapping(value="chargesn")
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
		logger.debug("收到统一通信公司回调报文 {}", json);
		if(json != null && !json.equals("")) {
			JSONObject object = JSON.parseObject(json);
			UCReport report = new UCReport();
			report.setPhone(object.getIntValue("phone"));
			report.setGroup(object.getIntValue("group"));
			report.setResult(object.getIntValue("result"));
			report.setRemark(object.getString("remark"));
			report.setPartner_order_no(object.getString("partner_order_no"));
			JsonMapper mapper = JsonMapper.nonDefaultMapper();
			TradeReportServiceProxy.save(report.getPartner_order_no(), mapper.toJson(report));
			return "OK";
		} else {
			return "ERR";
		}
	}
}
