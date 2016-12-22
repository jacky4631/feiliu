package com.jiam365.flow.plugins.wuyika;

import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.ResponseData;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class WuyikaHandlerTest extends TestCase {
    private WuyikaHandler handler;
    public void setUp() throws Exception {
        super.setUp();
        handler = new WuyikaHandler();
        handler.loadParams("{"
                + "\"rechargeUrl\":\"http://flowapi.shenzhouka.com/api/FlowCharge.aspx\","
                + "\"merID\":\"2286\","
                + "\"key\":\"38c1315b99d21e1ff738822cba4bf759\","
                + "\"callbackUrl\":\"\""
                +"}");

    }

    public void tearDown() throws Exception {

    }

    public void testRecharge() throws Exception {
        RechargeRequest rechargeRequest = new RechargeRequest();
        rechargeRequest.setProductId("NA8123456");
        rechargeRequest.setSize(10);
        rechargeRequest.setMobile("15109536624");
        ResponseData responseData = handler.recharge(rechargeRequest);
        System.out.println(responseData);
    }

    public void testQueryReport() throws Exception {
        String json = "{\"message\":\"充值成功！\",\"mobile\":\"15498785038\",\"orderNo\":\"20160331105537\",\"orderNumber\":\"201603170120000107262\",\"status\":\"2\"}";
        ResponseData responseData = handler.callback(json, "201603170120000107262");
        System.out.println(responseData);
    }

    public void testCallback() throws Exception {
        String json = "{\"message\":\"\",\"mobile\":\"13354050256\",\"orderNo\":\"201612151146351000001\",\"orderNumber\":\"M2016121511463558603024358\",\"status\":\"2\"}";
        ResponseData responseData = handler.callback(json, "");
        System.out.println(responseData);
    }

}