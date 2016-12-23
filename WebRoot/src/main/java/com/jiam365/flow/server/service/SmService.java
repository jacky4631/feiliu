// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import com.jiam365.flow.server.utils.HttpInvokeUtils;
import org.slf4j.LoggerFactory;
import com.jiam365.flow.server.entity.SmConfig;
import com.jiam365.modules.tools.ConfigurationHolder;
import java.text.MessageFormat;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.params.SmConfigManager;
import org.slf4j.Logger;

public class SmService
{
    private static Logger logger;
    @Autowired
    private SmConfigManager smConfigManager;
    
    public boolean sendSm(final String mobile, final String content) {
        String body = "&user={0}&pwd={1}&phone={2}&msg={3}";
        final SmConfig config = this.smConfigManager.loadSmConfig();
        body = MessageFormat.format(body, config.getSmsAccount(), config.getSmsPass(), mobile, content);
        try {
            if (!ConfigurationHolder.properties().getBoolean("debug", false)) {
                SmService.logger.debug("\u53d1\u9001\u4f59\u989d\u63d0\u9192\u77ed\u4fe1, => {} \u5185\u5bb9 {}", (Object)mobile, (Object)content);
                HttpInvokeUtils.post(config.getSmsApiUrl(), body, "application/x-www-form-urlencoded");
                return true;
            }
            SmService.logger.debug("\u8c03\u8bd5\u73af\u5883\u4e0b, \u4e0d\u5b9e\u9645\u53d1\u9001\u77ed\u4fe1,=> {} \u5185\u5bb9{}", (Object)mobile, (Object)content);
        }
        catch (Exception e) {
            SmService.logger.error(e.getMessage());
        }
        return false;
    }
    
    static {
        SmService.logger = LoggerFactory.getLogger((Class)SmService.class);
    }
}
