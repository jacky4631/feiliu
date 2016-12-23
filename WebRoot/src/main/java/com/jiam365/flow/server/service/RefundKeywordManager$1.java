/*    */ package com.jiam365.flow.server.service;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import java.util.Set;
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
/*    */ class RefundKeywordManager$1
/*    */   extends CacheLoader<String, Set<String>>
/*    */ {
/*    */   RefundKeywordManager$1(RefundKeywordManager this$0) {}
/*    */   
/*    */   public Set<String> load(String id)
/*    */     throws Exception
/*    */   {
/* 26 */     return RefundKeywordManager.access$000(this.this$0, id);
/*    */   }
/*    */ }


/* Location:              C:\Users\沈吉\Desktop\com.zip!\com\jiam365\flow\server\service\RefundKeywordManager$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */