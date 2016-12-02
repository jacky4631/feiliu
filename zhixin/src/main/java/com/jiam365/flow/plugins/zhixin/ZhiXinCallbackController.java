package com.jiam365.flow.plugins.zhixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.mapper.JsonMapper;
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
public class ZhiXinCallbackController {

    private static Logger logger = LoggerFactory.getLogger(ZhiXinCallbackController.class);
    private JsonMapper mapper = JsonMapper.nonEmptyMapper();

    @RequestMapping(value = "charge")
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
        logger.debug("收到志新公司回调报文 {}", json);

        if (!StringUtils.isEmpty(json)) {
            try {
                ZhiXinReport report = mapper.fromJson(json, ZhiXinReport.class);
                if(report.getData() != null) {
                    TradeReportServiceProxy.save(report.getData().messageid, json);
                    return "ok";
                }
               return "fail";
            } catch (Exception e) {
                return "fail";
            }
        } else {
            return "fail";
        }
    }
}
