package com.jiam365.flow.plugins.yunteng;

import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class YuntengCallbackControllerTest extends TestCase {
    private YuntengCallbackController controller;
    public void setUp() throws Exception {
        super.setUp();
        controller = new YuntengCallbackController();
    }

    public void tearDown() throws Exception {

    }

    public void testParse() throws Exception {
        YuntengReport report = new YuntengReport();
        String res = controller.parse("[{\"orderNo\":\"20160331105537\",\"orderNumber\":\"201603170120000107262\",\"mobile\":\"15498785038\",\"status\":\"2\",\"message\":\"充值成功！\"},{\"orderNo\":\"20160331123837\",\"orderNumber\":\"201603170120000123462\",\"mobile\":\"18693940989\",\"status\":\"2\",\"message\":\"充值成功！\"}]");
        assertEquals(res, "ok");
    }

}