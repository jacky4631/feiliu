// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import org.slf4j.LoggerFactory;
import java.net.SocketTimeoutException;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.ChannelRateLimitException;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.server.entity.PendingTrade;
import com.jiam365.flow.server.entity.TradeLog;
import com.jiam365.flow.server.service.MobileService;
import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.modules.utils.Identities;
import com.jiam365.flow.server.channel.ChannelConnection;
import com.jiam365.flow.sdk.RechargeRequest;
import java.util.Date;
import org.slf4j.Logger;
import java.util.concurrent.Callable;

public class Trade implements Callable<Void>
{
    private static final Logger logger;
    private static final int STATUS_ACCEPTED = -1;
    private static final int STATUS_NOT_RECHARGE = 0;
    private static final int STATUS_WAITING_RESULT = 4;
    private static final int STATUS_FINISH = 9;
    private String tradeId;
    private String requestNo;
    private Date startTime;
    private int status;
    private String lastMessage;
    private boolean isDummyResult;
    private double billDiscount;
    private double billAmount;
    private RechargeRequest request;
    private ChannelConnection connection;
    private TradeCenter tradeCenter;
    private int queryTimes;
    private boolean foreceInterceptorMe;
    
    public Trade(final TradeCenter central, final ChannelConnection connection, final RechargeRequest request) {
        this.status = 0;
        this.lastMessage = "";
        this.isDummyResult = false;
        this.billDiscount = 1.0;
        this.billAmount = 0.0;
        this.queryTimes = 0;
        this.foreceInterceptorMe = false;
        this.tradeId = Identities.uuid2();
        this.tradeCenter = central;
        this.connection = connection;
        this.request = request;
        this.setStartTimeAndTradeId(new Date());
    }
    
    private Trade() {
        this.status = 0;
        this.lastMessage = "";
        this.isDummyResult = false;
        this.billDiscount = 1.0;
        this.billAmount = 0.0;
        this.queryTimes = 0;
        this.foreceInterceptorMe = false;
    }
    
    public boolean assignedToConnection() {
        return this.connection != null;
    }
    
    public boolean isRecharging() {
        return 4 == this.status;
    }
    
    public void delayMe(final boolean delay) {
        this.status = (delay ? -1 : 0);
    }
    
    public double getCost() {
        double price = this.request.getExecuteProductPrice();
        if (price <= 0.01) {
            price = this.request.getPrice();
        }
        final double value = DoubleUtils.mul(this.request.getOrigiDiscount(), price);
        return DoubleUtils.round(value, 2);
    }
    
    public static Trade restoreTrade(final TradeCenter central, final MobileService mobileService, final TradeLog tradeLog) {
        if (tradeLog.getChannelResult() == 0 || tradeLog.isTradeFinished()) {
            return null;
        }
        final Trade trade = new Trade();
        trade.tradeId = tradeLog.getId();
        trade.tradeCenter = central;
        trade.requestNo = tradeLog.getRequestNo();
        trade.status = ((9 == tradeLog.getResult()) ? 0 : 9);
        trade.billDiscount = tradeLog.getBillDiscount();
        trade.billAmount = tradeLog.getBillAmount();
        trade.lastMessage = tradeLog.getChannelMessage();
        final RechargeRequest request = new RechargeRequest();
        request.setExecuteProductId(tradeLog.getExecuteProductId());
        request.setMobile(tradeLog.getMobile());
        request.setMobileInfo(mobileService.mobileInfo(tradeLog.getMobile()));
        request.setOrigiDiscount(tradeLog.getCostDiscount());
        request.setOrigiProductId(tradeLog.getOrigiProductId());
        request.setPrice(tradeLog.getPrice());
        request.setExecuteProductPrice(tradeLog.getExecuteProductPrice());
        request.setProductId(tradeLog.getProductId());
        request.setProductName(tradeLog.getProductName());
        request.setProvider(request.getMobileInfo().getProvider());
        request.setSize(tradeLog.getSize());
        request.setUsername(tradeLog.getUsername());
        request.setUserReqNo(tradeLog.getUserRequestNo());
        trade.setRequest(request);
        trade.setStartTimeAndTradeId(tradeLog.getStartDate());
        return trade;
    }
    
