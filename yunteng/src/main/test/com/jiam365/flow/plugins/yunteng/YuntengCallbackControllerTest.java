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
        String res = controller.parse("{\"failReason\":\"\",\"orderId\":\"123456\",\"channelOrderId\":\"2016xxxx\",\"status\":\"0000\"}\n");
        assertEquals(res, "{ \" status \": \"1\" }");
    }

}