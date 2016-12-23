// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.product;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import com.jiam365.modules.utils.Identities;
import com.jiam365.flow.sdk.OrigiProductId;
import java.util.Iterator;
import com.jiam365.flow.sdk.MobileInfo;
import java.util.Collection;
import java.util.ArrayList;
import com.jiam365.modules.telco.Telco;
import java.util.List;
import com.jiam365.flow.server.service.MobileService;
import com.jiam365.flow.server.channel.ChannelConnectionManager;
import com.jiam365.flow.server.dao.FlowPackageDao;
import com.jiam365.flow.server.dao.FlowProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.UserProductDao;

public class UserProductManager
{
    @Autowired
    private UserProductDao userProductDao;
    @Autowired
    private FlowProductDao flowProductDao;
    @Autowired
    private FlowPackageDao flowPackageDao;
    @Autowired
    private ChannelConnectionManager channelConnectionManager;
    @Autowired
    private MobileService mobileService;
    
    public List<UserProduct> findUsersProduct(final String username) {
        return this.userProductDao.findUserProduct(username);
    }
    
    public List<UserProduct> findNationProducts(final String username, final Telco provider) {
        return this.userProductDao.findNationProducts(username, provider);
    }
    
    public List<UserProduct> findStateProducts(final String username, final Telco provider, final String scope) {
        if ("NA".equals(scope)) {
            return this.userProductDao.findProducts(username, provider, scope, new boolean[0]);
        }
        final List<UserProduct> products = new ArrayList<UserProduct>();
        products.addAll(this.userProductDao.findProducts(username, provider, scope, true));
        products.addAll(this.userProductDao.findProducts(username, provider, scope, false));
        return products;
    }
    
    public List<UserProduct> findStateProducts(final String username, final Telco provider) {
        return this.userProductDao.findProducts(username, provider);
    }
    
    public List<UserProduct> findStateProducts(final String username, final String mobile) {
        final List<UserProduct> products = new ArrayList<UserProduct>();
        final MobileInfo info = this.mobileService.mobileInfo(mobile);
        products.addAll(this.findStateProducts(username, info.getProvider(), "NA"));
        products.addAll(this.findStateProducts(username, info.getProvider(), info.getStateCode()));
        return products;
    }
    
    public List<String> findProductScopes(final String username, final Telco provider) {
        final List<String> scopeCodes = this.userProductDao.findProductScopes(username, provider);
        final List<String> scopes = new ArrayList<String>();
        for (final String code : scopeCodes) {
            if (!"NA".equals(code)) {
                scopes.add(this.mobileService.getStateNameByCode(code));
            }
        }
        return scopes;
    }
    
    public List<FlowProduct> findUserOnlineFlowProduct(final String username, final MobileInfo info, final boolean isNationwide) {
        final List<UserProduct> userProducts = this.findUsersProduct(username);
        final List<FlowProduct> flowProducts = new ArrayList<FlowProduct>();
        for (final UserProduct userProduct : userProducts) {
            final OrigiProductId oid = new OrigiProductId(userProduct.getProductId());
            final FlowProduct flowProduct = (FlowProduct)this.flowProductDao.get(oid.origiProductId);
            if (flowProduct != null) {
                if (!flowProduct.isEnabled()) {
                    continue;
                }
                if (isNationwide) {
                    if (!flowProduct.getProvider().equals((Object)info.getProvider())) {
                        continue;
                    }
                    if (!"NA".equals(flowProduct.getScope())) {
                        continue;
                    }
                }
                else {
                    if (!flowProduct.getProvider().equals((Object)info.getProvider())) {
                        continue;
                    }
                    if (!flowProduct.getScope().equals(info.getStateCode())) {
                        continue;
                    }
                }
                final List<FlowPackage> flowPackages = this.flowPackageDao.findFlowPackagesByProductId(flowProduct.getId(), true);
                for (final FlowPackage flowPackage : flowPackages) {
                    if (this.channelConnectionManager.isOnline(flowPackage.getFlowChannelId())) {
                        if (!oid.roamable) {
                            flowProduct.setId(flowProduct.getId() + "$");
                        }
                        flowProducts.add(flowProduct);
                        break;
                    }
                }
            }
        }
        return flowProducts;
    }
    
    public void save(final UserProduct userProduct) {
        this.userProductDao.save(userProduct);
    }
    
    public boolean hasProduct(final String productId, final String username) {
        return this.userProductDao.hasProduct(productId, username);
    }
    
