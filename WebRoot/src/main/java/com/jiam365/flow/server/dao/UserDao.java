// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.query.UpdateOperations;
import com.mongodb.WriteResult;
import org.mongodb.morphia.query.Query;
import java.util.List;
import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.entity.User;
import org.mongodb.morphia.dao.BasicDAO;

public class UserDao extends BasicDAO<User, Long>
{
    public UserDao(final Datastore ds) {
        super(ds);
    }
    
    public User getUserByUserName(final String username) {
        return (User)this.findOne("username", (Object)username);
    }
    
    public List<User> findNeedBillFileUsers() {
        final Query<User> q = (Query<User>)this.createQuery().filter("sendBillFile", (Object)true);
        return (List<User>)q.asList();
    }
    
    public boolean isExsit(final String username) {
        return this.exists("username", (Object)username);
    }
    
    public boolean deleteByUsername(final String username) {
        final Query<User> q = (Query<User>)this.createQuery().filter("username", (Object)username);
        final WriteResult result = super.deleteByQuery((Query)q);
        return result.getN() > 0;
    }
    
    public void updateStatus(final long id, final int status) {
        final Query<User> query = (Query<User>)this.createQuery().filter("id", (Object)id);
        final UpdateOperations<User> opts = (UpdateOperations<User>)this.createUpdateOperations();
        opts.set("status", (Object)status);
        this.update((Query)query, (UpdateOperations)opts);
    }
}
