/*    */ package com.jiam365.flow.server.engine;
/*    */ 
/*    */ import com.jiam365.flow.sdk.RechargeRequest;
/*    */ import com.jiam365.flow.server.entity.SmLog;
/*    */ import com.jiam365.flow.server.service.SmLogManager;
/*    */ import com.jiam365.flow.server.service.SmService;
/*    */ import com.jiam365.flow.server.service.UserManager;
/*    */ import java.util.concurrent.Callable;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class RechargeMobilePromptManager$SmsPromptTask
/*    */   implements Callable<Void>
/*    */ {
/*    */   private Trade trade;
/*    */   private String content;
/*    */   
/*    */   protected RechargeMobilePromptManager$SmsPromptTask(RechargeMobilePromptManager this$0, Trade trade, String content)
/*    */   {
/* 73 */     this.trade = trade;
/* 74 */     this.content = content;
/*    */   }
/*    */   
/*    */   public Void call() throws Exception
/*    */   {
/* 79 */     String username = this.trade.getRequest().getUsername();
/* 80 */     String mobile = this.trade.getRequest().getMobile();
/* 81 */     if (RechargeMobilePromptManager.access$000(this.this$0).sendSmActived(username)) {
/* 82 */       RechargeMobilePromptManager.access$100().debug("向{}的用户{}发送充值短信提醒[{}]", new Object[] { username, this.trade.getRequest().getMobile(), this.content });
/* 83 */       if (RechargeMobilePromptManager.access$200(this.this$0).sendSm(mobile, this.content)) {
/* 84 */         writeSmPayRecord(this.trade);
/*    */       }
/*    */     }
/* 87 */     return null;
/*    */   }
/*    */   
/*    */   private void writeSmPayRecord(Trade trade) {
/* 91 */     SmLog smLog = new SmLog();
/* 92 */     smLog.setTradeId(trade.getTradeId());
/* 93 */     smLog.setUsername(trade.getRequest().getUsername());
/* 94 */     smLog.setMobile(trade.getRequest().getMobile());
/*    */     
/* 96 */     smLog.setBillAmount(RechargeMobilePromptManager.access$000(this.this$0).getSmPrice(trade.getRequest().getUsername()));
/* 97 */     RechargeMobilePromptManager.access$300(this.this$0).save(smLog);
/*    */   }
/*    */ }


/* Location:              C:\Users\沈吉\Desktop\com.zip!\com\jiam365\flow\server\engine\RechargeMobilePromptManager$SmsPromptTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */