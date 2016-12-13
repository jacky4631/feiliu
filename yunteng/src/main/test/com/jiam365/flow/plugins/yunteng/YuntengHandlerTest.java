package com.jiam365.flow.plugins.yunteng;

import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.ResponseData;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class YuntengHandlerTest extends TestCase {
    private YuntengHandler handler;
    public void setUp() throws Exception {
        super.setUp();
        handler = new YuntengHandler();
        handler.loadParams("{"
                + "\"rechargeUrl\":\"http://112.124.9.23:8080/api/submit\","
                + "\"appid\":\"1024\","
                + "\"apiKey\":\"dufpD0DqE4C3vNHjtxz3LdjaYfhMyEI2\","
                + "\"callbackUrl\":\"http://120.55.71.93/report/yunteng\""
                + "}");

    }

    public void tearDown() throws Exception {

    }

    public void testRecharge() throws Exception {
        RechargeRequest rechargeRequest = new RechargeRequest();
        rechargeRequest.setSize(10);
        rechargeRequest.setMobile("13402565476");
        ResponseData responseData = handler.recharge(rechargeRequest);
        System.out.println(responseData);
    }

    public void testQueryReport() throws Exception {
    }

    public void testCallback() throws Exception {
        String json = "{\"failReason\":\"\",\"orderId\":\"123456\",\"channelOrderId\":\"2016xxxx\",\"status\":\"1\"}\n";
        ResponseData responseData = handler.callback(json, "");
        System.out.println(responseData);
    }

}