package com.jiam365.flow.plugins.lingdian;

import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class LingdianCallbackControllerTest extends TestCase {
    private LingdianCallbackController controller;
    public void setUp() throws Exception {
        super.setUp();
        controller = new LingdianCallbackController();
    }

    public void tearDown() throws Exception {

    }

    public void testParse() throws Exception {
        LingdianReport report = new LingdianReport();
        String res = controller.parse("tradeNo=1111100000&mobile=18684410000&ok=true&result=s&signature=0cfa8e32703a8dd906425f02e95aa08e");
        assertEquals(res, "true");
    }

}