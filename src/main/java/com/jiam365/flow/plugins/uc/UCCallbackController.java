package com.jiam365.flow.plugins.uc;

import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.mapper.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/report")
public class UCCallbackController {
	
	private static Logger logger = LoggerFactory.getLogger(UCCallbackController.class);
	
	@RequestMapping(value="chargesn")
	@ResponseBody
	public String callback(UCReport report) {
		logger.debug("收到统一通信公司回调报文 success {}", report.getSuccess());
		if (report.getSuccess() == 1) {
			JsonMapper mapper = JsonMapper.nonDefaultMapper();
			TradeReportServiceProxy.save(report.getPartner_order_no(), mapper.toJson(report));
			return "OK";
		} else {
			return "ERR";
		}
	}
}
