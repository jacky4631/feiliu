package com.jiam365.flow.plugins.xinkedang;

import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.ResponseData;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class XinkedangHandlerTest extends TestCase {
    private XinkedangHandler handler;
    public void setUp() throws Exception {
        super.setUp();
        handler = new XinkedangHandler();
        handler.loadParams("{"
                + "\"rechargeUrl\":\"http://114.55.173.27:8080/fenxiao-if/General/order\","
                + "\"mid\":\"10031\","
                + "\"Secretkey\":\"70f90994fd58452fbda519a5d3a90a53\","
                + "\"notifyUrl\":\"\"" + "}");

    }

    public void tearDown() throws Exception {

    }

    public void testRecharge() throws Exception {
        RechargeRequest rechargeRequest = new RechargeRequest();
        rechargeRequest.setProductId("123&");
        rechargeRequest.setMobile("13402565476");
        rechargeRequest.setSize(10);
        ResponseData responseData = handler.recharge(rechargeRequest);
        System.out.println(responseData);
    }

    public void testQueryReport() {

    }

}