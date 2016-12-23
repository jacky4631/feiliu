// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.modules.utils.StringIdGenerator;
import com.jiam365.flow.sdk.RechargeRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/mock" })
public class WMock
{
    private static int i;
    
    @RequestMapping(value = { "recharge" }, method = { RequestMethod.POST }, produces = { "application/json" })
    @ResponseBody
    public String flowRecharge(final RechargeRequest rechargeRequest) {
        final String id = String.valueOf(StringIdGenerator.get());
        return "{\"message\":\"\u64cd\u4f5c\u6210\u529f\",\"status\":\"1000\",\"request_no\":\"" + id + "\"" + "}";
    }
    
    @RequestMapping(value = { "query" }, method = { RequestMethod.POST }, produces = { "application/json" })
    @ResponseBody
    public String flowRechargeStatus() {
        final int x = WMock.i++ % 3;
        if (x == 0) {
            return "{\"message\":\"\u8d60\u9001\u4e2d\",\"status\":\"1005\"}";
        }
        if (x == 1) {
            return "{\"message\":\"\u64cd\u4f5c\u6210\u529f\",\"status\":\"1000\"}";
        }
        return "{\"message\":\"\u6a21\u62df\u5145\u503c\u5931\u8d25\",\"status\":\"1001\"}";
    }
    
    @RequestMapping(value = { "query-always" }, method = { RequestMethod.POST }, produces = { "application/json" })
    @ResponseBody
    public String flowRechargeStatus2() {
        return "{\"message\":\"\u8d60\u9001\u4e2d\",\"status\":\"1005\"}";
    }
    
    @RequestMapping(value = { "query-success" }, method = { RequestMethod.POST }, produces = { "application/json" })
    @ResponseBody
    public String flowRechargeSuccess() {
        return "{\"message\":\"\u64cd\u4f5c\u6210\u529f\",\"status\":\"1000\"}";
    }
    
    @RequestMapping(value = { "query-fail" }, method = { RequestMethod.POST }, produces = { "application/json" })
    @ResponseBody
    public String flowRechargeFail() {
        return "{\"message\":\"\u6a21\u62df\u5145\u503c\u5931\u8d25\",\"status\":\"1001\"}";
    }
    
    static {
        WMock.i = 1;
    }
}
