/*    */ package com.jiam365.flow.server.service;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.jiam365.flow.server.entity.ChannelProductGroupProfile;
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
/*    */ class FlowPackageManager$1
/*    */   extends CacheLoader<String, ChannelProductGroupProfile>
/*    */ {
/*    */   FlowPackageManager$1(FlowPackageManager this$0) {}
/*    */   
/*    */   public ChannelProductGroupProfile load(String id)
/*    */     throws Exception
/*    */   {
/* 51 */     return this.this$0.getGroupProfile(id);
/*    */   }
/*    */ }


/* Location:              C:\Users\沈吉\Desktop\com.zip!\com\jiam365\flow\server\service\FlowPackageManager$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */