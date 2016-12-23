// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dao;

import org.mongodb.morphia.annotations.Id;
import java.util.regex.Pattern;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.query.MorphiaIterator;
import org.mongodb.morphia.aggregation.Sort;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.aggregation.Group;
import org.mongodb.morphia.aggregation.Projection;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;
import java.util.List;
import java.util.Set;
import org.mongodb.morphia.Datastore;
import com.jiam365.flow.server.product.FlowPackage;
import org.mongodb.morphia.dao.BasicDAO;

public class FlowPackageDao extends BasicDAO<FlowPackage, String>
{
    public FlowPackageDao(final Datastore ds) {
        super(ds);
    }
    
    public List<FlowPackage> findFlowPackagesIn(final Set<Long> flowChannleIds) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        return (List<FlowPackage>)((Query)((Query)query.field("enabled").equal((Object)true)).field("flowChannelId").in((Iterable)flowChannleIds)).order("flowChannelId, productId").asList();
    }
    
    public List<FlowPackage> findFlowPackages(final Long flowChannelId) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        query.filter("flowChannelId", (Object)flowChannelId).order("productId");
        return (List<FlowPackage>)query.asList();
    }
    
    public List<FlowPackage> findFlowPackagesByProductId(final String productId, final boolean... enabled) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        query.filter("productId", (Object)productId);
        if (enabled.length > 0) {
            query.filter("enabled", (Object)enabled[0]);
        }
        query.order("billAmount, priority");
        return (List<FlowPackage>)query.asList();
    }
    
    public List<FlowPackage> findAllCompatiableFlowPackages(final String productId, final String compatiabeProductId, final boolean... enabled) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        query.or(new Criteria[] { (Criteria)query.criteria("productId").equal((Object)productId), (Criteria)query.criteria("productId").equal((Object)compatiabeProductId) });
        if (enabled.length > 0) {
            query.filter("enabled", (Object)enabled[0]);
        }
        query.order("billAmount, priority");
        return (List<FlowPackage>)query.asList();
    }
    
    public long countEnabledFlowPackagesByProductId(final String productId) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        query.filter("productId", (Object)productId);
        query.filter("enabled", (Object)true);
        return super.count((Query)query);
    }
    
    public FlowPackage getFlowPackage(final Long flowChannelId, final int packageSize) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        query.filter("flowChannelId", (Object)flowChannelId).filter("size", (Object)packageSize);
        return (FlowPackage)this.findOne((Query)query);
    }
    
    public FlowPackage getFlowPackage(final Long flowChannelId, final String productId) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        query.filter("flowChannelId", (Object)flowChannelId).filter("productId", (Object)productId);
        return (FlowPackage)this.findOne((Query)query);
    }
    
    public void removeFlowPackagesByChannelId(final long channleId) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        query.filter("flowChannelId", (Object)channleId);
        super.deleteByQuery((Query)query);
    }
    
    public void updateFlowPackages(final Long flowChannelId, final List<FlowPackage> flowPackages) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        query.filter("flowChannelId", (Object)flowChannelId);
        this.deleteByQuery((Query)query);
        for (final FlowPackage flowPackage : flowPackages) {
            flowPackage.setFlowChannelId(flowChannelId);
            if (StringUtils.isBlank((CharSequence)flowPackage.getId())) {
                throw new DbException("Before insert a flowPackage object, you need an ID");
            }
            this.save(flowPackage);
        }
    }
    
    public Map<String, Integer> getProductGroupCount(final long channleId) {
        final Map<String, Integer> map = new HashMap<String, Integer>();
        Iterator<ProductGroupCount> iterator = null;
        try {
            final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
            query.filter("flowChannelId", (Object)channleId);
            final AggregationPipeline pipeline = this.getDs().createAggregation((Class)FlowPackage.class).match((Query)query).project(new Projection[] { Projection.projection("_id").suppress(), Projection.projection("productGroup", Projection.expression("$concat", new Object[] { Projection.expression("$substr", new Object[] { "$productId", 0, 3 }), Projection.expression("$substr", new Object[] { "$productId", 8, 1 }) }), new Projection[0]) }).group("productGroup", new Group[] { Group.grouping("count", new Accumulator("$sum", (Object)1)) }).sort(new Sort[] { Sort.ascending("productGroup") });
            iterator = (Iterator<ProductGroupCount>)pipeline.aggregate((Class)ProductGroupCount.class);
            while (iterator.hasNext()) {
                try {
                    final ProductGroupCount v = iterator.next();
                    map.put(v.productGroup, v.count);
                }
                catch (NullPointerException ex) {}
            }
        }
        finally {
            if (iterator != null) {
                ((MorphiaIterator)iterator).close();
            }
        }
        return map;
    }
    
    public List<FlowPackage> findProductGroupItems(final long channelId, final String productGroup) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        query.filter("flowChannelId", (Object)channelId);
        final String reg = this.productGroupQueryRegexp(productGroup);
        final Pattern regexp = Pattern.compile(reg);
        query.filter("productId", (Object)regexp);
        query.order("size");
        return (List<FlowPackage>)query.asList();
    }
    
    public long countGroupItems(final long channelId, final String productGroup, final boolean enabled) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        query.filter("flowChannelId", (Object)channelId);
        final String reg = this.productGroupQueryRegexp(productGroup);
        final Pattern regexp = Pattern.compile(reg);
        query.filter("productId", (Object)regexp);
        query.filter("enabled", (Object)enabled);
        return this.count((Query)query);
    }
    
    private String productGroupQueryRegexp(final String productGroup) {
        String reg;
        if (productGroup.endsWith("$")) {
            reg = productGroup.substring(0, 3) + "\\d{5}\\$";
        }
        else {
            reg = productGroup.substring(0, 3) + "\\d{5}$";
        }
        return reg;
    }
    
    public void removeProductGroupItems(final long channelId, final String productGroup) {
        final Query<FlowPackage> query = (Query<FlowPackage>)this.createQuery();
        query.filter("flowChannelId", (Object)channelId);
        final String reg = this.productGroupQueryRegexp(productGroup);
        final Pattern regexp = Pattern.compile(reg);
        query.filter("productId", (Object)regexp);
        super.deleteByQuery((Query)query);
    }
    
    static class ProductGroupCount
    {
        @Id
        String productGroup;
        int count;
    }
}
