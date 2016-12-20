package com.jiam365.flow.plugins.lingdian;

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
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
            /* report an error */
		}

		String json = jb.toString().trim();
		return parse(json);

	}

	public String parse(String json) {
		logger.debug("收到零点回调报文 {}", json);

		if (!StringUtils.isEmpty(json)) {
			LingdianReport report = new LingdianReport();
			String[] params = json.split("&");
			for (String paramObj : params) {
				String[] param = paramObj.split("=");
				String key = param[0];
				String value = param[1];
				if ("tradeNo".equals(key)) {
					report.setTradeNo(value);
				} else if ("mobile".equals(key)) {
					report.setMobile(value);
				} else if ("ok".equals(key)) {
					report.setOk(value);
				} else if ("result".equals(key)) {
					report.setResult(value);
				} else if ("signature".equals(key)) {
					report.setSignature(value);
				}
			}
				TradeReportServiceProxy.save(report.getTradeNo(), JSON.toJSONString(report));
				return "true";

		} else {
			return "false";
		}
	}
}
