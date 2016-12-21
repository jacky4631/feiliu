package com.jiam365.flow.plugins.wuyika;

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
public class WuyikaCallbackController {

    private static Logger logger = LoggerFactory.getLogger(WuyikaCallbackController.class);

    @RequestMapping(value = "wuyika")
    @ResponseBody
    public String callback(HttpServletRequest request) {
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            logger.debug("收到伍壹卡回调报文1 {}", jb.toString());
            String json = new String(jb.toString().trim().getBytes(),"GBK");
            return parse(json);
        } catch (Exception e) {
            return "fail";
        }

    }

    public String parse(String json) {
        logger.debug("收到伍壹卡回调报文2 {}", json);

        WuyikaReport report = new WuyikaReport();
        String[] params = json.split("&");
        for(String param : params) {
            String[] childParam = param.split("=");
            if(childParam.length > 1){
                String key = childParam[0];
                String value = childParam[1];
                if("OrderID".equals(key)){
                    report.setOrderID(value);
                }else if("OrderStatus".equals(key)) {
                    report.setOrderStatus(value);
                }
            }
        }
        if (!StringUtils.isEmpty(report.getOrderStatus()) && !StringUtils.isEmpty(report.getOrderID())) {
            TradeReportServiceProxy.save(report.getOrderID(), JSON.toJSONString(report));
            return "success";
        } else {
            return "fail";
        }
    }
}
