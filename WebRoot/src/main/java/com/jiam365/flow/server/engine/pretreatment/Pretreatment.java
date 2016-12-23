// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.pretreatment;

import com.jiam365.flow.server.rest.RestException;
import com.jiam365.flow.sdk.RechargeRequest;

public interface Pretreatment
{
    void check(final RechargeRequest p0) throws RestException;
}
