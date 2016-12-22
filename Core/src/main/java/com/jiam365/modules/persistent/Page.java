package com.jiam365.modules.persistent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page<T>
{
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final int DEFAULT_PAGE_SIZE = 15;
    protected long currentPage = 1L;
    protected long pageSize;
    protected List<T> result = Collections.emptyList();
    protected long totalCount = -1L;
    private List<SearchFilter> searchFilters;
    private Map<String, String> sortMap = new HashMap();

    public Page() {}

    public Page(int pageSize)
    {
        setPageSize(pageSize);
    }

    public long getCurrentPage()
    {
        return this.currentPage;
    }

    public void setCurrentPage(long currentPage)
    {
        this.currentPage = currentPage;
        if (currentPage < 1L) {
            this.currentPage = 1L;
        }
    }

    public Page<T> currentPage(long thePageNo)
    {
        setCurrentPage(thePageNo);
        return this;
    }

    public long getPageSize()
    {
        return this.pageSize;
    }

    public void setPageSize(long pageSize)
    {
        this.pageSize = pageSize;
        if (pageSize < 1L) {
            this.pageSize = 1L;
        }
    }

    public Page<T> pageSize(int thePageSize)
    {
        setPageSize(thePageSize);
        return this;
    }

    public long getFirst()
    {
        return (this.currentPage - 1L) * this.pageSize;
    }

    public List<T> getResult()
    {
        return this.result;
    }

    public void setResult(List<T> result)
    {
        this.result = result;
    }

    public long getTotalCount()
    {
        return this.totalCount;
    }

    public void setTotalCount(long totalCount)
    {
        this.totalCount = totalCount;
    }

    public long getTotalPages()
    {
        if (this.totalCount < 0L) {
            return -1L;
        }
        long count = this.totalCount / this.pageSize;
        if (this.totalCount % this.pageSize > 0L) {
            count += 1L;
        }
        return count;
    }

    public boolean hasNext()
    {
        return this.currentPage + 1L <= getTotalPages();
    }

    public long getNextPage()
    {
        if (hasNext()) {
            return this.currentPage + 1L;
        }
        return this.currentPage;
    }

    public boolean hasPre()
    {
        return this.currentPage - 1L >= 1L;
    }

    public long getPrePage()
    {
        if (hasPre()) {
            return this.currentPage - 1L;
        }
        return this.currentPage;
    }

    public void addSearchFilter(SearchFilter filter)
    {
        if (this.searchFilters == null) {
            this.searchFilters = new ArrayList();
        }
        this.searchFilters.add(filter);
    }

    public void addSort(String field, String type)
    {
        if (this.sortMap == null) {
            this.sortMap = new HashMap();
        }
        this.sortMap.put(field, type);
    }

    public List<SearchFilter> getSearchFilters()
    {
        return this.searchFilters;
    }

    public void setSearchFilters(List<SearchFilter> searchFilters)
    {
        this.searchFilters = searchFilters;
    }

    public Map<String, String> getSortMap()
    {
        return this.sortMap;
    }

    public void setSortMap(Map<String, String> sortMap)
    {
        this.sortMap = sortMap;
    }
}
