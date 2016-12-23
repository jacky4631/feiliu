package com.jiam365.flow.server.utils;

import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.modules.net.HttpClients;
import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpInvokeUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpInvokeUtils.class);

    public static /* varargs */ String postTLS(String url, String body, String contentType, String encoding, int timeout, Header ... headers) {
        return HttpInvokeUtils.doPost(true, url, body, contentType, encoding, timeout, headers);
    }

    public static /* varargs */ String post(String url, String body, String contentType, String encoding, int timeout, Header ... headers) {
        return HttpInvokeUtils.doPost(false, url, body, contentType, encoding, timeout, headers);
    }


    private static String doPost(final boolean useTLS, final String url, final String body, final String contentType, final String encoding, final int timeout, final Header... headers) {
        try (final CloseableHttpClient httpClient = HttpClients.newClient(useTLS, timeout, timeout)) {
            final StringEntity stringEntity = new StringEntity(body, encoding);
            stringEntity.setContentType(contentType);
            final HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity((HttpEntity)stringEntity);
            for (final Header header : headers) {
                httpPost.addHeader(header);
            }
            try (final CloseableHttpResponse res = httpClient.execute((HttpUriRequest)httpPost)) {
                final int responseCode = res.getStatusLine().getStatusCode();
                if (responseCode == 200) {
                    return EntityUtils.toString(res.getEntity(), encoding);
                }
                if (responseCode == 502) {
                    throw new ChannelConnectionException((Throwable)new IllegalStateException("\u63a5\u53e3\u670d\u52a1\u65b9\u8fd4\u56deBad Gateway(502)\u9519\u8bef"), new boolean[0]);
                }
                throw new ChannelConnectionException("\u8c03\u7528" + url + "\u8fd4\u56de\u9519\u8bef\u7801:" + responseCode, new boolean[0]);
            }
        }
        catch (IOException | IllegalStateException e) {
            HttpInvokeUtils.logger.warn("\u8c03\u7528{}\u9519\u8bef, \u9519\u8bef\u7c7b\u578b: {}, {} \u62a5\u6587:{}", new Object[] { url, e.getClass(), e.getMessage(), body });
            throw new ChannelConnectionException((Throwable)e, new boolean[0]);
        }
    }

    public static String post(String url, String body, String contentType, String encoding) {
        return HttpInvokeUtils.post(url, body, contentType, encoding, Configs.TIMEOUT_DEFAULT, new Header[0]);
    }

    public static String post(String url, String body, String contentType) {
        return HttpInvokeUtils.post(url, body, contentType, Configs.UTF8_ENCODING, Configs.TIMEOUT_DEFAULT, new Header[0]);
    }

    public static /* varargs */ String getTLS(String url, String encoding, int timeout, Header ... headers) {
        return HttpInvokeUtils.doGet(true, url, encoding, timeout, headers);
    }

    public static /* varargs */ String get(String url, String encoding, int timeout, Header ... headers) {
        return HttpInvokeUtils.doGet(false, url, encoding, timeout, headers);
    }
    private static String doGet(final boolean useTLS, final String url, final String encoding, final int timeout, final Header... headers) {
        try (final CloseableHttpClient httpClient = HttpClients.newClient(useTLS, timeout, timeout)) {
            final HttpGet httpGet = new HttpGet(url);
            for (final Header header : headers) {
                httpGet.addHeader(header);
            }
            try (final CloseableHttpResponse res = httpClient.execute((HttpUriRequest)httpGet)) {
                final int responseCode = res.getStatusLine().getStatusCode();
                if (responseCode == 200) {
                    return EntityUtils.toString(res.getEntity(), encoding);
                }
                if (responseCode == 502) {
                    throw new ChannelConnectionException((Throwable)new IllegalStateException("\u63a5\u53e3\u670d\u52a1\u65b9\u8fd4\u56deBad Gateway(502)\u9519\u8bef"), new boolean[0]);
                }
                throw new ChannelConnectionException("\u8c03\u7528" + url + "\u8fd4\u56de\u9519\u8bef\u7801:" + responseCode, new boolean[0]);
            }
        }
        catch (IOException | IllegalStateException e) {
            HttpInvokeUtils.logger.warn(url + "\u8c03\u7528{}\u9519\u8bef, {}, \u62a5\u6587:{}", (Object)url, (Object)e.getMessage());
            throw new ChannelConnectionException((Throwable)e, new boolean[0]);
        }
    }

    public static String get(String url, String encoding) {
        return HttpInvokeUtils.get(url, encoding, Configs.TIMEOUT_DEFAULT, new Header[0]);
    }

    public static String get(String url) {
        return HttpInvokeUtils.get(url, Configs.UTF8_ENCODING, Configs.TIMEOUT_DEFAULT, new Header[0]);
    }
}

