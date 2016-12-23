// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import java.util.List;
import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.dic.State;
import org.mongodb.morphia.dao.BasicDAO;

public class StateDao extends BasicDAO<State, String>
{
    public StateDao(final Datastore ds) {
        super(ds);
    }
    
    public List<State> findAllStates() {
        return (List<State>)this.createQuery().order("-sort").asList();
    }
    
    public State getByName(final String name) {
        return (State)this.findOne("name", (Object)name);
    }
}
