// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.modules.utils.Identities;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_transferlog", noClassnameStored = true)
public class TransferLog implements Serializable
{
    private static final long serialVersionUID = -8816826513093160065L;
    @Id
    private String id;
    @Indexed
    private String operator;
    @Indexed
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date operateTime;
    @Indexed
    private String username;
    private String displayName;
    private double amount;
    private double balance;
    private String remark;
    private int type;
    public static final int A_SUBJECT_INCOME = 1;
    public static final int A_SUBJECT_EXPENSES = 9;
    public static final int A_SUBJECT_SPERATOR_FLAG = 100;
    public static final int A_SUBJECT_TRANSFER = 102;
    public static final int A_SUBJECT_BALANCE = 103;
    public static final int A_SUBJECT_IN_FORFEIT = 104;
    public static final int A_SUBJECT_OUT_FORFEIT = 105;
    public static final int A_SUBJECT_REFUND = 106;
    public static final int A_SUBJECT_SMS_ITEM = 107;
    @Indexed
    private int accountingSubject;
    
    public TransferLog() {
        this.type = 0;
        this.id = Identities.uuid2();
        this.operateTime = new Date();
    }
    
    public double getBeforeAmount() {
        return DoubleUtils.round(DoubleUtils.sub(this.balance, this.amount), 2);
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getOperator() {
        return this.operator;
    }
    
    public void setOperator(final String operator) {
        this.operator = operator;
    }
    
    public Date getOperateTime() {
        return this.operateTime;
    }
    
    public void setOperateTime(final Date operateTime) {
        this.operateTime = operateTime;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(final double amount) {
        this.amount = amount;
    }
    
    public double getBalance() {
        return this.balance;
    }
    
    public void setBalance(final double balance) {
        this.balance = balance;
    }
    
    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(final String remark) {
        this.remark = remark;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public int getAccountingSubject() {
        return this.accountingSubject;
    }
    
    public void setAccountingSubject(final int accountingSubject) {
        this.accountingSubject = accountingSubject;
    }
}
