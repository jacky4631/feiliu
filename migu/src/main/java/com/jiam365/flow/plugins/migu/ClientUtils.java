package com.jiam365.flow.plugins.migu;


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
            list.add(new BasicNameValuePair("customerOrderId", ((OrderCreateRequestDTO)dto).getCustomerOrderId()));
            list.add(new BasicNameValuePair("enterpriseCode",((OrderCreateRequestDTO)dto).getEnterpriseCode()));
            list.add(new BasicNameValuePair("productCode", ((OrderCreateRequestDTO)dto).getProductCode()));
            list.add(new BasicNameValuePair("mobile", ((OrderCreateRequestDTO)dto).getMobile()));
            list.add(new BasicNameValuePair("orderTime", ((OrderCreateRequestDTO)dto).getOrderTime()));
            list.add(new BasicNameValuePair("sign", ((OrderCreateRequestDTO)dto).getSign()));
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

}
