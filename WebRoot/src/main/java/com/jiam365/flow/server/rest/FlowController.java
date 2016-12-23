// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import com.jiam365.modules.web.Requests;
import com.jiam365.flow.server.entity.TradeLog;
import org.springframework.http.HttpStatus;
import com.jiam365.flow.server.usercallback.SafeReportMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.modules.tools.BeanValidators;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Collection;
import com.jiam365.modules.mapper.BeanMapper;
import com.jiam365.flow.server.security.RestAuthUtils;
import com.jiam365.flow.server.dto.TUserProduct;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import com.jiam365.flow.server.service.UserManager;
import com.jiam365.flow.server.product.UserProductManager;
import com.jiam365.flow.server.engine.TradeCenter;
import com.jiam365.flow.server.service.TradeLogManager;
import com.jiam365.flow.server.engine.FlowRouter;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/flow" })
public class FlowController
{
    @Autowired
    private Validator validator;
    @Autowired
    private FlowRouter flowRouter;
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private TradeCenter tradeCenter;
    @Autowired
    private UserProductManager userProductManager;
    @Autowired
    private UserManager userManager;
    
    @RequestMapping(value = { "products/{mobile}" }, produces = { "application/json" })
    @ResponseBody
    public List<TUserProduct> showProducts(@PathVariable("mobile") final String mobile) {
        final String username = RestAuthUtils.getSession();
        return (List<TUserProduct>)BeanMapper.mapList((Collection)this.userProductManager.findStateProducts(username, mobile), (Class)TUserProduct.class);
    }
    
    @RequestMapping(value = { "order" }, method = { RequestMethod.POST }, produces = { "application/json" })
    @ResponseBody
    public RechargeResponse flowRecharge(final RechareApplication rechareApplication, final HttpServletRequest request) {
        BeanValidators.validateWithException(this.validator, (Object)rechareApplication, new Class[0]);
        if (!this.checkIp(request, RestAuthUtils.getSession())) {
            throw new GeneralRestException("50006");
        }
        rechareApplication.username(RestAuthUtils.getSession());
        final String reqNo = this.flowRouter.route(rechareApplication);
        return new RechargeResponse("10000", Result.msg("10000"), reqNo);
    }
    
    @RequestMapping(value = { "query/{reqNo}" }, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<?> flowRechargeStatus(@PathVariable("reqNo") String reqNo, final HttpServletRequest request) {
        final QueryResponse response = new QueryResponse();
        TradeLog tradeLog = null;
        final String sUserNo = request.getHeader("X-Userno");
        if ("true".equalsIgnoreCase(sUserNo)) {
            final String username = RestAuthUtils.getSession();
            tradeLog = this.tradeLogManager.getByUserRequestNo(username, reqNo);
            reqNo = ((tradeLog == null) ? "" : tradeLog.getId());
        }
        response.setReqNo(reqNo);
        if (StringUtils.isBlank((CharSequence)reqNo)) {
            return this.notFound(response);
        }
        if (this.tradeCenter.notFinish(reqNo)) {
            response.setStatusCascade("10001");
        }
        else {
            if (tradeLog == null) {
                tradeLog = this.tradeLogManager.get(reqNo);
            }
            if (tradeLog == null) {
                return this.notFound(response);
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
    
    private ResponseEntity<?> notFound(final QueryResponse response) {
        response.setStatusCascade("50101");
        return (ResponseEntity<?>)new ResponseEntity((Object)response, HttpStatus.NOT_FOUND);
    }
    
    private boolean checkIp(final HttpServletRequest request, final String username) {
        final String ip = Requests.srcIp(request);
        final String allowIps = this.userManager.getAllowIps(username);
        return StringUtils.isBlank((CharSequence)allowIps) || allowIps.contains(ip);
    }
}
