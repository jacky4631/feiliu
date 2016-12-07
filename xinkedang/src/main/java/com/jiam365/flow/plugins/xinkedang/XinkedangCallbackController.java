package com.jiam365.flow.plugins.xinkedang;

import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

@Controller
@RequestMapping(value = "/report")
public class XinkedangCallbackController {

    private static Logger logger = LoggerFactory.getLogger(XinkedangCallbackController.class);

    @RequestMapping(value = "xinkedang")
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
        logger.debug("收到新可当公司回调报文 {}", json);

        if (!StringUtils.isEmpty(json)) {
            try {
                XinkedangReport report = new XinkedangReport();
                SAXReader reader = new SAXReader();
                Document document = reader.read(new StringReader(json));
                Element root = document.getRootElement();
                List<Element> childElements = root.elements();
                for (Element child :childElements)
                {
                    List<Element>result2 = child.elements();
                    for (Element c :result2)
                    {
                        List<Element>c2 = c.elements();
                        for (Element ret :c2)
                        {
                            report.setFs_code(ret.elementText("FS_CODE"));
                            report.setFs_message(ret.elementText("FS_MESSAGE"));
                            report.setSerial_number(ret.elementText("SERIAL_NUMBER"));
                            report.setOrder_id(ret.elementText("ORDER_ID"));
                        }
                    }
                }
                TradeReportServiceProxy.save(report.getOrder_id(), ClientUtils.getJsonMapper().toJson(report));
                return retMsg("0000", "成功");
            } catch (Exception e) {
                return retMsg("1111", "系统异常");
            }
        } else {
            return retMsg("1111", "系统异常");
        }
    }

    public String retMsg(String fs_code, String fs_message) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ns2:noticeResponse xmlns:ns2=\"http://callback.webservice.wujun.com/\">");
        sb.append("<return>");
        sb.append("<FS_CODE>" + fs_code + "</FS_CODE>");
        sb.append("<FS_MESSAGE>" + fs_message + "</FS_MESSAGE>");
        sb.append("</return>");
        sb.append("</ns2:noticeResponse></soap:Body></soap:Envelope>");

        return sb.toString();
    }
}
