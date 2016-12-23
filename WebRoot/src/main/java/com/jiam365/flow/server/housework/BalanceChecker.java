// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.housework;

import org.apache.commons.lang3.time.DateFormatUtils;
import java.util.Date;
import org.stringtemplate.v4.ST;
import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.flow.server.channel.FlowChannel;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.server.entity.BalanceNotify;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Cache;
import com.jiam365.flow.server.service.SmService;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import com.jiam365.flow.server.service.ChannelAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.BalanceNotifyManager;

public class BalanceChecker
{
    @Autowired
    private BalanceNotifyManager balanceNotifyManager;
    @Autowired
    private ChannelAccountManager channelAccountManager;
    @Autowired
    private ChannelAdminManager channelAdminManager;
    @Autowired
    private SmService smService;
    private Cache<String, Integer> cache;
    
    public BalanceChecker() {
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.MINUTES).maximumSize(1000L).build();
    }
    
    public void execute() {
        final List<BalanceNotify> notifyList = this.balanceNotifyManager.findAllEnabled();
        for (final BalanceNotify notify : notifyList) {
            if (StringUtils.isNotBlank((CharSequence)notify.getMobiles()) && this.hasEnabledChannel(notify.getChannels()) && this.checkChannelNotifyIsNeeded(notify.getChannels())) {
                final double balance = this.getBalance(notify.getChannels());
                if (balance > notify.getThreshold()) {
                    continue;
                }
                this.doNotify(notify, balance);
            }
        }
    }
    
    private boolean hasEnabledChannel(final List<Long> channelIds) {
        for (final Long id : channelIds) {
            final FlowChannel channel = this.channelAdminManager.get(id);
            if (channel != null && channel.getStatus() == 0) {
                return true;
            }
        }
        return false;
    }
    
    private double getBalance(final List<Long> channelIds) {
        double amount = 0.0;
        for (final Long id : channelIds) {
            amount = DoubleUtils.add(this.channelAccountManager.getBalance(id), amount);
        }
        return DoubleUtils.round(amount, 2);
    }
    
    private boolean checkChannelNotifyIsNeeded(final List<Long> channelIds) {
        final Integer any = (Integer)this.cache.getIfPresent((Object)this.getCombinId(channelIds));
        return any == null;
    }
    
    private String getCombinId(final List<Long> channelIds) {
        final StringBuilder finalId = new StringBuilder(128);
        for (final Long id : channelIds) {
            finalId.append(id);
        }
        return finalId.toString();
    }
    
    private void doNotify(final BalanceNotify notify, final double balance) {
        final String[] split;
        final String[] mobileArray = split = notify.getMobiles().split(",");
        for (final String mobile : split) {
            if (StringUtils.isNotBlank((CharSequence)mobile)) {
                final String content = "\u3010\u6d41\u91cf\u5145\u503c\u3011\u4f9b\u5e94\u5546$title$\u622a\u6b62$timestamp$\u4f59\u989d\u4e3a$balance$\uff0c\u8b66\u6212\u7ebf$threshold$\u5143\uff0c\u8bf7\u6ce8\u610f\u8865\u5145\u4f59\u989d\u3002";
                final ST st = new ST(content, '$', '$');
                st.add("title", (Object)notify.getTitle());
                final String timestamp = DateFormatUtils.format(new Date(), "HH\u70b9mm\u5206");
                st.add("timestamp", (Object)timestamp);
                st.add("balance", (Object)balance);
                st.add("threshold", (Object)notify.getThreshold());
                this.smService.sendSm(mobile, st.render());
                this.cache.put(this.getCombinId(notify.getChannels()), 1);
            }
        }
    }
}
