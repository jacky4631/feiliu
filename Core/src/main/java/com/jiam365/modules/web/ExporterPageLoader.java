package com.jiam365.modules.web;

import com.jiam365.modules.persistent.Page;
import javax.servlet.http.HttpServletRequest;

public class ExporterPageLoader<T>
        extends WebPageLoader<T>
{
    public ExporterPageLoader(HttpServletRequest request, String... filterPrefixs)
    {
        super(request, filterPrefixs);
    }

    public Page<T> parse()
    {
        Page<T> page = new Page();
        page.setPageSize(500000L);
        page.setCurrentPage(1L);
        page.setSearchFilters(buildSearchFilters());
        return page;
    }
}
