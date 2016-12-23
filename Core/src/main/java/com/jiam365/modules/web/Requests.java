package com.jiam365.modules.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class Requests
{
    public static String srcIp(HttpServletRequest request)
    {
        String[] headers = { "X-Real-IP", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP" };
        for (String header : headers)
        {
            String ip = request.getHeader(header);
            if ((StringUtils.isNotBlank(ip)) && (!"unknown".equalsIgnoreCase(ip))) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
