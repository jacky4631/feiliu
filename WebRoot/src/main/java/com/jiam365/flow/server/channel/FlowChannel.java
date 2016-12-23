// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.channel;

import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.modules.json.JsonFormatTools;
import org.mongodb.morphia.annotations.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "flow_channel", noClassnameStored = true)
public class FlowChannel implements Serializable
{
    private static final long serialVersionUID = 6178443678511379493L;
    public static final int STATUS_ENABLED = 0;
    public static final int STATUS_DISABLED = -1;
    @Id
    private Long id;
    @Indexed
    private String name;
    private String orgcode;
    private boolean realTime;
    @Embedded
    private ConnectionParam channelConnectionParam;
    private String remark;
    private String remoteUrl;
    private String remoteUsername;
    private String remotePassword;
    private String remoteDescription;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    private String creator;
    private int status;
    private boolean canReplaceNA;
    @Transient
    private double balance;
    
    public FlowChannel() {
        this.realTime = false;
        this.status = -1;
        this.canReplaceNA = true;
        this.balance = 0.0;
        this.createDate = new Date();
        this.channelConnectionParam = new ConnectionParam();
    }
    
    public boolean configCompleted() {
        return this.channelConnectionParam != null && this.channelConnectionParam.getHandlerClass() != null;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(final int status) {
        this.status = status;
    }
    
    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(final String remark) {
        this.remark = remark;
    }
    
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }
    
    public String getCreator() {
        return this.creator;
    }
    
    public void setCreator(final String creator) {
        this.creator = creator;
    }
    
    public String getOrgcode() {
        return this.orgcode;
    }
    
    public void setOrgcode(final String orgcode) {
        this.orgcode = orgcode;
    }
    
    public ConnectionParam getChannelConnectionParam() {
        return this.channelConnectionParam;
    }
    
    public String getChannelConnectionParamJson() {
        return JsonFormatTools.formatJson(this.channelConnectionParam.getParamJson());
    }
    
    public void setChannelConnectionParam(final ConnectionParam channelConnectionParam) {
        this.channelConnectionParam = channelConnectionParam;
    }
    
    public boolean isRealTime() {
        return this.realTime;
    }
    
    public void setRealTime(final boolean realTime) {
        this.realTime = realTime;
    }
    
    public boolean isCanReplaceNA() {
        return this.canReplaceNA;
    }
    
    public void setCanReplaceNA(final boolean canReplaceNA) {
        this.canReplaceNA = canReplaceNA;
    }
    
    public double getBalance() {
        return DoubleUtils.round(this.balance, 2);
    }
    
    public void setBalance(final double balance) {
        this.balance = balance;
    }
    
    public String getRemoteUrl() {
        return this.remoteUrl;
    }
    
    public void setRemoteUrl(final String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }
    
    public String getRemoteUsername() {
        return this.remoteUsername;
    }
    
    public void setRemoteUsername(final String remoteUsername) {
        this.remoteUsername = remoteUsername;
    }
    
    public String getRemotePassword() {
        return this.remotePassword;
    }
    
    public void setRemotePassword(final String remotePassword) {
        this.remotePassword = remotePassword;
    }
    
    public String getRemoteDescription() {
        return this.remoteDescription;
    }
    
    public void setRemoteDescription(final String remoteDescription) {
        this.remoteDescription = remoteDescription;
    }
}
