// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Set;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.RefundKeywordManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/autorefund" })
public class AutoRefundController
{
    @Autowired
    private RefundKeywordManager refundKeywordManager;
    
    @RequestMapping(value = { "cfg" }, method = { RequestMethod.GET })
    public String cfg(final Model model) {
        final Set<String> keywords = this.refundKeywordManager.loadKeywords();
        model.addAttribute("keywords", (Object)keywords);
        return "oc/refund-keywords";
    }
    
    @RequestMapping({ "addkeyword" })
    @ResponseBody
    public String addKeyword(final String keyword) {
        final int count = this.refundKeywordManager.addKeyword(keyword);
        return "{\"count\":" + count + "}";
    }
    
    @RequestMapping({ "removekeyword" })
    @ResponseBody
    public String removeKeyword(final String keyword) {
        final int count = this.refundKeywordManager.reomveKeyword(keyword);
        return "{\"count\":" + count + "}";
    }
}
