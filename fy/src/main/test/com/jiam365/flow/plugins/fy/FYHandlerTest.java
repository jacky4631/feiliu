package com.jiam365.flow.plugins.fy;

import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.ResponseData;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class FYHandlerTest extends TestCase {
    private FYHandler handler;
    public void setUp() throws Exception {
        super.setUp();
        handler = new FYHandler();
        handler.loadParams("{" + "\"rechargeUrl\":\"http://120.25.62.190/dc/flow/cz_new\"," + "\"username\":\"henha\"," + "\"password\":\"henha123\"," + "\"key\":\"ae139edc8ae54e228294408a09379f9e\"" + "}");

    }

    public void tearDown() throws Exception {

    }

    public void testRecharge() throws Exception {
        RechargeRequest rechargeRequest = new RechargeRequest();
        rechargeRequest.setOrigiProductId("10");
        rechargeRequest.setMobile("13402565476");
        ResponseData responseData = handler.recharge(rechargeRequest);
        System.out.println(responseData);
//        assertEquals("000000",responseData.getResult() );
    }

    public void testQueryReport() throws Exception {

    }

}