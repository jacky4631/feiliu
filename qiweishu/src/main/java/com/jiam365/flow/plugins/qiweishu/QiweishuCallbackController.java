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
            QiweishuReport report = JSON.parseObject(json, QiweishuReport.class);
            if(report != null) {
                TradeReportServiceProxy.save(report.getOrderId(), json);
                return "{\"success\":true}";
            } else {
                return "{\"success\":false,\"error\":\"报文无法解析\"}";
            }


        } else {
            return "{\"success\":false,\"error\":\"报文为空\"}";
        }
    }
}
