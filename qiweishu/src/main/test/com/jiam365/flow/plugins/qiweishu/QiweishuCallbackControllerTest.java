package com.jiam365.flow.plugins.qiweishu;

import com.jiam365.modules.mapper.JsonMapper;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class QiweishuCallbackControllerTest extends TestCase {
    private QiweishuCallbackController controller;
    public void setUp() throws Exception {
        super.setUp();
        controller = new QiweishuCallbackController();
    }

    public void tearDown() throws Exception {

    }

    public void testParse() throws Exception {
        QiweishuReport report = new QiweishuReport();
        report.setAppkey("c2c7edba6ef548678de96c785be1cdd6");
        report.setTimestamp("");
        report.setSign("");
        QiweishuReport.DataReport dataReport = report.new DataReport();
        dataReport.message = "message";
        dataReport.mobile = "13402565476";
        dataReport.orderid = "orderid";
        dataReport.code = "1";
        dataReport.messageid = "messageid";
//        report.setData(dataReport);
        JsonMapper mapper = new JsonMapper();
//        String res = controller.parse(mapper.toJson(report));
        String res = controller.parse("{\"timestamp\":\"20161205215500\",\"sign\":\"80b93f7b1cb9a9f6aa197ca8da19ecfe\",\"data\":[{\"message\":\"充值成功\",\"code\":\"1\",\"messageid\":\"201612052134241000001\",\"orderid\":\"f4a3218f64114181b8b6a4609ac26333\",\"mobile\":\"15257126879\"}],\"apikey\":\"c2c7edba6ef548678de96c785be1cdd6\"}");
        assertEquals(res, "ok");
    }

}