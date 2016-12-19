package com.jiam365.flow.plugins.lingdian;


import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClientUtils {

    public static HttpPost getPostMethod(String rechargeUrl) {
        HttpPost httppost = new HttpPost(rechargeUrl);
        return httppost;
    }

    public static HttpGet getGetMethod(String rechargeUrl) {
        HttpGet httpget = new HttpGet(rechargeUrl);
        return httpget;
    }


    public static String getJson(HttpPost method, Object dto) {

        CloseableHttpClient httpClient = buildHttpClient();
        if (dto != null) {
            method.setHeader("Content-Type", "application/json;charset=UTF-8");
            String requestJsonBody = JSON.toJSONString(dto);
            System.out.println("req:" + requestJsonBody);
            HttpEntity requestEntity = EntityBuilder.create().setBinary(requestJsonBody.getBytes()).build();
            method.setEntity(requestEntity);
        }
        try {
            HttpResponse response = httpClient.execute(method);
            int status = response.getStatusLine().getStatusCode();
            if (status < 200 || status >= 300) {
                throw new ClientProtocolException("远程状态码错误: status=" + status);
            }
            HttpEntity entity = response.getEntity();
            String body = (entity != null) ? EntityUtils.toString(entity) : null;
            return body;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            }
        }

    }


    public static String getJson(HttpGet method) {
        CloseableHttpClient httpClient = buildHttpClient();
        try {
            HttpResponse response = httpClient.execute(method);
            int status = response.getStatusLine().getStatusCode();
            if (status < 200 || status >= 300) {
                throw new ClientProtocolException("远程状态码错误: status=" + status);
            }
            HttpEntity entity = response.getEntity();

            return convertStreamToString(entity.getContent());
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            }
        }

    }

    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();

        String line = null;

        try {

            while ((line = reader.readLine()) != null) {

                sb.append(line + "\n");

            }

        } catch (IOException e) {

        } finally {

            try {

                is.close();

            } catch (IOException e) {

            }

        }

        return sb.toString();

    }

    public static CloseableHttpClient buildHttpClient() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        return httpclient;
    }
}
