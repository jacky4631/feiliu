// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.pretreatment;

import com.jiam365.flow.server.rest.RestException;
import com.jiam365.flow.server.params.FunctionLimitParam;
import com.jiam365.flow.server.engine.InvalidTradeTimeException;
import com.jiam365.modules.utils.SimpleDateUtils;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import com.jiam365.modules.telco.Telco;
import com.jiam365.flow.sdk.RechargeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.params.ParamsService;

public class RechargeMomentPretreatment implements Pretreatment
{
    @Autowired
    private ParamsService paramsService;
    
    @Override
    public void check(final RechargeRequest request) throws RestException {
        if (Telco.CMCC.equals((Object)request.getProvider()) && ProductIDHelper.isNationProduct(request.getProductId())) {
            final FunctionLimitParam param = this.paramsService.loadFunctionLimitParam();
            if (param.isEnableAutoLimit() && SimpleDateUtils.isRemainDays(param.getLastDays())) {
                throw new InvalidTradeTimeException();
            }
        }
    }
}
