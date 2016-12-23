// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.service;

import org.mongodb.morphia.query.Query;
import java.util.Map;
import com.jiam365.modules.persistent.PageParseMongo;
import com.jiam365.modules.persistent.Page;
import java.util.Iterator;
import com.jiam365.modules.mapper.BeanMapper;
import java.util.ArrayList;
import com.jiam365.flow.oc.dto.UserInfo;
import com.jiam365.modules.utils.Digests;
import com.jiam365.modules.utils.Encodes;
import com.jiam365.flow.server.entity.SmTemplate;
import com.jiam365.flow.server.entity.FundAccount;
import java.math.BigDecimal;
import com.jiam365.modules.utils.Identities;
import java.util.Date;
import com.google.common.collect.Lists;
import com.jiam365.modules.utils.Sequences;
import com.jiam365.flow.server.entity.ChannelGroup;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.jiam365.flow.server.entity.User;
import com.google.common.cache.LoadingCache;
import com.jiam365.flow.server.dao.UserProductDao;
import com.jiam365.flow.server.params.SmConfigManager;
import com.jiam365.flow.server.dao.FundAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.UserDao;

public class UserManager
{
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FundAccountDao fundAccountDao;
    @Autowired
    private SmConfigManager smConfigManager;
    @Autowired
    private TradeLogManager tradeLogManager;
    @Autowired
    private UserProductDao userProductDao;
    @Autowired
    private ChannelGroupManager channelGroupManager;
    private LoadingCache<String, User> userCache;
    
    public UserManager() {
        this.userCache = (LoadingCache<String, User>)CacheBuilder.newBuilder().maximumSize(100L).expireAfterWrite(6L, TimeUnit.HOURS).build((CacheLoader)new CacheLoader<String, User>() {
            public User load(final String username) throws Exception {
                final User u = UserManager.this.getUserByUsername(username);
                return (u == null) ? new User() : u;
            }
        });
    }
    
    public User getUser(final long userId) {
        return (User)this.userDao.get(userId);
    }
    
    public String getAllowIps(final String username) {
        try {
            final User user = (User)this.userCache.get(username);
            if (user.getAllowIps() != null) {
                return user.getAllowIps();
            }
        }
        catch (ExecutionException ex) {}
        return "";
    }
    
    public boolean sendSmActived(final String username) {
        try {
            final User user = (User)this.userCache.get(username);
            return user.isSendSm();
        }
        catch (ExecutionException ex) {
            return false;
        }
    }
    
    public String getCallbackUrl(final String username) {
        try {
            final User user = (User)this.userCache.get(username);
            if (user.getCallbackUrl() != null) {
                return user.getCallbackUrl();
            }
        }
        catch (ExecutionException ex) {}
        return "";
    }
    
    public double getSmPrice(final String username) {
        try {
            final User user = (User)this.userCache.get(username);
            return user.getSmPrice();
        }
        catch (ExecutionException ex) {
            return 0.0;
        }
    }
    
    public List<String> getUserChannels(final String username) {
        final User user = this.loadUserByUsername(username);
        if (user == null) {
            return Collections.emptyList();
        }
        final List<String> ret = user.getAllowChannelProductList();
        if (StringUtils.isNotBlank((CharSequence)user.getAllowChannelGroup())) {
            final ChannelGroup group = this.channelGroupManager.load(user.getAllowChannelGroup());
            ret.addAll(group.getAllowChannelProductList());
        }
        return ret;
    }
    
    public List<String> getGrantedProtectedProducts(final String username) {
        final User user = this.loadUserByUsername(username);
        if (user == null) {
            return Collections.emptyList();
        }
        return user.getGrantedSpecialChannelProducts();
    }
    
    public User loadUserByUsername(final String username) {
        try {
            final User u = (User)this.userCache.get(username);
            return u.isNew() ? null : u;
        }
        catch (ExecutionException e) {
            return null;
        }
    }
    
    public User getUserByUsername(final String username) {
        return this.userDao.getUserByUserName(username);
    }
    
    public void registerUser(final User user) {
        final User exsitUser = this.getUserByUsername(user.getUsername());
        if (exsitUser != null) {
            throw new RuntimeException("\u7528\u6237 " + user.getUsername() + "\u5df2\u5b58\u5728!");
        }
        if (user.getId() == null) {
            user.setId(Sequences.get());
        }
        this.entryptPassword(user);
        if (user.getRoles().isEmpty()) {
            user.setRoles(Lists.newArrayList(new String[] { "user" }));
        }
        user.setRegisterDate(new Date());
        user.setAuthToken(Identities.uuid2());
        this.userDao.save(user);
    }
    
    public void updateUser(final User user) {
        if (user.getId() == null) {
            throw new RuntimeException("User is not exsit.");
        }
        if (StringUtils.isNotBlank((CharSequence)user.getPlainPassword())) {
            this.entryptPassword(user);
        }
        if (user.getId() == 1L && !user.getRoles().contains("admin")) {
            user.addRole("admin");
        }
        this.userDao.save(user);
        this.userCache.refresh(user.getUsername());
    }
    
    public void enableUser(final long id, final boolean enable) {
        this.userDao.updateStatus(id, enable ? 0 : -1);
    }
    
