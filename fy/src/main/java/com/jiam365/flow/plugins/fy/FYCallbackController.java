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
                FYReport report = ClientUtils.getJsonMapper().fromJson(json, FYReport.class);
                TradeReportServiceProxy.save(report.getOrderid(), json);
                return "000000";
            } catch (Exception e) {
                return "999999";
            }
        } else {
            return "999999";
        }
    }
}
