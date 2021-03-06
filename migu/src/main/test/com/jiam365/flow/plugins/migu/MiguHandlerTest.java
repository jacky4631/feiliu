package com.jiam365.flow.plugins.migu;

import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.ResponseData;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class MiguHandlerTest extends TestCase {
    private MiguHandler handler;
    public void setUp() throws Exception {
        super.setUp();
        handler = new MiguHandler();
        handler.loadParams("{" + "\"rechargeUrl\":\"http://101.201.148.20:8666/interface/Api.ashx\"," + "\"account\":\"szhh\"," + "\"apiKey\":\"2dbf29707612194a6de3dd2d3d361023\"" + "}");

    }

    public void tearDown() throws Exception {

    }

    public void testRecharge() throws Exception {
        RechargeRequest rechargeRequest = new RechargeRequest();
        rechargeRequest.setProductId("NA123456");
        rechargeRequest.setOrigiProductId("10M");
        rechargeRequest.setMobile("13402565476");
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