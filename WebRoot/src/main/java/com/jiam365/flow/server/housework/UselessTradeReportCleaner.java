// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.housework;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.TradeReportService;
import org.slf4j.Logger;

public class UselessTradeReportCleaner
{
    private static Logger logger;
    @Autowired
    private TradeReportService tradeReportService;
    
    public void execute() {
        UselessTradeReportCleaner.logger.debug("==\u5f00\u59cb\u6e05\u7406\u65e0\u7528\u7684\u4e34\u65f6\u4ea4\u6613\u62a5\u544a==");
        final int count = this.tradeReportService.cleanOldData();
        UselessTradeReportCleaner.logger.debug("==\u7ed3\u675f\u6e05\u7406\u65e0\u7528\u7684\u4e34\u65f6\u4ea4\u6613\u62a5\u544a, \u6e05\u7406" + count + "\u4efd\u65e0\u7528\u62a5\u544a==");
    }
    
    static {
        UselessTradeReportCleaner.logger = LoggerFactory.getLogger((Class)UselessTradeReportCleaner.class);
    }
}
