// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.server.security.RestAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/flow" })
public class BalanceController
{
    @Autowired
    private UserManager userManager;
    
    @RequestMapping(value = { "balance" }, method = { RequestMethod.GET }, produces = { "application/json" })
    @ResponseBody
    public BalanceResponse getBalance() {
        final String username = RestAuthUtils.getSession();
        final double branch = this.userManager.getBalance(username);
        final BalanceResponse response = new BalanceResponse();
        response.setStatusCascade("10000");
        response.setBalance(branch);
        return response;
    }
    
    class BalanceResponse extends RestResponse
    {
        private static final long serialVersionUID = -6807986016842078933L;
        double balance;
        
        public double getBalance() {
            return this.balance;
        }
        
        public void setBalance(final double balance) {
            this.balance = balance;
        }
    }
}
