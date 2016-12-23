// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.slf4j.LoggerFactory;
import com.jiam365.flow.server.entity.TradeReport;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.TradeReportDao;
import org.slf4j.Logger;
import com.jiam365.flow.sdk.support.ITradeReportService;

public class TradeReportService implements ITradeReportService
{
    private static Logger logger;
    @Autowired
    private TradeReportDao tradeReportDao;
    private static TradeReportService instance;
    
    public void save(final TradeReport report) {
        try {
            this.tradeReportDao.save((Object)report);
        }
        catch (Exception e) {
            TradeReportService.logger.error("\u4fdd\u5b58\u4e34\u65f6\u62a5\u544a{}\u51fa\u73b0\u9519\u8bef", (Object)report.getJson(), (Object)e);
        }
    }
    
    public void save(final String key, final String json) {
        final TradeReport report = new TradeReport(key, json);
        this.save(report);
    }
    
    public String get(final String key) {
        TradeReport report = null;
        try {
            report = this.fetch(key);
        }
        catch (Exception ex) {}
        if (report != null) {
            return report.getJson();
        }
        return null;
    }
    
    public TradeReport fetch(final String key) {
        final TradeReport report = (TradeReport)this.tradeReportDao.get((Object)key);
        if (report != null) {
            this.tradeReportDao.deleteById((Object)key);
        }
        return report;
    }
    
    public int cleanOldData() {
        return this.tradeReportDao.cleanOldData();
    }
    
    public void selfRegister() {
        TradeReportService.instance = this;
    }
    
    public static TradeReportService getInstance() {
        if (TradeReportService.instance == null) {
            throw new RuntimeException("TradeReportService is null");
        }
        return TradeReportService.instance;
    }
    
    static {
        TradeReportService.logger = LoggerFactory.getLogger((Class)TradeReportService.class);
    }
}
