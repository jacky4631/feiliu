package com.jiam365.flow.plugins.qiweishu;

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
        String res = controller.parse("sign=c9131abb60818cd8d02746fe0e32a008&reason=%E6%88%90%E5%8A%9F&status=1&customerOrderId=201612151510281000001&orderId=PO20161215151028533862");
        assertEquals(res, "ok");
    }

}