    public void deleteUser(final long id) {
        if (this.isSupervisor(id)) {
            throw new RuntimeException("Can't remove super user.");
        }
        final User user = this.getUser(id);
        if (user == null) {
            throw new RuntimeException("User id " + id + " not exsit, can't be removed.");
        }
        if (this.tradeLogManager.getUserTradeLogCount(user.getUsername()) > 0L) {
            throw new RuntimeException("User " + user.getUsername() + " have some trade logs.");
        }
        final double balance = this.getBalance(id);
        final BigDecimal data1 = new BigDecimal(0.0);
        final BigDecimal data2 = new BigDecimal(balance);
        final int result = data1.compareTo(data2);
        if (result != 0) {
            throw new RuntimeException("User " + user.getUsername() + "'s balance is not zero, can't remove");
        }
        this.fundAccountDao.deleteById(id);
        this.userProductDao.removeProductsByUsername(user.getUsername());
        this.userDao.deleteById(id);
        this.userCache.invalidate(id);
    }
    
    public void updateBalance(final long userId, final double balance) {
        FundAccount fundAccount = (FundAccount)this.fundAccountDao.get(userId);
        if (fundAccount == null) {
            fundAccount = new FundAccount();
            fundAccount.setUserId(userId);
        }
        fundAccount.setBalance(balance);
        this.fundAccountDao.save(fundAccount);
    }
    
    public void updatecreditLine(final long userId, final double creditLine) {
        FundAccount fundAccount = (FundAccount)this.fundAccountDao.get(userId);
        if (fundAccount == null) {
            fundAccount = new FundAccount();
            fundAccount.setUserId(userId);
        }
        fundAccount.setCreditLine(creditLine);
        this.fundAccountDao.save(fundAccount);
    }
    
    public Long getUid(final String username) {
        final User user = this.getUserByUsername(username);
        return (user == null) ? null : user.getId();
    }
    
    public double getBalance(final long userId) {
        final FundAccount fundAccount = (FundAccount)this.fundAccountDao.get(userId);
        return (fundAccount == null) ? 0.0 : fundAccount.getBalance();
    }
    
    public double getBalance(final String username) {
        final Long uid = this.getUid(username);
        if (uid != null) {
            return this.getBalance(uid);
        }
        throw new RuntimeException("Can't find the user " + username);
    }
    
    public FundAccount getCredit(final long userId) {
        return (FundAccount)this.fundAccountDao.get(userId);
    }
    
    public void saveSmsTemplate(final SmTemplate SmTemplate) {
        if (SmTemplate.getUserId() == null) {
            throw new RuntimeException("\u66f4\u65b0\u77ed\u4fe1\u6a21\u677f\u65f6\u65e0\u6cd5\u786e\u8ba4\u6240\u5c5e\u7528\u6237");
        }
        this.smConfigManager.saveTemplate(SmTemplate);
    }
    
    public SmTemplate getSmsTemplate(final long userId) {
        return this.smConfigManager.getTemplate(userId);
    }
    
    public SmTemplate getSmsTemplate(final String username) {
        final User user = this.userDao.getUserByUserName(username);
        return this.getSmsTemplate(user.getId());
    }
    
    public boolean verifyPassword(final long id, final String plainPassword) {
        final User user = this.getUser(id);
        if (user == null) {
            return false;
        }
        final byte[] salt = Encodes.decodeHex(user.getSalt());
        final byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, 1024);
        return Encodes.encodeHex(hashPassword).equals(user.getPassword());
    }
    
    public List<User> findAll() {
        return (List<User>)this.userDao.find().asList();
    }
    
    public List<User> findNeedBillFileUsers() {
        return this.userDao.findNeedBillFileUsers();
    }
    
    public List<UserInfo> findNotEqualsZeroUsers() {
        final List<FundAccount> accounts = this.fundAccountDao.findNotEqualsZeroAccounts();
        final List<UserInfo> users = new ArrayList<UserInfo>();
        for (final FundAccount account : accounts) {
            final User user = (User)this.userDao.get(account.getUserId());
            if (user != null) {
                final UserInfo userInfo = new UserInfo();
                BeanMapper.copy((Object)user, (Object)userInfo);
                userInfo.setBalance(account.getBalance());
                users.add(userInfo);
            }
        }
        return users;
    }
    
    public Page<User> searchPage(final Page<User> page) {
        final PageParseMongo<?> builder = (PageParseMongo<?>)new PageParseMongo((Page)page);
        final Query<User> q = this.buildQuery(builder.searchParams());
        page.setTotalCount(this.userDao.count((Query)q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.userDao.find((Query)q).asList());
        return page;
    }
    
    private Query<User> buildQuery(final Map<String, String> filters) {
        final Query<User> q = (Query<User>)this.userDao.createQuery();
        if (filters.containsKey("EQ_userType")) {
            q.field("userType").equal((Object)Integer.parseInt(filters.get("EQ_userType")));
        }
        if (filters.containsKey("EQ_username")) {
            q.field("username").equal((Object)filters.get("EQ_username"));
        }
        if (filters.containsKey("EQ_displayName")) {
            q.field("displayName").equal((Object)filters.get("EQ_displayName"));
        }
        return q;
    }
    
    private boolean isSupervisor(final long id) {
        return id == 1L;
    }
    
    private void entryptPassword(final User user) {
        final byte[] salt = Digests.generateSalt(8);
        user.setSalt(Encodes.encodeHex(salt));
        final byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, 1024);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }
}
