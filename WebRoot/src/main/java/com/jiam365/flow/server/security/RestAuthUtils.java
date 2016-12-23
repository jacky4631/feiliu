// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.security;

import java.util.Map;
import com.jiam365.modules.utils.Encodes;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public class RestAuthUtils
{
    private static final ThreadLocal<String> threadSession;
    
    public static void cleanThreadLocalSession() {
        RestAuthUtils.threadSession.remove();
    }
    
    public static RestAuthHeader getAuthorization(final HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        final String[] parts = authorization.split(",");
        if (parts.length != 2) {
            throw new RestAuthorizationException("Authorization info invalid");
        }
        final Map<String, String> values = new HashMap<String, String>();
        for (final String part : parts) {
            final String[] pair = part.split("=");
            values.put(pair[0].trim(), pair[1].trim().replace("\"", ""));
        }
        final String sign = values.get("sign");
        final String nonce = values.get("nonce");
        final String authInfo = new String(Encodes.decodeBase64(nonce));
        final String[] auths = authInfo.split(":");
        if (auths.length != 2) {
            throw new RestAuthorizationException("Authorization info invalid");
        }
        return new RestAuthHeader(auths[0], auths[1], sign);
    }
    
    public static String getSession() {
        return RestAuthUtils.threadSession.get();
    }
    
    public static void setSession(final String s) {
        RestAuthUtils.threadSession.set(s);
    }
    
    static {
        threadSession = new ThreadLocal<String>();
    }
}
