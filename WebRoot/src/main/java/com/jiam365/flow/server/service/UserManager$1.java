/*    */ package com.jiam365.flow.server.service;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.jiam365.flow.server.entity.User;
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
/*    */ class UserManager$1
/*    */   extends CacheLoader<String, User>
/*    */ {
/*    */   UserManager$1(UserManager this$0) {}
/*    */   
/*    */   public User load(String username)
/*    */     throws Exception
/*    */   {
/* 61 */     User u = this.this$0.getUserByUsername(username);
/* 62 */     return u == null ? new User() : u;
/*    */   }
/*    */ }


/* Location:              C:\Users\沈吉\Desktop\com.zip!\com\jiam365\flow\server\service\UserManager$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */