// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.reportsupport;

import org.slf4j.LoggerFactory;
import com.jiam365.flow.sdk.response.DataReader;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.sdk.response.JSONDataReader;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;
import com.jiam365.flow.server.channel.FlowChannel;
import java.util.List;
import com.jiam365.modules.utils.SimpleDateUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.jiam365.flow.server.service.TradeLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/report/support" })
public class TradeCountQueryController
{
    private static Logger logger;
    @Autowired
    private ChannelAdminManager channelAdminManager;
    @Autowired
    private TradeLogManager tradeLogManager;
    
    @RequestMapping({ "trade/count" })
    @ResponseBody
    public ResponseEntity<?> count(final String subscriberID, String incProdID, final String className, final String queryTimeStart, final String queryTimeEnd) {
        final List<FlowChannel> flowChannels = this.channelAdminManager.findChannelsByHandlerClass(className);
        final FlowChannel channel = this.getBySubscriberID(flowChannels, subscriberID);
        if (channel == null) {
            return (ResponseEntity<?>)new ResponseEntity((Object)0, HttpStatus.OK);
        }
        if ("-1".equals(incProdID)) {
            incProdID = "";
        }
        long count = 0L;
        try {
            final Date sd = DateUtils.parseDate(queryTimeStart, new String[] { "yyyyMMDD" });
            final Date startDate = SimpleDateUtils.getDateStart(sd);
            final Date ed = DateUtils.parseDate(queryTimeEnd, new String[] { "yyyyMMDD" });
            final Date endDate = SimpleDateUtils.getDateEnd(ed);
            count = this.tradeLogManager.getSuccessCount(channel.getId(), incProdID, startDate, endDate);
        }
        catch (Exception e) {
            TradeCountQueryController.logger.warn("\u67e5\u8be2\u6210\u529f\u4ea4\u6613\u7b14\u6570\u65f6\u51fa\u73b0\u9519\u8bef {}", (Object)e.getMessage(), (Object)e);
        }
        return (ResponseEntity<?>)new ResponseEntity((Object)count, HttpStatus.OK);
    }
    
    private FlowChannel getBySubscriberID(final List<FlowChannel> flowChannels, final String subscriberId) {
        for (final FlowChannel flowChannel : flowChannels) {
            final String paramJson = flowChannel.getChannelConnectionParam().getParamJson();
            try {
                final DataReader reader = (DataReader)new JSONDataReader();
                reader.init(paramJson);
                final String theSubscriberID = reader.read("subscriberID");
                reader.release();
                if (StringUtils.isNotBlank((CharSequence)theSubscriberID) && StringUtils.equals((CharSequence)subscriberId, (CharSequence)theSubscriberID)) {
                    return flowChannel;
                }
                continue;
            }
            catch (Exception ex) {}
        }
        return null;
    }
    
    static {
        TradeCountQueryController.logger = LoggerFactory.getLogger((Class)TradeCountQueryController.class);
    }
}
