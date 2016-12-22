package com.jiam365.flow.plugins.sswdahan;

import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class DahanCallbackControllerTest extends TestCase {
    private DaHanCallbackController controller;
    public void setUp() throws Exception {
        super.setUp();
        controller = new DaHanCallbackController();
    }

    public void tearDown() throws Exception {

    }

    public void testParse() throws Exception {
        DahanReport report = new DahanReport();
//        String res = controller.parse("[{\"orderNo\":\"201612151146351000001\",\"orderNumber\":\"M2016121511463558603024358\",\"mobile\":\"13354050256\",\"status\":\"2\",\"message\":\"\",\"sign\":\"9f92f57312c3c88475adb93a39a91fd1\"}]");
//        assertEquals(res, "ok");
    }

}