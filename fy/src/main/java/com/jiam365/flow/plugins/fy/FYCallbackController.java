package com.jiam365.flow.plugins.fy;

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
public class FYCallbackController {

    private static Logger logger = LoggerFactory.getLogger(FYCallbackController.class);

    @RequestMapping(value = "fy")
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
        logger.debug("收到FY公司回调报文 {}", json);

        if (!StringUtils.isEmpty(json)) {
            try {
                FYReport report = new FYReport();
                String[] params = json.split("&");
                for(String param : params) {
                    String[] childParam = param.split("=");
                    String key = childParam[0];
                    String value = childParam[1];
                    if("sign".equals(key)){
                        report.setSgin(value);
                    }else if("timestamp".equals(key)) {
                        report.setTimestamp(value);
                    }else if("code".equals(key)) {
                        report.setCode(value);
                    }else if("orderid".equals(key)) {
                        report.setOrderid(value);
                    }else if("msg".equals(key)) {
                        report.setMsg(URLDecoder.decode(value,"UTF-8"));
                    }
                }
                TradeReportServiceProxy.save(report.getOrderid(), ClientUtils.getJsonMapper().toJson(report));
                return "000000";
            } catch (Exception e) {
                return "999999";
            }
        } else {
            return "999999";
        }
    }
}
