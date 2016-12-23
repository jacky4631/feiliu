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
import com.jiam365.flow.server.product.FlowProduct;
import java.util.regex.Pattern;
import org.mongodb.morphia.query.Query;
import com.jiam365.modules.telco.Telco;
import java.util.List;
import com.jiam365.flow.server.product.UserProduct;
import org.mongodb.morphia.dao.BasicDAO;

public class UserProductDao extends BasicDAO<UserProduct, String>
{
    public List<UserProduct> findAll() {
        return (List<UserProduct>)this.find().asList();
    }
    
    public List<UserProduct> findNationProducts(final String username, final Telco provider) {
        final Query<UserProduct> q = (Query<UserProduct>)this.createQuery();
        q.filter("provider", (Object)provider).filter("scope", (Object)"NA").filter("username", (Object)username).order("size");
        return (List<UserProduct>)q.asList();
    }
    
    public List<UserProduct> findProducts(final String username, final Telco provider, final String scope, final boolean... roamable) {
        final Query<UserProduct> q = (Query<UserProduct>)this.createQuery();
        if (roamable.length > 0 && !roamable[0]) {
            final Pattern regexp = Pattern.compile("\\w{2}\\d{6}\\$");
            q.filter("productId", (Object)regexp);
        }
        else {
            final Pattern regexp = Pattern.compile("\\w{2}\\d{6}$");
            q.filter("productId", (Object)regexp);
        }
        q.filter("username", (Object)username).filter("provider", (Object)provider).filter("scope", (Object)scope).order("size");
        return (List<UserProduct>)q.asList();
    }
    
    public List<UserProduct> findProducts(final String username, final Telco provider) {
        final Query<UserProduct> q = (Query<UserProduct>)this.createQuery();
        ((Query)q.filter("username", (Object)username).filter("provider", (Object)provider).field("scope").notEqual((Object)"NA")).order("scope, size");
        return (List<UserProduct>)q.asList();
    }
    
    public List<String> findProductScopes(final String username, final Telco provider) {
        final AggregationPipeline pipeline = this.getDs().createAggregation((Class)FlowProduct.class).match((Query)((Query)this.createQuery().field("provider").equal((Object)provider)).field("username").equal((Object)username)).group(Group.id(new Group[] { Group.grouping("provider"), Group.grouping("scope") }), new Group[0]);
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
    
    public List<UserProduct> findUserProduct(final String username) {
        final Query<UserProduct> q = (Query<UserProduct>)this.createQuery();
        q.filter("username", (Object)username);
        q.order("productId");
        return (List<UserProduct>)q.asList();
    }
    
    public boolean hasProduct(final String productId, final String username) {
        final Query<UserProduct> q = (Query<UserProduct>)this.createQuery();
        q.filter("productId", (Object)productId);
        q.filter("username", (Object)username);
        return this.exists((Query)q);
    }
    
    public List<UserProduct> findUserProducts(final String productId) {
        final Query<UserProduct> q = (Query<UserProduct>)this.createQuery();
        q.filter("productId", (Object)productId);
        return (List<UserProduct>)q.asList();
    }
    
    public UserProduct getUserProduct(final String productId, final String username) {
        final Query<UserProduct> q = (Query<UserProduct>)this.createQuery();
        q.filter("productId", (Object)productId);
        q.filter("username", (Object)username);
        return (UserProduct)this.findOne((Query)q);
    }
    
    public void removeProduct(final String productId, final String username) {
        final Query<UserProduct> q = (Query<UserProduct>)this.createQuery();
        q.filter("productId", (Object)productId);
        q.filter("username", (Object)username);
        this.deleteByQuery((Query)q);
    }
    
    public void removeProductsByUsername(final String username) {
        final Query<UserProduct> q = (Query<UserProduct>)this.createQuery();
        q.filter("username", (Object)username);
        this.deleteByQuery((Query)q);
    }
    
    public UserProductDao(final Datastore ds) {
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
