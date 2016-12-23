// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.params;

import com.jiam365.flow.server.entity.SmTemplate;
import com.jiam365.flow.server.entity.SmConfig;
import com.jiam365.flow.server.dao.SmTemplateDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.SmConfigDao;

public class SmConfigManager
{
    @Autowired
    private SmConfigDao smConfigDao;
    @Autowired
    private SmTemplateDao smTemplateDao;
    private SmConfig configCache;
    
    public SmConfig loadSmConfig() {
        synchronized (this) {
            if (this.configCache == null) {
                this.configCache = this.smConfigDao.getSetting();
            }
        }
        return this.configCache;
    }
    
    public void saveSmConfig(final SmConfig smConfig) {
        this.smConfigDao.save(smConfig);
        synchronized (this) {
            this.configCache = smConfig;
        }
    }
    
    public void saveTemplate(final SmTemplate SmTemplate) {
        this.smTemplateDao.save(SmTemplate);
    }
    
    public SmTemplate getTemplate(final long userId) {
        SmTemplate template = (SmTemplate)this.smTemplateDao.get(userId);
        if (template == null) {
            final SmConfig setting = this.smConfigDao.getSetting();
            template = new SmTemplate(userId);
            template.setContent(setting.getTemplate());
        }
        return template;
    }
}
