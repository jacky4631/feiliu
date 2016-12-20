package com.jiam365.flow.plugins.lingdian;

import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.ResponseData;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class LingdianHandlerTest extends TestCase {
    private LingdianHandler handler;
    public void setUp() throws Exception {
        super.setUp();
        handler = new LingdianHandler();
        handler.loadParams("{\n" +
                "\"rechargeUrl\": \"http://182.92.181.48:9000/pf/api/1.0/order/create-single\"," +
                "\"username\": \"szhh\"," +
                "\"key\": \"822ba8200d58406f86dcebd3bcd17997\"," +
                "\"callbackUrl\": \"\"" +
                "}");

    }

    public void tearDown() throws Exception {

    }

    public void testRecharge() throws Exception {
        RechargeRequest rechargeRequest = new RechargeRequest();
        rechargeRequest.setProductId("NA123456");
        rechargeRequest.setSize(10);
        rechargeRequest.setMobile("13402565476");
        ResponseData responseData = handler.recharge(rechargeRequest);
        System.out.println(responseData);
    }

    public void testCallback() throws Exception {
        String json = "{\"mobile\":\"18684410000\",\"ok\":\"true\",\"result\":\"s\",\"signature\":\"0cfa8e32703a8dd906425f02e95aa08e\",\"tradeNo\":\"1111100000\"}";
        ResponseData responseData = handler.callback(json, "");
        System.out.println(responseData);
    }

}