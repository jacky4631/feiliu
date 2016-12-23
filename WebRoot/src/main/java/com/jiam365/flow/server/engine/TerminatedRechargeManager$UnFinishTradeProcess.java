/*     */ package com.jiam365.flow.server.engine;
/*     */ 
/*     */ import com.jiam365.flow.sdk.ChannelConnectionException;
/*     */ import com.jiam365.flow.sdk.RechargeRequest;
/*     */ import com.jiam365.flow.sdk.response.ResponseData;
/*     */ import com.jiam365.flow.server.channel.ChannelConnection;
/*     */ import com.jiam365.flow.server.channel.ChannelConnectionManager;
/*     */ import com.jiam365.flow.server.entity.TradeLog;
/*     */ import com.jiam365.flow.server.params.SystemProperties;
/*     */ import com.jiam365.flow.server.service.TradeLogManager;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
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
/*     */ class TerminatedRechargeManager$UnFinishTradeProcess
/*     */   implements Runnable
/*     */ {
/*     */   TerminatedRechargeManager$UnFinishTradeProcess(TerminatedRechargeManager this$0) {}
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*  57 */       if (!SystemProperties.isDebug()) {
/*  58 */         loadAndProcess();
/*     */       }
/*     */     } catch (Exception e) {
/*  61 */       TerminatedRechargeManager.access$000().error("Terminate trade process fail", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void loadAndProcess() {
/*  66 */     List<TradeLog> tradeLogs = TerminatedRechargeManager.access$100(this.this$0).findTerminateTradeLog(100);
/*  67 */     for (TradeLog log : tradeLogs) {
/*  68 */       processTrade(log);
/*     */     }
/*     */   }
/*     */   
/*     */   private void processTrade(TradeLog log) {
/*  73 */     long channelId = log.getChannelId() == null ? 0L : log.getChannelId().longValue();
/*  74 */     ChannelConnection connection = TerminatedRechargeManager.access$200(this.this$0).getConnection(channelId);
/*  75 */     String reqNo = log.getRequestNo();
/*     */     
/*  77 */     if (connection != null) {
/*  78 */       if (StringUtils.isBlank(reqNo)) {
/*  79 */         log.setChannelFinishDate(new Date());
/*  80 */         log.setChannelMessage("提交失败, 没有返回交易ID");
/*  81 */         TerminatedRechargeManager.access$300(this.this$0, log);
/*  82 */         TerminatedRechargeManager.access$100(this.this$0).save(log);
/*     */       } else {
/*  84 */         RechargeRequest request = new RechargeRequest();
/*  85 */         request.setMobile(log.getMobile());
/*  86 */         request.setOrigiProductId(log.getOrigiProductId());
/*  87 */         request.setUsername(log.getUsername());
/*     */         try {
/*  89 */           ResponseData data = connection.queryReport(request, log.getRequestNo());
/*  90 */           if (!data.canRetry()) {
/*  91 */             TerminatedRechargeManager.access$000().info("手工终止的交易{}补取状态得到最终状态{}", log.getId(), data.isSuccess() ? "成功" : "失败");
/*  92 */             if (data.isSuccess()) {
/*  93 */               log.setChannelResult(0);
/*     */             } else {
/*  95 */               TerminatedRechargeManager.access$300(this.this$0, log);
/*     */             }
/*  97 */             log.setChannelFinishDate(new Date());
/*  98 */             log.setChannelMessage("[补取]" + data.getMessage());
/*  99 */             TerminatedRechargeManager.access$100(this.this$0).save(log);
/*     */           } else {
/* 101 */             TerminatedRechargeManager.access$400(this.this$0, log);
/*     */           }
/*     */         } catch (ChannelConnectionException e) {
/* 104 */           TerminatedRechargeManager.access$400(this.this$0, log);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\沈吉\Desktop\com.zip!\com\jiam365\flow\server\engine\TerminatedRechargeManager$UnFinishTradeProcess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */