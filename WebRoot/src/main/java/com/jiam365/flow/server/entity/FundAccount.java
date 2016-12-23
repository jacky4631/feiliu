// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.entity;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "sys_account", noClassnameStored = true)
public class FundAccount implements Serializable
{
    private static final long serialVersionUID = 5593288918281809791L;
    @Id
    private Long userId;
    private Double balance;
    private Double creditLine;
    
    public FundAccount() {
        this.balance = 0.0;
        this.creditLine = 0.0;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    
    public void setUserId(final Long userId) {
        this.userId = userId;
    }
    
    public Double getBalance() {
        return this.balance;
    }
    
    public void setBalance(final Double balance) {
        this.balance = balance;
    }
    
    public Double getCreditLine() {
        return this.creditLine;
    }
    
    public void setCreditLine(final Double creditLine) {
        this.creditLine = creditLine;
    }
}
