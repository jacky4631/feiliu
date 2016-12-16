package com.jiam365.flow.plugins.qiweishu;

import com.alibaba.fastjson.JSON;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.net.URLDecoder;

@Controller
@RequestMapping(value = "/report")
public class QiweishuCallbackController {

    private static Logger logger = LoggerFactory.getLogger(QiweishuCallbackController.class);

    @RequestMapping(value = "qiweishu")
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
        logger.debug("收到七位数公司回调报文 {}", json);

        if (!StringUtils.isEmpty(json)) {
            QiweishuReport report = new QiweishuReport();
            try {
                String[] params = json.split("&");
                for (String paramObj : params) {
                    String[] param = paramObj.split("=");
                    String key = param[0];
                    String value = param[1];
                    if ("orderId".equals(key)) {
                        report.setOrderId(value);
                    } else if ("customerOrderId".equals(key)) {
                        report.setCustomerOrderId(value);
                    } else if ("status".equals(key)) {
                        report.setStatus(value);
                    } else if ("reason".equals(key)) {
                        report.setReason(URLDecoder.decode(value,"UTF-8"));
                    } else if ("sign".equals(key)) {
                        report.setSign(value);
                    }
                }
                TradeReportServiceProxy.save(report.getOrderId(), JSON.toJSONString(report));
                return "{\"success\":true}";

            } catch (Exception e) {
                return "{\"success\":false,\"error\":\"报文无法解析\"}";
            }

        } else {
            return "{\"success\":false,\"error\":\"报文为空\"}";
        }
    }
}
