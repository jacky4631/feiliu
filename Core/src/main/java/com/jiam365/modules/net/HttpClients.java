package com.jiam365.modules.net;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public final class HttpClients {
    public static CloseableHttpClient create(int connectTimeout, int socketTimeout) {
        return newClient(false, connectTimeout, socketTimeout);
    }

    public static CloseableHttpClient createTLS(int connectTimeout, int socketTimeout) {
        return newClient(true, connectTimeout, socketTimeout);
    }

    public static CloseableHttpClient newClient(boolean useTLS, int connectTimeout, int socketTimeout) {
        X509TrustManager x509mgr = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] xcs, String string) {
            }

            public void checkServerTrusted(X509Certificate[] xcs, String string) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLConnectionSocketFactory sslConnectionSocketFactory;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509mgr}, null);
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();

        HttpClientBuilder builder = HttpClientBuilder.create().setMaxConnTotal(200).setMaxConnPerRoute(4).setDefaultSocketConfig(socketConfig).setDefaultRequestConfig(requestConfig);
        if (useTLS) {
            builder.setSSLSocketFactory(sslConnectionSocketFactory);
        }
        return builder.build();
    }
}