    public List<UserProduct> findUserProducts(final String productId) {
        return this.userProductDao.findUserProducts(productId);
    }
    
    public UserProduct getUserProduct(final String productId, final String username) {
        return this.userProductDao.getUserProduct(productId, username);
    }
    
    public void authProduct2User(final String productId, final String username, final boolean roamable) {
        final FlowProduct flowProduct = (FlowProduct)this.flowProductDao.get(productId);
        if (flowProduct == null) {
            throw new RuntimeException("\u6307\u5b9a\u7684\u57fa\u7840\u4ea7\u54c1\u5e76\u4e0d\u5b58\u5728, \u65e0\u6cd5\u6388\u6743");
        }
        final String realProductId = productId + (roamable ? "" : "$");
        if (!this.hasProduct(realProductId, username)) {
            final UserProduct userProduct = new UserProduct();
            userProduct.setId(Identities.uuid2());
            userProduct.setUsername(username);
            userProduct.setProductId(realProductId);
            userProduct.setName(flowProduct.getName());
            userProduct.setShortName(flowProduct.getShortName());
            userProduct.setPrice(flowProduct.getPrice());
            userProduct.setSize(flowProduct.getSize());
            userProduct.setProvider(flowProduct.getProvider());
            userProduct.setScope(flowProduct.getScope());
            this.save(userProduct);
        }
    }
    
    public UserProduct updateDiscount(final String id, final double discount) {
        final UserProduct userProduct = (UserProduct)this.userProductDao.get(id);
        userProduct.setDiscount(discount);
        this.save(userProduct);
        return userProduct;
    }
    
    public void updateProductPrice(final String productId, final double price) {
        final List<UserProduct> userProducts = this.userProductDao.findUserProducts(productId);
        userProducts.stream().forEach(userProduct -> {
            userProduct.setPrice(price);
            this.userProductDao.save(userProduct);
        });
    }
    
    public UserProduct applyDiscount2Group(final String id, final double discount) {
        final UserProduct userProduct = (UserProduct)this.userProductDao.get(id);
        final boolean roamable = !userProduct.getProductId().endsWith("$");
        final List<UserProduct> products = this.userProductDao.findProducts(userProduct.getUsername(), userProduct.getProvider(), userProduct.getScope(), roamable);
        for (final UserProduct up : products) {
            up.setDiscount(discount);
            this.save(up);
        }
        return userProduct;
    }
    
    public UserProduct removeGroup(final String id) {
        final UserProduct userProduct = (UserProduct)this.userProductDao.get(id);
        final boolean roamable = !userProduct.getProductId().endsWith("$");
        final List<UserProduct> products = this.userProductDao.findProducts(userProduct.getUsername(), userProduct.getProvider(), userProduct.getScope(), roamable);
        for (final UserProduct up : products) {
            this.userProductDao.delete(up);
        }
        return userProduct;
    }
    
    public UserProduct removeProduct(final String id) {
        final UserProduct userProduct = (UserProduct)this.userProductDao.get(id);
        if (userProduct != null) {
            this.userProductDao.delete(userProduct);
        }
        return userProduct;
    }
    
    public void removeProduct(final String productId, final String username) {
        this.userProductDao.removeProduct(productId, username);
    }
    
    public Page<UserProduct> searchPage(final Page<UserProduct> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<UserProduct> q = this.buildQuery(builder.searchParams());
        if (q != null) {
            page.setTotalCount(this.userProductDao.count((Query)q));
            q.order(builder.sortParams());
            q.offset((int)page.getFirst());
            q.limit((int)page.getPageSize());
            page.setResult(this.userProductDao.find((Query)q).asList());
        }
        return page;
    }
    
    private Query<UserProduct> buildQuery(final Map<String, String> filters) {
        final Query<UserProduct> q = (Query<UserProduct>)this.userProductDao.createQuery();
        if (filters.containsKey("EQ_username") && StringUtils.isNotBlank((CharSequence)filters.get("EQ_username"))) {
            q.field("username").equal((Object)filters.get("EQ_username"));
            if (filters.containsKey("EQ_state")) {
                q.field("scope").equal((Object)filters.get("EQ_state"));
            }
            if (filters.containsKey("EQ_provider")) {
                q.field("provider").equal((Object)Telco.valueOf((String)filters.get("EQ_provider")));
            }
            return q;
        }
        return null;
    }
}