    public static Trade restoreTrade(final TradeCenter central, final ChannelConnection connection, final PendingTrade pendingTrade) {
        final Trade trade = new Trade();
        trade.connection = connection;
        trade.tradeCenter = central;
        trade.tradeId = pendingTrade.getTradeId();
        trade.request = pendingTrade.getRechargeRequest();
        trade.setStartTimeAndTradeId(pendingTrade.getStartTime());
        trade.status = pendingTrade.getStatus();
        trade.lastMessage = pendingTrade.getLastMessage();
        trade.requestNo = pendingTrade.getRequestNo();
        if (pendingTrade.getBillAmount() != null) {
            trade.billDiscount = pendingTrade.getBillDiscount();
            trade.billAmount = pendingTrade.getBillAmount();
        }
        return trade;
    }
    
    public boolean isFinished() {
        return this.status == 9;
    }
    
    public void onResult(final ResponseData data) {
        this.lastMessage = data.getMessage();
        this.isDummyResult = data.isDummy();
        if (data.isSuccess()) {
            this.doFinish(true);
        }
        else if (data.canRetry()) {
            final QueryFrequencyStrategy strategy = new SimpleQueryFrequencyStrategy();
            final int delay = strategy.delaySeconds(this.queryTimes);
            this.tradeCenter.continueTrade(this, delay);
        }
        else {
            this.doFinish(false);
        }
    }
    
    protected void onTimeout() {
        String message = "";
        boolean markSuccess = false;
        if (this.status == 0) {
            message = "\u5f00\u59cb\u8d60\u9001, \u5c1a\u672a\u53d1\u51fa";
        }
        else if (this.status == 4) {
            message = "\u7b49\u5f85\u8d60\u9001\u7ed3\u679c";
            markSuccess = TimeoutPolicy.result();
        }
        this.lastMessage = "\u5230\u671f" + (markSuccess ? "\u6210\u529f" : "\u5931\u8d25") + ", \u6700\u540e\u72b6\u6001: " + message;
        this.isDummyResult = true;
        this.doFinish(markSuccess);
    }
    
    @Override
    public Void call() throws Exception {
        try {
            if (this.status == 0) {
                this.doRecharge();
            }
            else if (this.status == 4) {
                this.doQueryRechargeResult();
            }
        }
        catch (Exception e) {
            this.lastMessage = e.getMessage();
            Trade.logger.error("\u6267\u884c\u4ea4\u6613\u65f6\u51fa\u73b0\u672a\u77e5\u7684\u9519\u8bef, {}", (Object)e.getMessage(), (Object)e);
            this.doFinish(false);
        }
        return null;
    }
    
    private void doFinish(final boolean isSuccess) {
        Trade.logger.debug("{}/{} \u4ea4\u6613{}\u7ed3\u675f, \u8be6\u60c5\u89c1\u4ea4\u6613\u65e5\u5fd7", new Object[] { this.getTradeId(), this.getRequest().getMobile(), isSuccess ? "\u6210\u529f" : "\u5931\u8d25" });
        this.status = 9;
        this.tradeCenter.onTradeFinish(isSuccess, this);
    }
    
    private void doRecharge() {
        ResponseData data;
        try {
            data = this.connection.recharge(this.request);
        }
        catch (ChannelConnectionException e) {
            if (e.isTemporary()) {
                if (e instanceof ChannelRateLimitException) {
                    this.tradeCenter.continueTrade(this, ((ChannelRateLimitException)e).getSuggestDelaySeconds());
                }
                else {
                    this.lastMessage = this.analyseSubmitErrorReason(e);
                    this.foreceInterceptorMe = true;
                    Trade.logger.warn("\u5728\u901a\u9053{}\u63d0\u4ea4{}\u6d41\u91cf\u8d60\u9001\u8bf7\u6c42\u65f6\u51fa\u73b0\u4e34\u65f6\u9519\u8bef, {}, \u9001\u4eba\u5de5\u5904\u7406", new Object[] { this.connection.channelId(), this.request.getMobile(), e.getMessage() });
                    this.doFinish(false);
                }
            }
            else {
                Trade.logger.error("\u5728\u901a\u9053{}\u63d0\u4ea4{}\u6d41\u91cf\u8d60\u9001\u8bf7\u6c42\u65f6\u51fa\u73b0\u6c38\u4e45\u9519\u8bef, \u6267\u884c\u5931\u8d25, {}", new Object[] { this.connection.channelId(), this.request.getMobile(), e.getMessage() });
                this.lastMessage = e.getMessage();
                this.doFinish(false);
            }
            return;
        }
        Trade.logger.debug("{}/{} \u8d60\u9001\u8bf7\u6c42\u5df2\u6210\u529f\u63d0\u4ea4", (Object)this.getTradeId(), (Object)this.request.getMobile());
        this.lastMessage = data.getMessage();
        if (data.isSuccess()) {
            this.requestNo = data.getRequestNo();
            if (StringUtils.isBlank((CharSequence)this.requestNo)) {
                this.lastMessage += "\u3010\u8ba2\u5355\u63d0\u4ea4\u51fa\u9519\u3011\u8fd4\u56de\u6570\u636e\u4e2d\u4e0d\u5305\u542brequestNo)";
                this.foreceInterceptorMe = true;
                this.doFinish(false);
                return;
            }
            if (this.connection.getChannel().isRealTime()) {
                this.doFinish(true);
                return;
            }
            this.status = 4;
            Trade.logger.debug("{}/{} {}\u79d2\u540e\u67e5\u8be2\u8d60\u9001\u7ed3\u679c", new Object[] { this.getTradeId(), this.request.getMobile(), 10 });
            this.request.setSubmitTime(System.currentTimeMillis());
            this.tradeCenter.onRechareRequestFinish(this, 10);
        }
        else if (data.canRetry()) {
            this.tradeCenter.continueTrade(this, 10);
        }
        else {
            this.doFinish(false);
        }
    }
    
