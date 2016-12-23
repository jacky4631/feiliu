// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.RefundKeyword;
import org.mongodb.morphia.dao.BasicDAO;

public class RefundKeywordDao extends BasicDAO<RefundKeyword, String>
{
    public RefundKeywordDao(final Datastore ds) {
        super(ds);
    }
}
