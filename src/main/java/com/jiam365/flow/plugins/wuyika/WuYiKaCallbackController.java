package com.jiam365.flow.plugins.wuyika;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.mapper.JsonMapper;

@Controller
@RequestMapping(value = "/report")
public class WuYiKaCallbackController {
	
	private static Logger logger = LoggerFactory.getLogger(WuYiKaCallbackController.class);
	
	@RequestMapping(value="sswyounakang")
	@ResponseBody
	public String callback(WuYiKaReport report) {
		logger.debug("收到伍壹卡公司回调报文 OrderID {}, OrderStatus: {}, ChgMobile: {}", report.getOrderID(), report.getOrderStatus(),
				report.getChgMobile());
		if (StringUtils.isNotBlank(report.getOrderID()) && StringUtils.isNotBlank(report.getOrderStatus())) {
			JsonMapper mapper = JsonMapper.nonDefaultMapper();
			TradeReportServiceProxy.save(report.getOrderID(), mapper.toJson(report));
			return "OK";
		} else {
			return "ERR";
		}
	}
}
