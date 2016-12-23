// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import com.jiam365.flow.server.product.FlowProduct;
import com.jiam365.modules.telco.Telco;
import java.util.Iterator;
import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;
import com.jiam365.flow.server.dto.ProductGroup;
import com.jiam365.modules.utils.Identities;
import com.jiam365.flow.server.product.FlowPackage;
import java.util.List;
import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.flow.server.channel.FlowChannel;
import com.jiam365.flow.sdk.utils.ProductIDHelper;
import org.apache.commons.lang3.StringUtils;
import java.util.concurrent.ExecutionException;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.jiam365.flow.server.entity.ChannelProductGroupProfile;
import com.google.common.cache.LoadingCache;
import com.jiam365.flow.server.dao.ChannelProductGroupProfileDao;
import com.jiam365.flow.oc.service.ChannelAdminManager;
import com.jiam365.flow.server.product.FlowProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.FlowPackageDao;

public class FlowPackageManager
{
    @Autowired
    private FlowPackageDao flowPackageDao;
    @Autowired
    private FlowProductManager flowProductManager;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private ChannelAdminManager channelAdminManager;
    @Autowired
    private ChannelProductGroupProfileDao channelProductGroupProfileDao;
    private LoadingCache<String, ChannelProductGroupProfile> profileCache;
    
    public FlowPackageManager() {
        this.profileCache = (LoadingCache<String, ChannelProductGroupProfile>)CacheBuilder.newBuilder().maximumSize(500L).expireAfterWrite(1L, TimeUnit.DAYS).build((CacheLoader)new CacheLoader<String, ChannelProductGroupProfile>() {
            public ChannelProductGroupProfile load(final String id) throws Exception {
                return FlowPackageManager.this.getGroupProfile(id);
            }
        });
    }
    
    public ChannelProductGroupProfile loadGroupProfile(final String id) {
        try {
            return (ChannelProductGroupProfile)this.profileCache.get(id);
        }
        catch (ExecutionException e) {
            return new ChannelProductGroupProfile();
        }
    }
    
    public ChannelProductGroupProfile getGroupProfile(final String id) {
        ChannelProductGroupProfile profile = (ChannelProductGroupProfile)this.channelProductGroupProfileDao.get(id);
        if (profile == null) {
            profile = new ChannelProductGroupProfile(id);
        }
        if (id.endsWith("$")) {
            profile.setRoamable(false);
        }
        return profile;
    }
    
    public void saveGroupProfile(final ChannelProductGroupProfile profile) {
        final String id = profile.getId();
        final long channelId = Long.parseLong(StringUtils.substringBefore(id, "-"));
        profile.setChannelId(channelId);
        profile.setName(this.fullChannelGroupName(id));
        this.channelProductGroupProfileDao.save(profile);
        this.profileCache.refresh(id);
    }
    
    public String fullChannelGroupName(final String fullGroupCode) {
        final long channelId = Long.parseLong(StringUtils.substringBefore(fullGroupCode, "-"));
        final FlowChannel channel = this.channelAdminManager.get(channelId);
        final String channelName = (channel == null) ? "[\u5220\u9664\u7684\u901a\u9053]" : channel.getName();
        final String groupCode = StringUtils.substringAfter(fullGroupCode, "-");
        if ("ALL".equals(groupCode)) {
            return channelName;
        }
        final String flag = groupCode.substring(2, 3);
        final String stateCode = groupCode.substring(0, 2);
        final String tecloName = ProductIDHelper.telcoFlag2Name(flag);
        final String stateName = this.mobileService.getStateNameByCode(stateCode);
        final String roamableFlag = fullGroupCode.endsWith("$") ? "$" : "";
        return tecloName + "-" + stateName + roamableFlag + "@" + channelName;
    }
    
