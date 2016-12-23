// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.Role;
import org.mongodb.morphia.dao.BasicDAO;

public class RoleDao extends BasicDAO<Role, String>
{
    public RoleDao(final Datastore ds) {
        super(ds);
    }
}
