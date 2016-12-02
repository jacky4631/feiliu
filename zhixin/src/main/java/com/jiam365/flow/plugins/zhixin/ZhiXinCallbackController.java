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
                JSONObject object = JSON.parseObject(json);
                ZhiXinReport report = new ZhiXinReport();
                report.setAppkey(object.getString("appkey"));
                report.setTimestamp(object.getString("timestamp"));
                report.setSign(object.getString("sign"));
                JSONObject dataObject = object.getJSONObject("data");
                ZhiXinReport.DataReport dataReport = report.new DataReport();
                dataReport.orderid = dataObject.getString("orderid");
                dataReport.message = dataObject.getString("message");
                dataReport.code = dataObject.getString("code");
                dataReport.mobile = dataObject.getString("mobile");
                dataReport.messageid = dataObject.getString("messageid");
                report.setData(dataReport);
                JsonMapper mapper = JsonMapper.nonDefaultMapper();
                TradeReportServiceProxy.save(dataReport.messageid, mapper.toJson(report));
                return "ok";
            } catch (Exception e) {
                return "fail";
            }
        } else {
            return "fail";
        }
    }
}
