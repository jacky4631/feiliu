/*     */ package com.jiam365.flow.server.service.statis;
/*     */ 
/*     */ import java.util.Date;
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
/*     */ class StatisticsManager$PeriodKey
/*     */ {
/*     */   Date d1;
/*     */   Date d2;
/*     */   
/*     */   StatisticsManager$PeriodKey(Date[] period)
/*     */   {
/*  98 */     this.d1 = period[0];
/*  99 */     this.d2 = period[1];
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 104 */     if (this == o)
/* 105 */       return true;
/* 106 */     if (!(o instanceof PeriodKey)) {
/* 107 */       return false;
/*     */     }
/* 109 */     PeriodKey periodKey = (PeriodKey)o;
/* 110 */     return (this.d1.equals(periodKey.d1)) && (this.d2.equals(periodKey.d2));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 115 */     int result = this.d1.hashCode();
/* 116 */     result = 31 * result + this.d2.hashCode();
/* 117 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\沈吉\Desktop\com.zip!\com\jiam365\flow\server\service\statis\StatisticsManager$PeriodKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */