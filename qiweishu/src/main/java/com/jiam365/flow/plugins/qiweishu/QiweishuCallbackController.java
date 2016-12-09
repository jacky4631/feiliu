package com.jiam365.flow.plugins.qiweishu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    @RequestMapping(value = "zhixin")
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
                QiweishuReport report = new QiweishuReport();
                QiweishuReport.DataReport dataReport = report.new DataReport();
                JSONObject jsonObject = JSON.parseObject(json);
                report.setSign(jsonObject.getString("sign"));
                report.setAppkey(jsonObject.getString("appkey"));
                report.setTimestamp(jsonObject.getString("timestamp"));
                report.setData(dataReport);
                JSONArray dataArray = jsonObject.getJSONArray("data");
                int dataSize = dataArray.size();
                if(dataSize > 0) {
                    JSONObject dataObject = dataArray.getJSONObject(0);
                    dataReport.code = dataObject.getString("code");
                    dataReport.message = dataObject.getString("message");
                    dataReport.messageid = dataObject.getString("messageid");
                    dataReport.mobile = dataObject.getString("mobile");
                    dataReport.orderid = dataObject.getString("orderid");
                    TradeReportServiceProxy.save(report.getData().messageid, ClientUtils.getJsonMapper().toJson(report));
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
