// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.sdk.MobileInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.MobileService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/flow" })
public class MobileInfoController
{
    @Autowired
    private MobileService mobileService;
    
    @RequestMapping(value = { "mobile/{mobile}" }, method = { RequestMethod.GET }, produces = { "application/json" })
    @ResponseBody
    public MobileInfoResponse mobileInfo(@PathVariable("mobile") final String mobile) {
        final MobileInfo info = this.mobileService.mobileInfo(mobile);
        final MobileInfoResponse response = new MobileInfoResponse();
        response.setStatusCascade("10000");
        response.setInfo(info);
        return response;
    }
    
    class MobileInfoResponse extends RestResponse
    {
        private static final long serialVersionUID = -7453417668765046821L;
        private MobileInfo info;
        
        public MobileInfo getInfo() {
            return this.info;
        }
        
        public void setInfo(final MobileInfo info) {
            this.info = info;
        }
    }
}
