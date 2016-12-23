package com.jiam365.modules.web;

import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.SearchFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

public abstract class WebPageLoader<T>
        implements PageParamLoader<T>
{
    protected HttpServletRequest request;
    protected String filterPrefix;

    public WebPageLoader(HttpServletRequest request, String... filterPrefixs)
    {
        this.request = request;
        this.filterPrefix = (filterPrefixs.length > 0 ? filterPrefixs[0] : "filter_");
    }

    public List<SearchFilter> buildSearchFilters()
    {
        Map<String, Object> filterParamMap = readSearchCondition();
        return buildSearchFilters(filterParamMap);
    }

    public List<SearchFilter> buildSearchFilters(Map<String, Object> filterParamMap)
    {
        List<SearchFilter> filterList = new ArrayList();
        for (Map.Entry<String, Object> entry : filterParamMap.entrySet())
        {
            String searchName = (String)entry.getKey();
            Object value = entry.getValue();

            String[] conditions = isArray(value) ? (String[])value : new String[] {(String)value} ;
            for (String condition : conditions) {
                if (StringUtils.isNotBlank(condition))
                {
                    SearchFilter filter = new SearchFilter(searchName, condition);
                    filterList.add(filter);
                }
            }
        }
        return filterList;
    }

    private boolean isArray(Object object)
    {
        assert (object != null);
        Class<?> clazz = object.getClass();
        return clazz.isArray();
    }

    private Map<String, Object> readSearchCondition()
    {
        return WebUtils.getParametersStartingWith(this.request, this.filterPrefix);
    }
}
