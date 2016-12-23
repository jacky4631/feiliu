/*    */ package com.jiam365.flow.server.service;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.jiam365.flow.server.dao.BlacklistDao;
/*    */ import com.jiam365.flow.server.entity.Blacklist;
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
/*    */ class BlacklistManager$1
/*    */   extends CacheLoader<String, Blacklist>
/*    */ {
/*    */   BlacklistManager$1(BlacklistManager this$0) {}
/*    */   
/*    */   public Blacklist load(String key)
/*    */   {
/* 27 */     Blacklist blacklist = BlacklistManager.access$000(this.this$0).getFirstOne();
/* 28 */     if (blacklist == null) {
/* 29 */       blacklist = new Blacklist();
/*    */     }
/* 31 */     BlacklistManager.access$100(this.this$0, blacklist.getMobiles());
/* 32 */     return blacklist;
/*    */   }
/*    */ }


/* Location:              C:\Users\沈吉\Desktop\com.zip!\com\jiam365\flow\server\service\BlacklistManager$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */