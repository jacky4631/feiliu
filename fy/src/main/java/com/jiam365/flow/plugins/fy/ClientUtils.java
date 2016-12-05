package com.jiam365.flow.plugins.fy;


import com.jiam365.modules.mapper.JsonMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ClientUtils {
    private static JsonMapper mapper;

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
//             method.setHeader("header", "Content-Type: application/x-www-form-urlencoded");
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("username", ((OrderCreateRequestDTO)dto).getUsername()));
            list.add(new BasicNameValuePair("password",((OrderCreateRequestDTO)dto).getPassword()));
            list.add(new BasicNameValuePair("echostr", ((OrderCreateRequestDTO)dto).getEchostr()));
            list.add(new BasicNameValuePair("orderid", ((OrderCreateRequestDTO)dto).getOrderid()));
            list.add(new BasicNameValuePair("timestamp", ((OrderCreateRequestDTO)dto).getTimestamp()));
            list.add(new BasicNameValuePair("phone", ((OrderCreateRequestDTO)dto).getPhone()));
            list.add(new BasicNameValuePair("type", ((OrderCreateRequestDTO)dto).getType()));
            list.add(new BasicNameValuePair("product",((OrderCreateRequestDTO)dto).getProduct()));
            list.add(new BasicNameValuePair("sign",((OrderCreateRequestDTO)dto).getSign()));
            HttpEntity requestEntity=null;
            try {
                requestEntity = new UrlEncodedFormEntity(list,"utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
        } catch(Exception e) {
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
            String body = (entity != null) ? EntityUtils.toString(entity) : null;

            //System.out.println("res get: " + body);
//            JSONObject object = JSON.parseObject(body);
            return body;
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            }
        }

    }

    public static CloseableHttpClient buildHttpClient() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        return httpclient;
    }

    public static JsonMapper getJsonMapper(){
        if(mapper == null) {
            mapper = JsonMapper.nonEmptyMapper();
        }
        return mapper;
    }
}
