package com.jiam365.modules.persistent;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageParseMongo<T> {
    private Page<T> page;

    public PageParseMongo(Page<T> page) {
        this.page = page;
    }

    public Map<String, String> searchParams() {
        Map<String, String> params = new HashMap();
        List<SearchFilter> searchFilters = this.page.getSearchFilters();
        if (this.page.getSearchFilters() != null) {
            for (SearchFilter filter : searchFilters) {
                String matchType = filter.getMatchType().name();
                String propertyName = filter.getPropertyNames()[0];
                params.put(matchType + "_" + propertyName, filter.getValue());
            }
        }
        return params;
    }

    public DBObject sortParams(List<String> sortFields) {
        DBObject sort = new BasicDBObject();
        Map<String, String> sortMaps = this.page.getSortMap();
        for (Map.Entry<String, String> entry : sortMaps.entrySet()) {
            String sortValue = (String) entry.getValue();
            if ((sortFields.contains(entry.getKey())) && (sortValue != null)) {
                if (sortValue.toUpperCase().equals("ASC")) {
                    sort.put((String) entry.getKey(), Integer.valueOf(1));
                } else {
                    sort.put((String) entry.getKey(), Integer.valueOf(-1));
                }
            }
        }
        return sort;
    }

    public String sortParams() {
        StringBuilder sb = new StringBuilder();
        Map<String, String> sortMaps = this.page.getSortMap();
        for (Map.Entry<String, String> entry : sortMaps.entrySet()) {
            String sortValue = (String) entry.getValue();
            String col;
            if (sortValue.toUpperCase().equals("ASC")) {
                col = (String) entry.getKey();
            } else {
                col = "-" + (String) entry.getKey();
            }
            if (sb.length() > 0) {
                sb.append(",").append(col);
            } else {
                sb.append(col);
            }
        }
        return sb.toString();
    }
}
