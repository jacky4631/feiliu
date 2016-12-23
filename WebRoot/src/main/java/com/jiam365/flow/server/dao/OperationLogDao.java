// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.OperationLog;
import org.mongodb.morphia.dao.BasicDAO;

public class OperationLogDao extends BasicDAO<OperationLog, String>
{
    public OperationLogDao(final Datastore ds) {
        super(ds);
    }
}
