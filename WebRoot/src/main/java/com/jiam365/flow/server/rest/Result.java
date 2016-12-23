// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import java.util.HashMap;
import java.util.Map;

public final class Result
{
    private static Map<String, String> resultCodes;
    public static final String CODE_SUCCESS = "10000";
    public static final String CODE_INVALID_REQUEST = "50001";
    public static final String CODE_ACCOUNT_ERROR = "50002";
    public static final String CODE_DECRYPT_ERROR = "50003";
    public static final String CODE_INVALID_MOBILE = "50004";
    public static final String CODE_INSUFFICENT_BALANCE = "50005";
    public static final String CODE_INVALID_VISITOR = "50006";
    public static final String CODE_NOT_AUTHENTICATED = "50007";
    public static final String CODE_INVALID_RECHARETIME = "50008";
    public static final String CODE_DUPLICATED_USERREQNO = "50009";
    public static final String CODE_IN_PROGRESS = "10001";
    public static final String CODE_TRADE_SUCCESS = "20000";
    public static final String CODE_TRADE_FAIL = "50100";
    public static final String CODE_TRADE_NOTFOUND = "50101";
    
    public static String msg(final String code) {
        return Result.resultCodes.get(code);
    }
    
    static {
        (Result.resultCodes = new HashMap<String, String>()).put("10000", "\u64cd\u4f5c\u6267\u884c\u6210\u529f");
        Result.resultCodes.put("50001", "\u8bf7\u6c42\u7684\u53c2\u6570\u4e0d\u5408\u6cd5");
        Result.resultCodes.put("50002", "\u8d26\u6237ID\u4e0e\u5bc6\u94a5\u65e0\u6548\u6216\u4e0d\u5339\u914d");
        Result.resultCodes.put("50003", "\u6570\u636e\u89e3\u5bc6\u5931\u8d25");
        Result.resultCodes.put("50004", "\u53d7\u8d60\u53f7\u7801\u65e0\u6548");
        Result.resultCodes.put("50005", "\u8d26\u6237\u4f59\u989d\u4e0d\u8db3");
        Result.resultCodes.put("50006", "IP\u5730\u5740\u4e0d\u5728\u6388\u6743\u8303\u56f4");
        Result.resultCodes.put("50007", "\u7528\u6237\u8ba4\u8bc1\u5931\u8d25");
        Result.resultCodes.put("50008", "\u6bcf\u6708\u7684\u6700\u540e\u51e0\u5929\u4e0d\u80fd\u5145\u503c");
        Result.resultCodes.put("50009", "\u91cd\u590d\u7684\u7528\u6237\u8ba2\u5355\u53f7");
        Result.resultCodes.put("10001", "\u5145\u503c\u8fdb\u884c\u4e2d");
        Result.resultCodes.put("20000", "\u5145\u503c\u6210\u529f");
        Result.resultCodes.put("50100", "\u5145\u503c\u5931\u8d25");
        Result.resultCodes.put("50101", "\u4ea4\u6613\u4e0d\u5b58\u5728");
    }
}
