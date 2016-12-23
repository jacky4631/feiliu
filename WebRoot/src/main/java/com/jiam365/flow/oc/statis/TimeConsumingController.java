// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.statis;

import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.telco.Telco;
import com.jiam365.flow.server.dto.TChannelProductTimeConsuming;
import java.util.Calendar;
import java.util.Date;
import com.jiam365.flow.server.channel.FlowChannel;
import java.util.List;
import org.springframework.ui.Model;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.statis.StatisticsManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/statis" })
public class TimeConsumingController
{
    @Autowired
    private StatisticsManager statisticsManager;
    @Autowired
    private ChannelAdminManager channelAdminManager;
    
    @RequestMapping({ "timeconsuming-state" })
    public String timeConsumingState(final Model model) {
        final List<FlowChannel> channels = this.channelAdminManager.findAll();
        model.addAttribute("channels", (Object)channels);
        return "oc/statis/timeconsuming-state";
    }
    
    @RequestMapping({ "timeconsuming-state-content" })
    public String timeConsumingStateContent(final Model model, final Long channelId, final String provider, final int recentMinutes) {
        try {
            final Date d2 = new Date();
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(d2);
            calendar.add(12, -recentMinutes);
            final Date d3 = calendar.getTime();
            final List<TChannelProductTimeConsuming> timeConsumings = this.statisticsManager.getTimeConsumingAverage(channelId, this.safeValueOf(provider), d3, d2);
            model.addAttribute("timeConsumings", timeConsumings);
        }
        catch (Exception ex) {}
        return "oc/statis/timeconsuming-state-content";
    }
    
    private Telco safeValueOf(final String provider) {
        return StringUtils.isBlank(provider) ? Telco.CMCC : Telco.valueOf(provider);
    }
}
