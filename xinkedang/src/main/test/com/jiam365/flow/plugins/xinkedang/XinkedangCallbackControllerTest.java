package com.jiam365.flow.plugins.xinkedang;

import com.jiam365.modules.mapper.JsonMapper;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class XinkedangCallbackControllerTest extends TestCase {
    private XinkedangCallbackController controller;
    public void setUp() throws Exception {
        super.setUp();
        controller = new XinkedangCallbackController();
    }

    public void tearDown() throws Exception {

    }

    public void testParse() throws Exception {
        XinkedangReport report = new XinkedangReport();
        report.setAppkey("c2c7edba6ef548678de96c785be1cdd6");
        report.setTimestamp("");
        report.setSign("");
        XinkedangReport.DataReport dataReport = report.new DataReport();
        dataReport.message = "message";
        dataReport.mobile = "13402565476";
        dataReport.orderid = "orderid";
        dataReport.code = "1";
        dataReport.messageid = "messageid";
        report.setData(dataReport);
        JsonMapper mapper = new JsonMapper();
        String res = controller.parse(mapper.toJson(report));
        assertEquals(res, "ok");
    }

}