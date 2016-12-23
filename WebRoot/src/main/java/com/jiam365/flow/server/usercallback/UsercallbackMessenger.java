// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.usercallback;

import java.util.List;
import java.util.function.Consumer;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.stringtemplate.v4.ST;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.LoggerFactory;
import com.jiam365.flow.server.params.SystemProperties;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import com.jiam365.modules.utils.Threads;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadFactory;
import com.jiam365.modules.net.HttpClients;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ScheduledFuture;
import org.apache.http.impl.client.CloseableHttpClient;
import java.util.concurrent.ScheduledExecutorService;
import org.slf4j.Logger;

public class UsercallbackMessenger
{
    private static Logger logger;
    private ScheduledExecutorService executorService;
    private UserReportManager userReportManager;
    private CloseableHttpClient httpClient;
    private ScheduledFuture future;
    private AtomicInteger deliverTaskCount;
    private static long MAX_WAITING;
    
    public UsercallbackMessenger() {
        this.deliverTaskCount = new AtomicInteger(0);
        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("callback-exec-%d").build();
        this.executorService = Executors.newScheduledThreadPool(4, threadFactory);
        this.httpClient = HttpClients.create(30000, 30000);
        this.future = this.runPendingReportProcessor();
        UsercallbackMessenger.logger.info("Usercallback Messenger\u542f\u52a8");
    }
    
    public void shutdown() throws IOException {
        UsercallbackMessenger.logger.info("\u6b63\u5728\u5173\u95edUsercallback Messenger...");
        this.future.cancel(true);
        Threads.gracefulShutdown((ExecutorService)this.executorService, 10, 30, TimeUnit.SECONDS);
        this.httpClient.close();
        UsercallbackMessenger.logger.info("Usercallback Messenger\u5173\u95ed");
    }
    
    public void publish(final UserReport userReport) {
        if (!SystemProperties.isDebug()) {
            final ReportPublisherTask task = new ReportPublisherTask(userReport);
            this.executorService.submit(task);
            this.deliverTaskCount.incrementAndGet();
        }
    }
    
    private void pendingReport(final UserReport userReport, final String reason) {
        userReport.setPushFailReason(reason);
        this.userReportManager.saveAgain(userReport);
        UsercallbackMessenger.logger.warn("\u63a8\u9001\u5145\u503c\u4efb\u52a1{}\u7684\u56de\u8c03\u5931\u8d25, {}", (Object)userReport.getTradeId(), (Object)reason);
    }
    
    private ScheduledFuture runPendingReportProcessor() {
        UsercallbackMessenger.logger.info("\u542f\u52a8\u7528\u6237\u56de\u8c03\u52a0\u8f7d\u7ebf\u7a0b, \u8fd0\u884c\u95f4\u9694{}\u79d2", (Object)2);
        return this.executorService.scheduleWithFixedDelay(new ReportDeliverTask(), 5L, 2L, TimeUnit.SECONDS);
    }
    
    public void setUserReportManager(final UserReportManager userReportManager) {
        this.userReportManager = userReportManager;
    }
    
    static {
        UsercallbackMessenger.logger = LoggerFactory.getLogger((Class)UsercallbackMessenger.class);
        UsercallbackMessenger.MAX_WAITING = 500L;
    }
    
    class ReportPublisherTask implements Runnable
    {
        private UserReport userReport;
        
        ReportPublisherTask(final UserReport userReport) {
            this.userReport = userReport;
        }
        
        @Override
        public void run() {
            final HttpPost httpPost = this.createPostRequest();
            try (final CloseableHttpResponse res = UsercallbackMessenger.this.httpClient.execute((HttpUriRequest)httpPost)) {
                final int responseCode = res.getStatusLine().getStatusCode();
                final String response = StringUtils.trim(EntityUtils.toString(res.getEntity()));
                if (responseCode == 202 || "OK".equalsIgnoreCase(response) || "SUCC".equalsIgnoreCase(response)) {
                    UsercallbackMessenger.logger.debug("\u8ba2\u5355{}\u7ed3\u679c\u6295\u9012\u6210\u529f", (Object)this.userReport.getTradeId(), (Object)responseCode);
                }
                else {
                    String simpleResponse;
                    if (StringUtils.isBlank((CharSequence)response)) {
                        simpleResponse = "[\u65e0\u5185\u5bb9]";
                    }
                    else {
                        simpleResponse = ((response.length() > 15) ? response.substring(0, 15) : response);
                    }
                    UsercallbackMessenger.this.pendingReport(this.userReport, "\u5e94\u7b54HTTP\u72b6\u6001" + responseCode + "(" + simpleResponse + ")");
                }
            }
            catch (Exception e) {
                UsercallbackMessenger.this.pendingReport(this.userReport, e.getClass().getCanonicalName() + " " + e.getMessage());
                httpPost.abort();
            }
            finally {
                UsercallbackMessenger.this.deliverTaskCount.decrementAndGet();
            }
        }
        
        private HttpPost createPostRequest() {
            final String apiUri = this.userReport.getCallbackUrl();
            final HttpPost httpPost = new HttpPost(apiUri);
            final String json = "{\"reqNo\":\"$reqNo$\",\"userReqNo\":\"$userReqNo$\",\"status\":\"$status$\",\"message\":\"$message$\"}";
            final ST st = new ST(json, '$', '$');
            st.add("reqNo", (Object)this.userReport.getTradeId());
            st.add("userReqNo", (Object)this.userReport.getUserReqNo());
            st.add("status", (Object)(this.userReport.getIsSuccess() ? "20000" : "50100"));
            st.add("message", (Object)this.userReport.getMessage());
            final String jsonBody = st.render();
            final StringEntity s = new StringEntity(jsonBody, "UTF-8");
            s.setContentType("application/json");
            httpPost.setEntity((HttpEntity)s);
            UsercallbackMessenger.logger.debug("\u5f00\u59cb\u6295\u9012\u8ba2\u5355{}\u7684\u8fd0\u884c\u7ed3\u679c\u81f3{}, \u62a5\u6587:{}", new Object[] { this.userReport.getTradeId(), this.userReport.getCallbackUrl(), jsonBody });
            return httpPost;
        }
    }
    
    class ReportDeliverTask implements Runnable
    {
        @Override
        public void run() {
            if (UsercallbackMessenger.this.deliverTaskCount.get() > UsercallbackMessenger.MAX_WAITING) {
                return;
            }
            try {
                final List<UserReport> reports = UsercallbackMessenger.this.userReportManager.popTasks(50);
                if (UsercallbackMessenger.logger.isDebugEnabled() && reports.size() > 0) {
                    UsercallbackMessenger.logger.debug("\u7528\u6237\u56de\u8c03\u4efb\u52a1\u52a0\u8f7d{}\u4e2a\u5f85\u63a8\u9001\u4efb\u52a1", (Object)reports.size());
                }
                reports.forEach(UsercallbackMessenger.this::publish);
            }
            catch (Exception e) {
                UsercallbackMessenger.logger.error("\u7528\u6237\u56de\u8c03\u52a0\u8f7d\u5931\u8d25, {}", (Object)e.getMessage());
            }
        }
    }
}
