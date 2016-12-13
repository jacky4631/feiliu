package com.jiam365.flow.plugins.yunteng;

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
import java.util.List;

@Controller
@RequestMapping(value = "/report")
public class YuntengCallbackController {

    private static Logger logger = LoggerFactory.getLogger(YuntengCallbackController.class);

    @RequestMapping(value = "migu")
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
        logger.debug("收到米谷回调报文 {}", json);

        if (!StringUtils.isEmpty(json)) {
            List<YuntengReport> reports = JSON.parseArray(json, YuntengReport.class);
            if(reports != null && reports.size() > 0) {
                YuntengReport report = reports.get(0);
                TradeReportServiceProxy.save(report.getOrderNumber(), JSON.toJSONString(report));
                return "ok";
            } else {
                return "err";
            }


        } else {
            return "err";
        }
    }
}
