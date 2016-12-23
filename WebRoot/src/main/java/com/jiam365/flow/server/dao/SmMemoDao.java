// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.SmMemo;
import org.mongodb.morphia.dao.BasicDAO;

public class SmMemoDao extends BasicDAO<SmMemo, String>
{
    public SmMemoDao(final Datastore ds) {
        super(ds);
    }
}
