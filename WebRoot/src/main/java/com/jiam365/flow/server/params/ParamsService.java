// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.params;

import java.util.concurrent.ExecutionException;
import com.jiam365.flow.server.entity.Params;
import com.jiam365.modules.mapper.JsonMapper;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.dao.ParamsDao;

public class ParamsService
{
    @Autowired
    private ParamsDao paramsDao;
    private LoadingCache<Class<?>, Object> cache;
    
    public ParamsService() {
        this.cache = (LoadingCache<Class<?>, Object>)CacheBuilder.newBuilder().maximumSize(10L).expireAfterWrite(5L, TimeUnit.MINUTES).build((CacheLoader)new CacheLoader<Class<?>, Object>() {
            public Object load(final Class<?> clazz) {
                final Params params = ParamsService.this.get(clazz);
                final JsonMapper mapper = JsonMapper.nonEmptyMapper();
                return mapper.fromJson(params.getObjJson(), (Class)clazz);
            }
        });
    }
    
    public TimeoutParam loadTimeoutParam() {
        try {
            return (TimeoutParam)this.cache.get(TimeoutParam.class);
        }
        catch (Exception e) {
            return new TimeoutParam();
        }
    }
    
    public void saveTimeoutParam(final TimeoutParam timeoutParam) {
        this.save(timeoutParam);
        this.cache.refresh(TimeoutParam.class);
    }
    
    public FunctionLimitParam loadFunctionLimitParam() {
        try {
            return (FunctionLimitParam)this.cache.get(FunctionLimitParam.class);
        }
        catch (Exception e) {
            return new FunctionLimitParam();
        }
    }
    
    public void saveFunctionLimitParam(final FunctionLimitParam functionLimitParam) {
        this.save(functionLimitParam);
        this.cache.refresh(FunctionLimitParam.class);
    }
    
    public UserReportParam loadUserReportParam() {
        try {
            return (UserReportParam)this.cache.get(UserReportParam.class);
        }
        catch (Exception ignore) {
            return new UserReportParam();
        }
    }
    
    public void saveUserReport(final UserReportParam userReportParam) {
        this.save(userReportParam);
        this.cache.refresh(UserReportParam.class);
    }
    
    public ProductSelectionParam loadProductSelectionParam() {
        try {
            return (ProductSelectionParam)this.cache.get(ProductSelectionParam.class);
        }
        catch (ExecutionException ignore) {
            return new ProductSelectionParam();
        }
    }
    
    public void saveProductSelectionParam(final ProductSelectionParam productSelectionParam) {
        this.save(productSelectionParam);
        this.cache.refresh(ProductSelectionParam.class);
    }
    
    protected void save(final Object object) {
        final JsonMapper mapper = JsonMapper.nonEmptyMapper();
        final String id = object.getClass().getCanonicalName();
        final Params params = new Params(id, mapper.toJson(object));
        this.paramsDao.save(params);
    }
    
    protected Params get(final Class<?> clazz) {
        final String id = clazz.getCanonicalName();
        Params params = (Params)this.paramsDao.get(id);
        if (params == null) {
            params = new Params(id, "{}");
        }
        return params;
    }
    
    public void deleteParams(final Class<?> clazz) {
        final String id = clazz.getCanonicalName();
        this.paramsDao.deleteById(id);
    }
}
