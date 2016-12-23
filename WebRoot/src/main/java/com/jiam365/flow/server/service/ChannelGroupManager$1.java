/*    */ package com.jiam365.flow.server.service;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.jiam365.flow.server.entity.ChannelGroup;
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
/*    */ class ChannelGroupManager$1
/*    */   extends CacheLoader<String, ChannelGroup>
/*    */ {
/*    */   ChannelGroupManager$1(ChannelGroupManager this$0) {}
/*    */   
/*    */   public ChannelGroup load(String id)
/*    */     throws Exception
/*    */   {
/* 28 */     ChannelGroup g = this.this$0.get(id);
/* 29 */     return g == null ? new ChannelGroup() : g;
/*    */   }
/*    */ }


/* Location:              C:\Users\沈吉\Desktop\com.zip!\com\jiam365\flow\server\service\ChannelGroupManager$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */