/*     */ package com.jiam365.flow.server.usercallback;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.entity.StringEntity;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.stringtemplate.v4.ST;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class UsercallbackMessenger$ReportPublisherTask
/*     */   implements Runnable
/*     */ {
/*     */   private UserReport userReport;
/*     */   
/*     */   UsercallbackMessenger$ReportPublisherTask(UsercallbackMessenger this$0, UserReport userReport)
/*     */   {
/*  71 */     this.userReport = userReport;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  76 */     HttpPost httpPost = createPostRequest();
/*  77 */     try { CloseableHttpResponse res = UsercallbackMessenger.access$000(this.this$0).execute(httpPost);Throwable localThrowable3 = null;
/*  78 */       try { int responseCode = res.getStatusLine().getStatusCode();
/*  79 */         String response = StringUtils.trim(EntityUtils.toString(res.getEntity()));
/*  80 */         if ((responseCode == 202) || 
/*  81 */           ("OK".equalsIgnoreCase(response)) || 
/*  82 */           ("SUCC".equalsIgnoreCase(response))) {
/*  83 */           UsercallbackMessenger.access$100().debug("订单{}结果投递成功", this.userReport.getTradeId(), Integer.valueOf(responseCode));
/*     */         } else { String simpleResponse;
/*     */           String simpleResponse;
/*  86 */           if (StringUtils.isBlank(response)) {
/*  87 */             simpleResponse = "[无内容]";
/*     */           } else {
/*  89 */             simpleResponse = response.length() > 15 ? response.substring(0, 15) : response;
/*     */           }
/*  91 */           UsercallbackMessenger.access$200(this.this$0, this.userReport, "应答HTTP状态" + responseCode + "(" + simpleResponse + ")");
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/*  77 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */         if (res != null) if (localThrowable3 != null) try { res.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else res.close();
/*  94 */       } } catch (Exception e) { UsercallbackMessenger.access$200(this.this$0, this.userReport, e.getClass().getCanonicalName() + " " + e.getMessage());
/*  95 */       httpPost.abort();
/*     */     } finally {
/*  97 */       UsercallbackMessenger.access$300(this.this$0).decrementAndGet();
/*     */     }
/*     */   }
/*     */   
/*     */   private HttpPost createPostRequest() {
/* 102 */     String apiUri = this.userReport.getCallbackUrl();
/* 103 */     HttpPost httpPost = new HttpPost(apiUri);
/* 104 */     String json = "{\"reqNo\":\"$reqNo$\",\"userReqNo\":\"$userReqNo$\",\"status\":\"$status$\",\"message\":\"$message$\"}";
/*     */     
/*     */ 
/* 107 */     ST st = new ST(json, '$', '$');
/* 108 */     st.add("reqNo", this.userReport.getTradeId());
/* 109 */     st.add("userReqNo", this.userReport.getUserReqNo());
/* 110 */     st.add("status", this.userReport.getIsSuccess().booleanValue() ? "20000" : "50100");
/* 111 */     st.add("message", this.userReport.getMessage());
/*     */     
/* 113 */     String jsonBody = st.render();
/* 114 */     StringEntity s = new StringEntity(jsonBody, "UTF-8");
/* 115 */     s.setContentType("application/json");
/* 116 */     httpPost.setEntity(s);
/* 117 */     UsercallbackMessenger.access$100().debug("开始投递订单{}的运行结果至{}, 报文:{}", new Object[] { this.userReport.getTradeId(), this.userReport.getCallbackUrl(), jsonBody });
/* 118 */     return httpPost;
/*     */   }
/*     */ }


/* Location:              C:\Users\沈吉\Desktop\com.zip!\com\jiam365\flow\server\usercallback\UsercallbackMessenger$ReportPublisherTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */