// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import org.slf4j.LoggerFactory;
import com.jiam365.flow.sdk.RechargeRequest;
import java.text.NumberFormat;
import com.jiam365.flow.server.entity.FundAccount;
import com.jiam365.flow.server.utils.DoubleUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import com.jiam365.flow.server.service.ChannelAccountManager;
import com.jiam365.flow.server.service.TradeLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.UserManager;
import org.slf4j.Logger;

public class BillingCenter
{
    private static Logger logger;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private ChannelAccountManager channelAccountManager;
    private final Map<String, Object> locks;
    
    public BillingCenter() {
        this.locks = new ConcurrentHashMap<String, Object>();
    }
    
    private Object getLock(final String username) {
        Object lock = this.locks.get(username);
        if (lock == null) {
            synchronized (this.locks) {
                if (!this.locks.containsValue(username)) {
                    lock = new Object();
                    this.locks.put(username, lock);
                }
            }
        }
        return lock;
    }
    
    private BillBean doUserPay(final String username, final double billDiscount, final double orgiPrice) throws InsufficientBalanceException {
        final BillBean billBean = new BillBean();
        final double price = DoubleUtils.round(DoubleUtils.mul(orgiPrice, billDiscount), 2);
        final double balance = this.doUserPay(username, price);
        billBean.amount = price;
        billBean.discount = billDiscount;
        billBean.balance = balance;
        return billBean;
    }
    
    private double doUserPay(final String username, final double amount) {
        final Long uid = this.userManager.getUid(username);
        synchronized (this.getLock(username)) {
            final FundAccount fundAccount = this.userManager.getCredit(uid);
            double balance = (fundAccount == null) ? 0.0 : fundAccount.getBalance();
            final double creditLine = (fundAccount == null) ? 0.0 : fundAccount.getCreditLine();
            if (DoubleUtils.add(balance, creditLine) < amount) {
                throw new InsufficientBalanceException(username + "\u9700\u8981\u6d88\u8d39" + amount + "\u5143, \u4f59\u989d\u4e0d\u8db3");
            }
            balance = DoubleUtils.sub(balance, amount);
            if (balance < 0.0) {
                BillingCenter.logger.info("{}\u7684\u8d39\u7528\u5df2\u7ecf\u82b1\u5b8c, \u4f7f\u7528\u4fe1\u7528\u989d{}/{}\u5143", new Object[] { username, Math.abs(balance), creditLine });
            }
            this.userManager.updateBalance(uid, balance);
            return balance;
        }
    }
    
    public void payChannelAccount(final Trade trade) {
        if (trade.assignedToConnection()) {
            synchronized (this.getLock(trade.getConnection().channelId().toString())) {
                BillingCenter.logger.info("{}/{}\u901a\u9053\u6210\u672c\u6263\u8d39\u6210\u529f, \u6263\u9664{}\u5143", new Object[] { trade.getTradeId(), trade.getConnection().channelId(), trade.getCost() });
                this.channelAccountManager.costing(trade);
            }
        }
    }
    
    public void pay(final Trade trade) throws InsufficientBalanceException {
        final RechargeRequest request = trade.getRequest();
        final BillBean info = this.doUserPay(request.getUsername(), request.getBillDiscount(), request.getPrice());
        this.payChannelAccount(trade);
        trade.setBillDiscount(info.discount);
        trade.setBillAmount(info.amount);
        BillingCenter.logger.info("{}/{}\u9884\u6263\u8d39\u6210\u529f, \u5b9e\u6263{}\u5143\uff08{}, \u4f59\u989d{}\u5143", new Object[] { trade.getTradeId(), request.getUsername(), info.amount, NumberFormat.getPercentInstance().format(info.discount), info.balance });
    }
    
    public double pay(final String username, final double amount) throws InsufficientBalanceException {
        final double balance = this.doUserPay(username, amount);
        BillingCenter.logger.info("{}\u8d26\u6237\u6263\u8d39{}\u5143\u6210\u529f", (Object)username, (Object)amount);
        return balance;
    }
    
    public void refundUser(final Trade trade) {
        final String username = trade.getRequest().getUsername();
        this.doUserRefund(username, trade.getBillAmount());
        BillingCenter.logger.info("{}/{}\u9000\u8d39\u6210\u529f, \u9000\u8fd8{}\u5143", new Object[] { trade.getTradeId(), username, trade.getBillAmount() });
        trade.setBillAmount(0.0);
        this.tradeLogManager.clearTradeLogBillInfo(trade.getTradeId());
    }
    
    private void doUserRefund(final String username, final double amount) {
        final Long uid = this.userManager.getUid(username);
        synchronized (this.getLock(username)) {
            double balance = this.userManager.getBalance(uid);
            balance = DoubleUtils.add(balance, amount);
            this.userManager.updateBalance(uid, balance);
        }
    }
    
    public void refundChannelAccount(final Trade trade) {
        if (trade.assignedToConnection()) {
            synchronized (this.getLock(trade.getConnection().channelId().toString())) {
                this.channelAccountManager.refund(trade);
                BillingCenter.logger.info("{}/{}\u901a\u9053\u6210\u672c\u9000\u56de\u6210\u529f, \u9000\u8fd8{}\u5143", new Object[] { trade.getTradeId(), trade.getConnection().channelId(), trade.getCost() });
            }
        }
    }
    
    public void refundChannelAccount(final String tradeId, final Long channelId, final double amount) {
        synchronized (this.getLock(String.valueOf(channelId))) {
            this.channelAccountManager.refund(channelId, amount);
            BillingCenter.logger.info("{}/{}\u901a\u9053\u6210\u672c\u9000\u56de\u6210\u529f, \u9000\u8fd8{}\u5143", new Object[] { tradeId, channelId, amount });
        }
    }
    
    public void balance(final boolean isSuccess, final Trade trade) {
        if (!isSuccess) {
            this.refundUser(trade);
        }
    }
    
    public double transfer2Channel(final long channelId, final double amount) {
        synchronized (this.getLock(String.valueOf(channelId))) {
            return DoubleUtils.round(this.channelAccountManager.trans2Channel(channelId, amount), 2);
        }
    }
    
    public double transfer(final String username, final double amount) {
        final Long uid = this.userManager.getUid(username);
        if (uid == null) {
            throw new RuntimeException("\u65e0\u6cd5\u8f6c\u8d26,\u7528\u6237" + username + "\u4e0d\u5b58\u5728");
        }
        synchronized (this.getLock(username)) {
            double balance = this.userManager.getBalance(uid);
            balance = DoubleUtils.round(DoubleUtils.add(balance, amount), 2);
            this.userManager.updateBalance(uid, balance);
            BillingCenter.logger.info("{}\u5165\u8d26\u6210\u529f, \u8f6c\u5165{}\u5143, \u4f59\u989d{}\u5143", new Object[] { username, amount, balance });
            return balance;
        }
    }
    
    static {
        BillingCenter.logger = LoggerFactory.getLogger((Class)BillingCenter.class);
    }
    
    class BillBean
    {
        double discount;
        double amount;
        double balance;
        
        BillBean() {
            this.discount = 1.0;
            this.amount = 0.0;
            this.balance = 0.0;
        }
    }
}
