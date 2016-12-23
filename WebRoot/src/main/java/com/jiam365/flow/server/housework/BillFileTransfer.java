// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.housework;

import org.slf4j.LoggerFactory;
import java.util.Calendar;
import java.util.Map;
import com.jiam365.modules.persistent.SearchFilter;
import java.util.ArrayList;
import com.jiam365.flow.server.entity.BillFileAccount;
import com.jiam365.flow.server.entity.TradeLog;
import com.jiam365.modules.persistent.Page;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.net.ZFtpClient;
import com.jiam365.modules.net.ZFtpClientConfig;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import com.jiam365.flow.server.entity.User;
import com.jiam365.modules.utils.FileUtils;
import com.jiam365.modules.tools.ConfigurationHolder;
import com.jiam365.flow.server.params.SystemProperties;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.TradeLogManager;
import org.slf4j.Logger;

public class BillFileTransfer
{
    private static Logger logger;
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private UserManager userManager;
    
    public void execute() {
        BillFileTransfer.logger.debug("==\u5f00\u59cb\u6267\u884c\u5bf9\u8d26\u5355\u9012\u9001\u4efb\u52a1==");
        if (SystemProperties.isDebug()) {
            BillFileTransfer.logger.info("\u8c03\u8bd5\u73af\u5883\u4e0d\u6267\u884c\u4f20\u9001\u5bf9\u8d26\u5355\u64cd\u4f5c");
            return;
        }
        final String path = FileUtils.createPath(ConfigurationHolder.properties().getProperty("download.temp", "/tmp"));
        final Date date = this.getWorkingDate();
        final List<User> users = this.userManager.findNeedBillFileUsers();
        for (final User user : users) {
            String fileName = user.getUsername() + "_" + DateFormatUtils.format(date, "yyyyMMdd") + ".csv";
            String fullFileName = path + "/" + fileName;
            final Page<TradeLog> page = this.preparePage(date, user);
            this.tradeLogManager.dump2CSV(fullFileName, page);
            if (FileUtils.sizeOf(fullFileName) >= 1024000L) {
                fullFileName = FileUtils.zipFile(fullFileName, new boolean[] { true });
                fileName = FilenameUtils.getName(fullFileName);
            }
            try {
                final BillFileAccount account = user.getBillFileAccount();
                if (account.isValid()) {
                    final ZFtpClientConfig config = ZFtpClientConfig.create(account.getHostname(), account.getPort(), account.getUsername(), account.getPassword());
                    config.setRemoteEncoding(account.getRemoteEncoding());
                    config.setPassiveMode(account.isPassiveMode());
                    config.setStyle(account.getStyle());
                    final ZFtpClient ftp = new ZFtpClient(config);
                    ftp.connect();
                    if (StringUtils.isNotBlank((CharSequence)account.getRootPath())) {
                        ftp.changeRemotePath(account.getRootPath());
                    }
                    ftp.upload(fullFileName, fileName);
                    BillFileTransfer.logger.info("\u6210\u529f\u4e0a\u4f20\u7528\u6237{}\u7684\u5bf9\u8d26\u5355{}", (Object)user.getUsername(), (Object)fileName);
                }
            }
            catch (Exception e) {
                BillFileTransfer.logger.warn("\u4e0a\u4f20\u7528\u6237{}\u5bf9\u8d26\u5355\u65f6{}\u51fa\u9519,{}", new Object[] { user.getUsername(), fileName, e.getMessage() });
            }
            FileUtils.deleteFile(fullFileName);
        }
        BillFileTransfer.logger.debug("==\u7ed3\u675f\u5bf9\u8d26\u5355\u9012\u9001\u4efb\u52a1");
    }
    
    private Page<TradeLog> preparePage(final Date date, final User user) {
        final Page<TradeLog> page = (Page<TradeLog>)new Page();
        page.setCurrentPage(1L);
        final List<SearchFilter> filters = new ArrayList<SearchFilter>();
        final String sDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        filters.add(new SearchFilter("EQ_username", user.getUsername()));
        filters.add(new SearchFilter("GED_startDate", sDate));
        filters.add(new SearchFilter("LED_startDate", sDate));
        page.setSearchFilters((List)filters);
        final Map<String, String> sortMap = (Map<String, String>)page.getSortMap();
        sortMap.put("startDate", "desc");
        return page;
    }
    
    private Date getWorkingDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(5, -2);
        return calendar.getTime();
    }
    
    static {
        BillFileTransfer.logger = LoggerFactory.getLogger((Class)BillFileTransfer.class);
    }
}