    private String analyseSubmitErrorReason(final ChannelConnectionException e) {
        final Throwable cause = e.getCause();
        if (cause != null && cause instanceof SocketTimeoutException) {
            return "\u3010\u8ba2\u5355\u63d0\u4ea4\u8d85\u65f6\u3011";
        }
        return "\u3010\u8ba2\u5355\u63d0\u4ea4\u5931\u8d25\u3011" + e.getMessage();
    }
    
    private void doQueryRechargeResult() {
        try {
            ++this.queryTimes;
            final ResponseData data = this.connection.queryReport(this.request, this.requestNo);
            if (!StringUtils.isNotBlank((CharSequence)data.getResult())) {
                throw new ChannelConnectionException("Query result field is blank", new boolean[0]);
            }
            this.onResult(data);
        }
        catch (ChannelConnectionException e) {
            if (e.isTemporary()) {
                Trade.logger.error("{}@{}==>{}\u67e5\u8be2\u6d41\u91cf\u8d60\u9001\u62a5\u544a\u65f6\u51fa\u73b0\u9519\u8bef, \u7a0d\u540e\u91cd\u8bd5, {}", new Object[] { this.getTradeId(), this.connection.getChannel().getName(), this.request.getMobile(), e.getMessage() });
                this.tradeCenter.continueTrade(this, 30);
            }
            else {
                this.lastMessage = "\u3010\u8ba2\u5355\u67e5\u8be2\u5931\u8d25\u3011" + e.getMessage();
                Trade.logger.error("{}@{}==>{}\u67e5\u8be2\u6d41\u91cf\u8d60\u9001\u62a5\u544a\u65f6\u51fa\u73b0\u6c38\u4e45\u9519\u8bef, \u6267\u884c\u5931\u8d25, {}", new Object[] { this.getTradeId(), this.connection.getChannel().getName(), this.request.getMobile(), e.getMessage() });
                this.foreceInterceptorMe = true;
                this.doFinish(false);
            }
        }
    }
    
    public boolean isDummyResult() {
        return this.isDummyResult;
    }
    
    public boolean checkTimeout() {
        return System.currentTimeMillis() - this.startTime.getTime() > TimeoutPolicy.timeoutSeconds() * 1000;
    }
    
    public String getTradeId() {
        return this.tradeId;
    }
    
    public String getRequestNo() {
        return this.requestNo;
    }
    
    public RechargeRequest getRequest() {
        return this.request;
    }
    
    public void setRequest(final RechargeRequest request) {
        this.request = request;
    }
    
    public ChannelConnection getConnection() {
        return this.connection;
    }
    
    public void setConnection(final ChannelConnection connection) {
        this.connection = connection;
        this.status = 0;
    }
    
    public Date getStartTime() {
        return this.startTime;
    }
    
    protected void setStartTimeAndTradeId(final Date date) {
        this.startTime = date;
        if (this.request != null) {
            this.request.setStartTime(date);
            this.request.setTradeId(this.tradeId);
        }
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public String getLastMessage() {
        return this.lastMessage;
    }
    
    public double getBillDiscount() {
        return this.billDiscount;
    }
    
    public void setBillDiscount(final double billDiscount) {
        this.billDiscount = billDiscount;
    }
    
    public double getBillAmount() {
        return this.billAmount;
    }
    
    public void setBillAmount(final double billAmount) {
        this.billAmount = billAmount;
    }
    
    public boolean isForeceInterceptorMe() {
        return this.foreceInterceptorMe;
    }
    
    public void clearInterceptor() {
        this.foreceInterceptorMe = false;
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)Trade.class);
    }
}
