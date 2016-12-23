// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.housework;

import org.slf4j.LoggerFactory;
import com.mongodb.DBCollection;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.jiam365.flow.server.utils.DbObjectUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import com.jiam365.flow.server.entity.TradeLog;
import com.jiam365.modules.utils.SimpleDateUtils;
import com.jiam365.flow.server.params.SystemProperties;
import com.jiam365.flow.server.service.TradeRetryManager;
import com.jiam365.flow.server.service.TradeLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.mongodb.MongoClient;
import org.slf4j.Logger;

public class OldTradeLogArchiver
{
    private static Logger logger;
    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private TradeRetryManager tradeRetryManager;
    
    public void execute() {
        if (SystemProperties.isDebug()) {
            return;
        }
        final int month = 3;
        OldTradeLogArchiver.logger.debug("\u5f00\u59cb\u5f52\u6863\u65e7\u4ea4\u6613\u65e5\u5fd7\u6570\u636e, \u5f53\u524d\u5728\u7ebf\u4fdd\u7559\u6700\u591a3\u4e2a\u6708\u7684\u4ea4\u6613\u8bb0\u5f55");
        final Date date = SimpleDateUtils.getLastDayOfxMonthAgo(3);
        final List<TradeLog> tradeLogs = this.tradeLogManager.findOldTradeLog(date, 1000);
        if (tradeLogs.size() > 0) {
            for (final TradeLog log : tradeLogs) {
                this.archive(log);
            }
            OldTradeLogArchiver.logger.debug("\u672c\u6b21\u5f52\u6863" + tradeLogs.size() + "\u6761\u4ea4\u6613\u6570\u636e");
        }
        else {
            OldTradeLogArchiver.logger.debug("\u65e0\u65e7\u6570\u636e\u9700\u8981\u5f52\u6863");
        }
    }
    
    private void archive(final TradeLog tradeLog) {
        try {
            final DB mongoDatabase = this.mongoClient.getDB("flow_archive");
            final DBCollection collection = mongoDatabase.getCollection("trade_log_old");
            final DBObject log = DbObjectUtils.bean2DBObject(tradeLog);
            log.removeField("id");
            log.put("_id", (Object)tradeLog.getId());
            collection.insert(new DBObject[] { log });
            this.tradeLogManager.delete(tradeLog.getId());
            this.tradeRetryManager.remove(tradeLog.getId());
        }
        catch (Exception e) {
            OldTradeLogArchiver.logger.error(e.getMessage());
        }
    }
    
    static {
        OldTradeLogArchiver.logger = LoggerFactory.getLogger((Class)OldTradeLogArchiver.class);
    }
}
