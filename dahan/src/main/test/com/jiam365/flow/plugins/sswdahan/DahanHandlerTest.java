package com.jiam365.flow.plugins.sswdahan;

import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.ResponseData;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class DahanHandlerTest extends TestCase {
    private DahanHandler handler;
    public void setUp() throws Exception {
        super.setUp();
        handler = new DahanHandler();
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

    public void testCallback() throws Exception {
        String json = "{\"message\":\"\",\"mobile\":\"13354050256\",\"orderNo\":\"201612151146351000001\",\"orderNumber\":\"M2016121511463558603024358\",\"status\":\"2\"}";
//        ResponseData responseData = handler.callback(json, "");
//        System.out.println(responseData);
    }
    public void testEncryptMobile() throws Exception {

        String responseData = handler.encryptMobile("18621764382");
        System.out.println(responseData);
    }

}