    public void updateProductPrice(final String productId, final double price) {
        final List<FlowPackage> flowPackages = this.flowPackageDao.findFlowPackagesByProductId(productId, new boolean[0]);
        flowPackages.stream().forEach(flowPackage -> {
            flowPackage.setPrice(price);
            flowPackage.setBillAmount(DoubleUtils.round(DoubleUtils.mul(price, flowPackage.getDiscount()), 2));
            this.flowPackageDao.save(flowPackage);
        });
    }
    
    public void removeGroupProfile(final String profileId) {
        this.channelProductGroupProfileDao.deleteById(profileId);
        this.profileCache.invalidate(profileId);
    }
    
    public List<ChannelProductGroupProfile> findProtectedProfile() {
        return this.channelProductGroupProfileDao.findProtectedProfile();
    }
    
    public FlowPackage removeFlowPackage(final String id) {
        final FlowPackage flowPackage = (FlowPackage)this.flowPackageDao.get(id);
        if (flowPackage != null) {
            this.flowPackageDao.delete(flowPackage);
        }
        return flowPackage;
    }
    
    public FlowPackage getFlowPackage(final Long flowChannelId, final String productId) {
        return this.flowPackageDao.getFlowPackage(flowChannelId, productId);
    }
    
    public FlowPackage getFlowPackage(final String id) {
        return (FlowPackage)this.flowPackageDao.get(id);
    }
    
    public List<FlowPackage> findByChannelId(final Long channelId) {
        return this.flowPackageDao.findFlowPackages(channelId);
    }
    
    public List<FlowPackage> findByGroupCode(final Long flowChannelId, final String groupCode) {
        return this.flowPackageDao.findProductGroupItems(flowChannelId, groupCode);
    }
    
    public void saveOrUpdate(final FlowPackage flowPackage) {
        if (StringUtils.isBlank((CharSequence)flowPackage.getId())) {
            final String productId = flowPackage.getProductId();
            final long channelId = flowPackage.getFlowChannelId();
            final FlowPackage fp = this.flowPackageDao.getFlowPackage(Long.valueOf(channelId), productId);
            flowPackage.setId((fp != null) ? fp.getId() : Identities.uuid2());
        }
        flowPackage.setBillAmount(DoubleUtils.mul(flowPackage.getPrice(), flowPackage.getDiscount()));
        this.flowPackageDao.save(flowPackage);
    }
    
    public FlowPackage changeStatus(final String id) {
        final FlowPackage flowPackage = (FlowPackage)this.flowPackageDao.get(id);
        flowPackage.setEnabled(!flowPackage.isEnabled());
        this.flowPackageDao.save(flowPackage);
        return flowPackage;
    }
    
    public FlowPackage selectFlowPackage(final Long channelId, final String productId, final String stateCode, final boolean naFirst) {
        final String replaceProductId = ProductIDHelper.replacementProductId(productId, stateCode);
        String[] array2;
        if (naFirst) {
            final String[] array = array2 = new String[2];
            array[0] = productId;
            array[1] = replaceProductId;
        }
        else {
            final String[] array3 = array2 = new String[2];
            array3[0] = replaceProductId;
            array3[1] = productId;
        }
        final String[] ids = array2;
        for (int i = 0; i < 2; ++i) {
            if (i == 1 && ids[0].equals(ids[1])) {
                return null;
            }
            final FlowPackage flowPackage = this.getFlowPackage(channelId, ids[i]);
            if (flowPackage != null) {
                return flowPackage;
            }
        }
        return null;
    }
    
    public List<ProductGroup> findProductGroup(final Long flowChannelId) {
        final Map<String, Integer> groups = this.flowPackageDao.getProductGroupCount(flowChannelId);
        final List<ProductGroup> productGroups = new ArrayList<ProductGroup>();
        for (final Map.Entry<String, Integer> entry : groups.entrySet()) {
            final String groupCode = entry.getKey();
            final long enabledCount = this.flowPackageDao.countGroupItems(flowChannelId, groupCode, true);
            final ChannelProductGroupProfile profile = this.getGroupProfile(flowChannelId + "-" + groupCode);
            final ProductGroup group = ProductGroup.create(flowChannelId, groupCode, entry.getValue(), enabledCount, profile, this.mobileService);
            productGroups.add(group);
        }
        Collections.sort(productGroups, (a, b) -> a.getGroupCode().compareTo(b.getGroupCode()));
        return productGroups;
    }

