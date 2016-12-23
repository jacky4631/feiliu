// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Criteria;
import java.util.List;
import com.jiam365.flow.server.entity.FundAccount;
import org.mongodb.morphia.dao.BasicDAO;

public class FundAccountDao extends BasicDAO<FundAccount, Long>
{
    public List<FundAccount> findNotEqualsZeroAccounts() {
        final Query<FundAccount> query = (Query<FundAccount>)this.createQuery();
        query.or(new Criteria[] { (Criteria)query.criteria("balance").greaterThan((Object)0.01), (Criteria)query.criteria("balance").lessThan((Object)0) });
        query.order("balance");
        return (List<FundAccount>)query.asList();
    }
    
    public FundAccountDao(final Datastore ds) {
        super(ds);
    }
}
