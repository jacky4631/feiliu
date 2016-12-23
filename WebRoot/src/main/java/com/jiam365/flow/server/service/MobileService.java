// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import com.jiam365.modules.telco.MobileIndentify;
import com.jiam365.flow.sdk.MobileInfo;
import java.util.List;
import com.jiam365.flow.server.dic.MobileLocation;
import com.jiam365.flow.server.dic.State;
import com.jiam365.flow.server.dao.MobileLocationDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.StateDao;

public class MobileService
{
    @Autowired
    private StateDao stateDao;
    @Autowired
    private MobileLocationDao mobileLocationDao;
    
    public State whichState(final String mobile) {
        final MobileLocation mobileLocation = this.mobileLocationDao.getByMobile(mobile);
        if (mobileLocation != null) {
            final String area = mobileLocation.getArea();
            final String stateName = this.getStateName(area);
            return this.stateDao.getByName(stateName);
        }
        return null;
    }
    
    public List<State> findAllStates() {
        return this.stateDao.findAllStates();
    }
    
    public String getStateNameByCode(final String code) {
        final State state = (State)this.stateDao.get(code);
        return (state == null) ? "" : state.getName();
    }
    
    private String getStateName(final String area) {
        String stateName = area;
        final int idx = area.indexOf(" ");
        if (idx > -1) {
            stateName = area.substring(0, idx);
        }
        return stateName;
    }
    
    public MobileLocation which(final String mobile) {
        return this.mobileLocationDao.getByMobile(mobile);
    }
    
    public MobileInfo mobileInfo(final String mobile) {
        final MobileInfo info = new MobileInfo();
        info.setProvider(MobileIndentify.getInstance().indentifyMobile(mobile));
        final MobileLocation mobileLocation = this.mobileLocationDao.getByMobile(mobile);
        if (mobileLocation != null) {
            final String area = mobileLocation.getArea();
            final String stateName = this.getStateName(area);
            info.setArea(area);
            info.setMobileType(mobileLocation.getMobileType());
            final State state = this.stateDao.getByName(stateName);
            if (state != null) {
                info.setStateCode(state.getCode());
                info.setStateName(state.getName());
            }
        }
        return info;
    }
    
    public String getStateCode(final String mobile) {
        final State state = this.whichState(mobile);
        return (state == null) ? "NA" : state.getCode();
    }
}
