// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import com.jiam365.flow.server.engine.InsufficientBalanceException;
import com.jiam365.flow.server.security.RestAuthorizationException;
import com.jiam365.flow.server.engine.InvalidTradeTimeException;
import com.jiam365.flow.server.engine.TradeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpHeaders;
import java.util.Map;
import org.springframework.http.HttpStatus;
import com.jiam365.modules.tools.BeanValidators;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import javax.validation.ConstraintViolationException;
import com.jiam365.modules.mapper.JsonMapper;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    private static Logger logger;
    private JsonMapper jsonMapper;
    
    public RestExceptionHandler() {
        this.jsonMapper = new JsonMapper();
    }
    
    @ExceptionHandler({ ConstraintViolationException.class })
    public final ResponseEntity<?> handleException(final ConstraintViolationException ex, final WebRequest request) {
        final Map<String, String> errors = (Map<String, String>)BeanValidators.extractPropertyAndMessage(ex.getConstraintViolations());
        final String body = this.jsonMapper.toJson((Object)errors);
        final HttpHeaders headers = this.createHttpHeaders();
        RestExceptionHandler.logger.warn("\u6536\u5230\u9519\u8bef\u7684\u8bf7\u6c42, {}", (Object)body);
        final RestResponse response = new RestResponse("50001", body);
        return (ResponseEntity<?>)this.handleExceptionInternal((Exception)ex, (Object)response, headers, HttpStatus.BAD_REQUEST, request);
    }
    
    @ExceptionHandler({ TradeException.class })
    public final ResponseEntity<?> handleException(final RuntimeException ex, final WebRequest request) {
        final HttpHeaders headers = this.createHttpHeaders();
        final RechargeResponse response = new RechargeResponse("50100", ex.getMessage());
        return (ResponseEntity<?>)this.handleExceptionInternal((Exception)ex, (Object)response, headers, HttpStatus.OK, request);
    }
    
    @ExceptionHandler({ GeneralRestException.class })
    public final ResponseEntity<?> handleException(final GeneralRestException ex, final WebRequest request) {
        final HttpHeaders headers = this.createHttpHeaders();
        final RechargeResponse response = new RechargeResponse(ex.getResultCode(), ex.getResultMessage());
        return (ResponseEntity<?>)this.handleExceptionInternal((Exception)ex, (Object)response, headers, HttpStatus.OK, request);
    }
    
    @ExceptionHandler({ InvalidTradeTimeException.class })
    public final ResponseEntity<?> handleException(final InvalidTradeTimeException ex, final WebRequest request) {
        final HttpHeaders headers = this.createHttpHeaders();
        final RechargeResponse response = new RechargeResponse("50008");
        return (ResponseEntity<?>)this.handleExceptionInternal((Exception)ex, (Object)response, headers, HttpStatus.OK, request);
    }
    
    @ExceptionHandler({ RestAuthorizationException.class })
    public final ResponseEntity<?> handleException(final RestAuthorizationException ex, final WebRequest request) {
        final HttpHeaders headers = this.createHttpHeaders();
        final RestResponse response = new RestResponse("50007", Result.msg("50007"));
        return (ResponseEntity<?>)this.handleExceptionInternal((Exception)ex, (Object)response, headers, HttpStatus.UNAUTHORIZED, request);
    }
    
    @ExceptionHandler({ InsufficientBalanceException.class })
    public final ResponseEntity<?> handleException(final InsufficientBalanceException ex, final WebRequest request) {
        final HttpHeaders headers = this.createHttpHeaders();
        final RechargeResponse response = new RechargeResponse("50005");
        return (ResponseEntity<?>)this.handleExceptionInternal((Exception)ex, (Object)response, headers, HttpStatus.OK, request);
    }
    
    private HttpHeaders createHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        return headers;
    }
    
    static {
        RestExceptionHandler.logger = LoggerFactory.getLogger((Class)RestExceptionHandler.class);
    }
}
