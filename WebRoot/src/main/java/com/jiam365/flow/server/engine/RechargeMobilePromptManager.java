// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import com.jiam365.flow.server.entity.SmLog;
import org.slf4j.LoggerFactory;
import com.jiam365.modules.utils.Threads;
import java.util.concurrent.TimeUnit;
import com.jiam365.flow.server.entity.SmConfig;
import com.jiam365.flow.sdk.RechargeRequest;
import java.util.concurrent.Callable;
import org.stringtemplate.v4.ST;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import com.jiam365.flow.server.service.SmService;
import com.jiam365.flow.server.service.SmLogManager;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.params.SmConfigManager;
import org.slf4j.Logger;

public class RechargeMobilePromptManager
{
    private static Logger logger;
    @Autowired
    private SmConfigManager smConfigManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SmLogManager smLogManager;
    @Autowired
    private SmService smService;
    private final ExecutorService executorService;
    
    public RechargeMobilePromptManager() {
        this.executorService = Executors.newFixedThreadPool(2);
    }
    
    public void notify(final Trade trade) {
        final RechargeRequest request = trade.getRequest();
        final SmConfig config = this.smConfigManager.loadSmConfig();
        final String telcoId = ProductIDHelper.code(trade.getRequest().getProductId());
        if (config.isSendSm() && !config.disabledTelco(telcoId)) {
            String content = config.getTemplate();
            if (StringUtils.isBlank((CharSequence)content)) {
                return;
            }
            final ST st = new ST(content, '$', '$');
            st.add("size", (Object)request.getSize());
            content = st.render();
            final String username = trade.getRequest().getUsername();
            if (config.isValid() && this.userManager.sendSmActived(username)) {
                this.executorService.submit(new SmsPromptTask(trade, content));
            }
        }
    }
    
    public void shutdown() {
        Threads.gracefulShutdown(this.executorService, 10, 10, TimeUnit.SECONDS);
        RechargeMobilePromptManager.logger.info("\u5145\u503c\u63d0\u9192\u670d\u52a1\u5173\u95ed");
    }
    
    static {
        RechargeMobilePromptManager.logger = LoggerFactory.getLogger((Class)RechargeMobilePromptManager.class);
    }
    
    class SmsPromptTask implements Callable<Void>
    {
        private Trade trade;
        private String content;
        
        protected SmsPromptTask(final Trade trade, final String content) {
            this.trade = trade;
            this.content = content;
        }
        
        @Override
        public Void call() throws Exception {
            final String username = this.trade.getRequest().getUsername();
            final String mobile = this.trade.getRequest().getMobile();
            if (RechargeMobilePromptManager.this.userManager.sendSmActived(username)) {
                RechargeMobilePromptManager.logger.debug("\u5411{}\u7684\u7528\u6237{}\u53d1\u9001\u5145\u503c\u77ed\u4fe1\u63d0\u9192[{}]", new Object[] { username, this.trade.getRequest().getMobile(), this.content });
                if (RechargeMobilePromptManager.this.smService.sendSm(mobile, this.content)) {
                    this.writeSmPayRecord(this.trade);
                }
            }
            return null;
        }
        
        private void writeSmPayRecord(final Trade trade) {
            final SmLog smLog = new SmLog();
            smLog.setTradeId(trade.getTradeId());
            smLog.setUsername(trade.getRequest().getUsername());
            smLog.setMobile(trade.getRequest().getMobile());
            smLog.setBillAmount(RechargeMobilePromptManager.this.userManager.getSmPrice(trade.getRequest().getUsername()));
            RechargeMobilePromptManager.this.smLogManager.save(smLog);
        }
    }
}
