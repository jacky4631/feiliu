// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.product;

import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import java.util.Iterator;
import java.util.ArrayList;
import com.jiam365.modules.telco.Telco;
import java.util.List;
import com.jiam365.flow.server.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.FlowProductDao;

public class FlowProductManager
{
    @Autowired
    private FlowProductDao flowProductDao;
    @Autowired
    private MobileService mobileService;
    
    public FlowProduct get(final String id) {
        return (FlowProduct)this.flowProductDao.get(id);
    }
    
    public List<FlowProduct> findAll() {
        return this.flowProductDao.findAll();
    }
    
    public List<FlowProduct> findNationProducts(final Telco provider) {
        return this.flowProductDao.findNationProducts(provider);
    }
    
    public List<FlowProduct> findStateProducts(final Telco provider) {
        return this.flowProductDao.findStateProducts(provider);
    }
    
    public List<FlowProduct> findStateProducts(final Telco provider, final String scope) {
        return this.flowProductDao.findStateProducts(provider, scope);
    }
    
    public List<String> findProductScopes(final Telco provider) {
        final List<String> scopeCodes = this.flowProductDao.findProductScopes(provider);
        final List<String> scopes = new ArrayList<String>();
        for (final String code : scopeCodes) {
            if (!"NA".equals(code)) {
                scopes.add(this.mobileService.getStateNameByCode(code));
            }
        }
        return scopes;
    }
    
    public boolean cloneProductGroup(final Telco telco, final String state) {
        final List<FlowProduct> nationProducts = this.findNationProducts(telco);
        if (nationProducts.size() == 0) {
            return false;
        }
        final String stateName = this.mobileService.getStateNameByCode(state);
        for (final FlowProduct p : nationProducts) {
            p.setScope(state);
            final String id = ProductIDHelper.productId(p.getProvider(), p.getScope(), p.getSize());
            p.setId(id);
            final String name = p.getName();
            if (name.contains("\u5168\u56fd")) {
                p.setName(name.replace("\u5168\u56fd", stateName));
            }
            else if (name.contains("\u5168\u7f51")) {
                p.setName(name.replace("\u5168\u7f51", stateName));
            }
            else {
                p.setName(stateName + telco.getName() + p.getSize() + "M\u6d41\u91cf\u5305");
            }
            final String shortname = p.getShortName();
            if (shortname.contains("\u5168\u56fd")) {
                p.setShortName(shortname.replace("\u5168\u56fd", stateName));
            }
            else if (shortname.contains("\u5168\u7f51")) {
                p.setShortName(shortname.replace("\u5168\u7f51", stateName));
            }
            else {
                p.setShortName(stateName + telco.getName() + p.getSize() + "M");
            }
            this.save(p);
        }
        return true;
    }
    
    public void save(final FlowProduct flowProduct) {
        final String id = ProductIDHelper.productId(flowProduct.getProvider(), flowProduct.getScope(), flowProduct.getSize());
        if (StringUtils.isBlank((CharSequence)flowProduct.getId())) {
            flowProduct.setId(id);
        }
        else if (!id.equals(flowProduct.getId())) {
            throw new RuntimeException("\u786e\u5b9aID\u7684\u4ea7\u54c1,\u4e0d\u80fd\u518d\u88ab\u4fee\u6539\u89c4\u683c\u4fe1\u606f");
        }
        this.flowProductDao.save(flowProduct);
    }
    
    public void remove(final String id) {
        this.flowProductDao.deleteById(id);
    }
    
    public Page<FlowProduct> searchPage(final Page<FlowProduct> page) {
        final PageParseMongo<FlowProduct> builder = (PageParseMongo<FlowProduct>)new PageParseMongo((Page)page);
        final Query<FlowProduct> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.flowProductDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.flowProductDao.find((Query)q).asList());
        return page;
    }
    
    private Query<FlowProduct> buildQuery(final Map<String, String> filters) {
        final Query<FlowProduct> q = (Query<FlowProduct>)this.flowProductDao.createQuery();
        if (filters.containsKey("EQ_id")) {
            q.field("id").equal((Object)filters.get("EQ_id"));
        }
        if (filters.containsKey("EQ_scope")) {
            q.field("scope").equal((Object)filters.get("EQ_scope"));
        }
        if (filters.containsKey("EQ_provider")) {
            q.field("provider").equal((Object)Telco.valueOf((String)filters.get("EQ_provider")));
        }
        if (filters.containsKey("LIKE_name")) {
            q.field("name").contains((String)filters.get("LIKE_name"));
        }
        return q;
    }
}
