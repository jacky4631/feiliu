// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.usercallback;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import java.util.List;

public class ReportMessageFilter
{
    private static List<String> keywords;
    
    public static String filter(String src) {
        if (StringUtils.isBlank((CharSequence)src)) {
            return src;
        }
        for (final String keyword : ReportMessageFilter.keywords) {
            if (src.contains(keyword)) {
                src = src.replace(keyword, "");
            }
        }
        return src;
    }
    
    static {
        (ReportMessageFilter.keywords = new ArrayList<String>()).add("\u4f59\u989d\u4e0d\u8db3");
        ReportMessageFilter.keywords.add("5\uff1a");
        ReportMessageFilter.keywords.add("2\uff1a");
        ReportMessageFilter.keywords.add("1\uff1a");
        ReportMessageFilter.keywords.add("3\uff1a");
        ReportMessageFilter.keywords.add("S:");
        ReportMessageFilter.keywords.add("ERR:");
        ReportMessageFilter.keywords.add("999\uff1a");
        ReportMessageFilter.keywords.add("05:");
        ReportMessageFilter.keywords.add("004:");
    }
}