    public void createGroup(long channelId, String state, Boolean roamable, String telco, int idMethod, int prior, double discount) {
        Telco provider = Telco.valueOf((String)telco);
        List<FlowProduct> flowProducts = this.flowProductManager.findStateProducts(Telco.valueOf((String)telco), state);
        if (flowProducts.size() == 0) {
            this.flowProductManager.cloneProductGroup(provider, state);
            flowProducts = this.flowProductManager.findStateProducts(Telco.valueOf((String)telco), state);
        }
        if (flowProducts.size() == 0) {
            throw new RuntimeException("\u627e\u4e0d\u5230\u6307\u5b9a\u7684\u7701\u5305\u57fa\u7840\u5e93\u4ea7\u54c1\u5b9a\u4e49, \u4e5f\u65e0\u6cd5\u4ece\u5168\u56fd\u5305\u590d\u5236, \u8bf7\u81f3\u5c11\u5148\u5b9a\u4e49\u624b\u5de5\u57fa\u7840\u4ea7\u54c1\u5e93\u7684\u5168\u56fd\u4ea7\u54c1.");
        }
        flowProducts.stream().filter(flowProduct -> flowProduct.getSize() != 12).forEach(flowProduct -> {
                    FlowPackage flowPackage = new FlowPackage();
                    flowPackage.setDiscount(discount);
                    flowPackage.setEnabled(false);
                    flowPackage.setFlowChannelId(Long.valueOf(channelId));
                    flowPackage.setPrice(flowProduct.getPrice());
                    flowPackage.setPriority(prior);
                    flowPackage.setProductId(roamable != false ? flowProduct.getId() : flowProduct.getId() + "$");
                    flowPackage.setSize(flowProduct.getSize());
                    flowPackage.setTitle(flowProduct.getShortName());
                    flowPackage.setOrigiProductId(this.generateProductId(flowPackage.getProductId(), idMethod, flowProduct.getSize()));
                    this.saveOrUpdate(flowPackage);
                }
        );
        String groupProfileId = ProductIDHelper.groupProfileId((long)channelId, (String)state, (Boolean)roamable, (String)telco);
        ChannelProductGroupProfile profile = this.getGroupProfile(groupProfileId);
        this.saveGroupProfile(profile);
    }

    private String generateProductId(final String productId, final int type, final int size) {
        switch (type) {
            case 0: {
                return String.valueOf(size);
            }
            case 1: {
                return String.valueOf((size >= 1024) ? (size / 1024 * 1000) : size);
            }
            case 3: {
                return productId;
            }
            default: {
                return "1";
            }
        }
    }
    
    public void removeByChannelId(final long channleId) {
        this.flowPackageDao.removeFlowPackagesByChannelId(channleId);
        this.channelProductGroupProfileDao.deleteByChannelId(channleId);
    }
    
    public void removeGroup(final Long flowChannelId, final String groupCode) {
        this.flowPackageDao.removeProductGroupItems(flowChannelId, groupCode);
        this.removeGroupProfile(flowChannelId + "-" + groupCode);
    }
    
    public List<FlowPackage> findAttachedFlowPackages(final String productId, final boolean... enabled) {
        return this.flowPackageDao.findFlowPackagesByProductId(productId, enabled);
    }
    
    public List<FlowPackage> findCompatiableFlowPackages(final String productId, final String stateCode, final boolean... enabled) {
        final String compatiabeProductId = ProductIDHelper.replacementProductId(productId, stateCode);
        return this.flowPackageDao.findAllCompatiableFlowPackages(productId, compatiabeProductId, enabled);
    }
}
