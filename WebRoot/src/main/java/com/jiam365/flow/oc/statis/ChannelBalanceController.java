// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.statis;

import com.jiam365.flow.server.channel.FlowChannel;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.ChannelAccountManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/statis" })
public class ChannelBalanceController
{
    @Autowired
    private ChannelAccountManager channelAccountManager;
    
    @RequestMapping({ "balance-channels" })
    public String channelsBalance(final Model model) {
        final List<FlowChannel> flowChannelList = this.channelAccountManager.findNotEqualsZeroChannels();
        model.addAttribute("flowChannelList", (Object)flowChannelList);
        return "oc/statis/balance-channels";
    }
}
