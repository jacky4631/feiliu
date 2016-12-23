// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.Datastore;
import java.util.Iterator;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import java.util.ArrayList;
import org.mongodb.morphia.aggregation.Group;
import com.jiam365.modules.telco.Telco;
import org.mongodb.morphia.query.Query;
import java.util.List;
import com.jiam365.flow.server.product.FlowProduct;
import org.mongodb.morphia.dao.BasicDAO;

public class FlowProductDao extends BasicDAO<FlowProduct, String>
{
    public List<FlowProduct> findAll() {
        final Query<FlowProduct> q = (Query<FlowProduct>)this.createQuery();
        q.order("provider, id, size");
        return (List<FlowProduct>)q.asList();
    }
    
    public List<FlowProduct> findNationProducts(final Telco provider) {
        final Query<FlowProduct> q = (Query<FlowProduct>)this.createQuery();
        q.filter("provider", (Object)provider).filter("scope", (Object)"NA").order("size");
        return (List<FlowProduct>)q.asList();
    }
    
    public List<FlowProduct> findStateProducts(final Telco provider) {
        final Query<FlowProduct> q = (Query<FlowProduct>)this.createQuery();
        ((Query)q.filter("provider", (Object)provider).field("scope").notEqual((Object)"NA")).order("scope,size");
        return (List<FlowProduct>)q.asList();
    }
    
    public List<FlowProduct> findStateProducts(final Telco provider, final String scope) {
        final Query<FlowProduct> q = (Query<FlowProduct>)this.createQuery();
        q.filter("provider", (Object)provider).filter("scope", (Object)scope).order("size");
        return (List<FlowProduct>)q.asList();
    }
    
    public List<String> findProductScopes(final Telco provider) {
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)FlowProduct.class).match((Query)this.createQuery().field("provider").equal((Object)provider)).group(Group.id(new Group[] { Group.grouping("provider"), Group.grouping("scope") }), new Group[0]);
        final List<String> scopes = new ArrayList<String>();
        final Iterator<ProductScope> iterator = (Iterator<ProductScope>)pipeline.aggregate((Class)ProductScope.class);
        while (iterator.hasNext()) {
            try {
                final ProductScope p = iterator.next();
                scopes.add(p.id.scope);
            }
            catch (NullPointerException ignore) {
                ignore.printStackTrace();
            }
        }
        return scopes;
    }
    
    public FlowProductDao(final Datastore ds) {
        super(ds);
    }
    
    static class ProductScope
    {
        @Id
        ProductScopeId id;
    }
    
    static class ProductScopeId
    {
        String provider;
        String scope;
    }
}
