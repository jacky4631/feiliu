/*    */ package com.jiam365.flow.server.params;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.jiam365.flow.server.entity.Params;
/*    */ import com.jiam365.modules.mapper.JsonMapper;
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
/*    */ class ParamsService$1
/*    */   extends CacheLoader<Class<?>, Object>
/*    */ {
/*    */   ParamsService$1(ParamsService this$0) {}
/*    */   
/*    */   public Object load(Class<?> clazz)
/*    */   {
/* 24 */     Params params = this.this$0.get(clazz);
/* 25 */     JsonMapper mapper = JsonMapper.nonEmptyMapper();
/* 26 */     return mapper.fromJson(params.getObjJson(), clazz);
/*    */   }
/*    */ }


/* Location:              C:\Users\沈吉\Desktop\com.zip!\com\jiam365\flow\server\params\ParamsService$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */