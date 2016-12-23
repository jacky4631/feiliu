package com.jiam365.modules.web;

import com.jiam365.modules.persistent.Page;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class DataTablePageLoader<T>
        extends WebPageLoader<T>
{
    public DataTablePageLoader(HttpServletRequest request, String... filterPrefixs)
    {
        super(request, filterPrefixs);
    }

    private int readIntValue(String paramName, boolean fireExceptionWhenNotExpect)
    {
        String str = this.request.getParameter(paramName);
        if (StringUtils.isNumeric(str)) {
            return Integer.parseInt(str);
        }
        if (fireExceptionWhenNotExpect) {
            throw new RuntimeException("Page error, can't get the DataTables component parameters.");
        }
        return 0;
    }

    private Map<String, String> buildSortMethod()
    {
        Map<String, String> sortMap = new HashMap();
        String[] cols = getAllDataTablesColumnNames();
        String[][] orders = getSortedColumns();
        for (String[] order : orders)
        {
            int colNum = Integer.parseInt(order[0]);
            if (!cols[colNum].equalsIgnoreCase("null")) {
                if ("asc".equalsIgnoreCase(order[1])) {
                    sortMap.put(cols[colNum], "asc");
                } else {
                    sortMap.put(cols[colNum], "desc");
                }
            }
        }
        return injectDefaultSortWhenNoSort(sortMap);
    }

    private Map<String, String> injectDefaultSortWhenNoSort(Map<String, String> sortMap)
    {
        if (sortMap.isEmpty()) {
            sortMap.put("id", "desc");
        }
        return sortMap;
    }

    private String[] getAllDataTablesColumnNames()
    {
        int len = readIntValue("iColumns", true);
        String[] cols = new String[len];
        for (int i = 0; i < len; i++) {
            cols[i] = this.request.getParameter("mDataProp_" + i);
        }
        return cols;
    }

    private String[][] getSortedColumns()
    {
        int len = readIntValue("iSortingCols", true);
        String[][] orders = new String[len][2];
        for (int i = 0; i < len; i++)
        {
            orders[i][0] = this.request.getParameter("iSortCol_" + i);
            orders[i][1] = this.request.getParameter("sSortDir_" + i);
        }
        return orders;
    }

    public Page<T> parse()
    {
        Page<T> page = new Page();
        int length = readIntValue("iDisplayLength", false);
        page.setPageSize(length == 0 ? 15L : length);
        page.setCurrentPage(readIntValue("iDisplayStart", false) / page.getPageSize() + 1L);
        page.setSearchFilters(buildSearchFilters());
        page.setSortMap(buildSortMethod());

        return page;
    }
}
