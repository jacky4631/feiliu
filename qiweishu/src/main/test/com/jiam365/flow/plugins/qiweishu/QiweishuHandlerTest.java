package com.jiam365.flow.plugins.qiweishu;

import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.ResponseData;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class QiweishuHandlerTest extends TestCase {
    private QiweishuHandler handler;
    public void setUp() throws Exception {
        super.setUp();
        handler = new QiweishuHandler();
        handler.loadParams("{" + "\"rechargeUrl\":\"http://114.55.111.129/flux/httpOrder.action\"," + "\"enterpriseCode\":\"E100183\"," + "\"password\":\"9i4avAdF\"" + "}");

    }

    public void tearDown() throws Exception {

    }

    public void testRecharge() throws Exception {
        RechargeRequest rechargeRequest = new RechargeRequest();
        rechargeRequest.setOrigiProductId("P0001");
        rechargeRequest.setMobile("13402565476");
        ResponseData responseData = handler.recharge(rechargeRequest);
        System.out.println(responseData);
    }

    public void testQueryReport() throws Exception {

    }

    public void testCallback() throws Exception {
        String json = "{\"timestamp\":\"20161205215500\",\"sign\":\"80b93f7b1cb9a9f6aa197ca8da19ecfe\",\"data\":{\"orderid\":\"f4a3218f64114181b8b6a4609ac26333\",\"mobile\":\"15257126879\",\"message\":\"充值成功\",\"code\":\"1\",\"messageid\":\"201612052134241000001\"}}";
        handler.callback(json, "");
    }

}