// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import org.slf4j.LoggerFactory;
import com.jiam365.modules.web.Requests;
import com.jiam365.flow.sdk.MobileInfo;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.flow.server.entity.TradeLog;
import com.jiam365.flow.server.usercallback.SafeReportMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.modules.tools.BeanValidators;
import org.springframework.http.HttpStatus;
import com.jiam365.flow.server.security.RestAuthUtils;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import com.jiam365.flow.server.service.UserManager;
import com.jiam365.flow.server.service.MobileService;
import com.jiam365.flow.server.engine.TradeCenter;
import com.jiam365.flow.server.service.TradeLogManager;
import com.jiam365.flow.server.engine.FlowRouter;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/flowpack" })
public class CompatibleController
{
    private static Logger logger;
    @Autowired
    private Validator validator;
    @Autowired
    private FlowRouter flowRouter;
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private TradeCenter tradeCenter;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private UserManager userManager;
    
    @RequestMapping(value = { "recharge" }, method = { RequestMethod.POST }, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<?> flowRecharge(final CompatibleRechargeApplication rechareApplication, final HttpServletRequest request, final HttpServletResponse httpResponse) {
        if (!this.checkIp(request, RestAuthUtils.getSession())) {
            httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }
        BeanValidators.validateWithException(this.validator, (Object)rechareApplication, new Class[0]);
        final RechareApplication application = new RechareApplication();
        application.setUsername(RestAuthUtils.getSession());
        application.setMobile(rechareApplication.getMobile());
        final String productId = this.getProductId(application.getMobile(), rechareApplication.getPacket(), rechareApplication.getNationwide());
        application.setProductId(productId);
        CompatibleController.logger.info("\u534f\u8bae\u8f6c\u6362, {}==>{} productId:{}", new Object[] { application.getUsername(), application.getMobile(), productId });
        final String serial = this.flowRouter.route(application);
        final CompatibleRechargeResponse response = new CompatibleRechargeResponse("10000", Result.msg("10000"), serial);
        return (ResponseEntity<?>)new ResponseEntity((Object)response, HttpStatus.OK);
    }
    
    @RequestMapping(value = { "query/{serial}" }, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<?> flowRechargeStatus(@PathVariable("serial") final String serial, final HttpServletRequest request, final HttpServletResponse httpResponse) {
        if (!this.checkIp(request, RestAuthUtils.getSession())) {
            httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }
        final RestResponse response = new RestResponse();
        if (StringUtils.isBlank((CharSequence)serial)) {
            return (ResponseEntity<?>)new ResponseEntity((Object)response, HttpStatus.NOT_FOUND);
        }
        if (this.tradeCenter.notFinish(serial)) {
            response.setStatusCascade("10001");
        }
        else {
            final TradeLog tradeLog = this.tradeLogManager.get(serial);
            if (tradeLog == null) {
                response.setStatusCascade("50101");
                return (ResponseEntity<?>)new ResponseEntity((Object)response, HttpStatus.NOT_FOUND);
            }
            if (0 == tradeLog.getResult()) {
                response.setStatusCascade("20000");
            }
            else if (-1 == tradeLog.getResult()) {
                response.setStatus("50100");
                response.setMessage(SafeReportMessage.process(tradeLog.getMessage()));
            }
            else {
                response.setStatusCascade("10001");
            }
        }
        return (ResponseEntity<?>)new ResponseEntity((Object)response, HttpStatus.OK);
    }
    
    private String getProductId(final String mobile, final int size, final boolean nationwide) {
        final MobileInfo info = this.mobileService.mobileInfo(mobile);
        final String scope = nationwide ? "NA" : info.getStateCode();
        return ProductIDHelper.productId(info.getProvider(), scope, size);
    }
    
    private boolean checkIp(final HttpServletRequest request, final String username) {
        final String ip = Requests.srcIp(request);
        final String allowIps = this.userManager.getAllowIps(username);
        return StringUtils.isBlank((CharSequence)allowIps) || allowIps.contains(ip);
    }
    
    static {
        CompatibleController.logger = LoggerFactory.getLogger((Class)CompatibleController.class);
    }
}
