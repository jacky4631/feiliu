/*    */ package com.jiam365.flow.server.service.statis;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.jiam365.flow.server.dao.TradeLogDao;
/*    */ import com.jiam365.flow.server.utils.DoubleUtils;
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
/*    */ class StatisticsManager$1
/*    */   extends CacheLoader<StatisticsManager.PeriodKey, Double>
/*    */ {
/*    */   StatisticsManager$1(StatisticsManager this$0) {}
/*    */   
/*    */   public Double load(StatisticsManager.PeriodKey period)
/*    */     throws Exception
/*    */   {
/* 87 */     double amount = StatisticsManager.access$000(this.this$0).getBillAmount(period.d1, period.d2);
/* 88 */     double cost = StatisticsManager.access$000(this.this$0).getCostAmount(period.d1, period.d2);
/* 89 */     return Double.valueOf(DoubleUtils.sub(amount, cost));
/*    */   }
/*    */ }


/* Location:              C:\Users\沈吉\Desktop\com.zip!\com\jiam365\flow\server\service\statis\StatisticsManager$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */