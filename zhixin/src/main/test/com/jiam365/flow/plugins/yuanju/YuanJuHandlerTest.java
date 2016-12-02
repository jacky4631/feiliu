package com.jiam365.flow.plugins.yuanju;

import com.jiam365.flow.sdk.RechargeRequest;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class YuanJuHandlerTest extends TestCase {
    private YuanJuHandler handler;
    public void setUp() throws Exception {
        super.setUp();
        handler = new YuanJuHandler();

    }

    public void tearDown() throws Exception {

    }

    public void testRecharge() throws Exception {
        RechargeRequest rechargeRequest = new RechargeRequest();
        handler.recharge(rechargeRequest);
    }

    public void testQueryReport() throws Exception {

    }

}