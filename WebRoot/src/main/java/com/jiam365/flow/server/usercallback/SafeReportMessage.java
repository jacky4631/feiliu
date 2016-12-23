// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.usercallback;

public class SafeReportMessage
{
    public static String process(final String message) {
        return ReportMessageFilter.filter(safeJsonValue(message));
    }
    
    private static String safeJsonValue(final String string) {
        if (string == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(string.length() + 4);
        for (int i = 0; i < string.length(); ++i) {
            final char c = string.charAt(i);
            switch (c) {
                case '\b':
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case '\"':
                case '\'':
                case '{':
                case '}': {
                    break;
                }
                default: {
                    if (c < ' ') {
                        final String t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                        break;
                    }
                    sb.append(c);
                    break;
                }
            }
        }
        return sb.toString();
    }
}
