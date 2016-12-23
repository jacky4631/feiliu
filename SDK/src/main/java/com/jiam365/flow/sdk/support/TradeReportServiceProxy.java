package com.jiam365.flow.sdk.support;

import com.jiam365.modules.tools.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradeReportServiceProxy {
    private static Logger logger = LoggerFactory.getLogger(TradeReportServiceProxy.class);

    public static void save(String key, String json) {
        ITradeReportService service = getService();
        if (service != null) {
            service.save(key, json);
        } else {
            logger.warn("丢失的充值报告, 请手工处理 {}, {}", key, json);
        }
    }

    public static String fetch(String key) {
        ITradeReportService service = getService();
        return service == null ? null : service.get(key);
    }

    private static ITradeReportService getService() {
        ITradeReportService service = null;
        try {
            service = (ITradeReportService) SpringContext.getBean(ITradeReportService.class);
        } catch (Exception e) {
            logger.warn("无法获得ITradeReportService, 可能服务器正在退出");
        }
        return service;
    }
}