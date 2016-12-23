// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import org.springframework.web.servlet.ModelAndView;
import java.util.Date;
import com.jiam365.flow.server.entity.User;
import com.jiam365.flow.server.security.RestAuthHeader;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.utils.Encodes;
import com.jiam365.modules.utils.Digests;
import org.apache.commons.lang3.time.DateUtils;
import com.jiam365.flow.server.security.RestAuthorizationException;
import com.jiam365.flow.server.security.RestAuthUtils;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.web.servlet.HandlerInterceptor;

public class RestAuthInterceptor implements HandlerInterceptor
{
    private UserManager userManager;
    
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        try {
            final RestAuthHeader restAuthHeader = RestAuthUtils.getAuthorization(request);
            final String username = restAuthHeader.getUsername();
            final User user = this.userManager.getUserByUsername(username);
            if (user == null) {
                throw new RestAuthorizationException("User not found");
            }
            if (!user.isEnabled()) {
                throw new RestAuthorizationException("Authorization fail");
            }
            final String timestamp = restAuthHeader.getTimestamp();
            final Date date = DateUtils.parseDate(timestamp, new String[] { "yyyyMMddHHmmss" });
            final long now = System.currentTimeMillis();
            final long stamp = date.getTime();
            if (Math.abs(now - stamp) > 1200000L) {
                throw new RestAuthorizationException("Authorization fail");
            }
            final String needHash = username + user.getAuthToken() + restAuthHeader.getTimestamp();
            final byte[] md5result = Digests.md5(needHash.getBytes());
            final String sign2 = Encodes.encodeHex(md5result);
            if (StringUtils.equalsIgnoreCase((CharSequence)restAuthHeader.getSign(), (CharSequence)sign2)) {
                RestAuthUtils.setSession(username);
                return true;
            }
            throw new RestAuthorizationException("Authorization fail");
        }
        catch (Exception e) {
            if (e instanceof RestAuthorizationException) {
                throw e;
            }
            throw new RestAuthorizationException(e.getMessage(), e);
        }
    }
    
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
    }
    
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        RestAuthUtils.cleanThreadLocalSession();
    }
    
    public void setUserManager(final UserManager userManager) {
        this.userManager = userManager;
    }
}
