package com.jiam365.flow.plugins.wuyika;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
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
             list.add(new BasicNameValuePair("Command", ((OrderCreateRequestDTO)dto).getCommand()));
             list.add(new BasicNameValuePair("MerID",((OrderCreateRequestDTO)dto).getMerID()));
             list.add(new BasicNameValuePair("OrderID", ((OrderCreateRequestDTO)dto).getOrderID()));
             list.add(new BasicNameValuePair("ChgMobile", ((OrderCreateRequestDTO)dto).getChgMobile()));
             list.add(new BasicNameValuePair("FlowAmount", ((OrderCreateRequestDTO)dto).getFlowAmount()));
             list.add(new BasicNameValuePair("ISP", ((OrderCreateRequestDTO)dto).getISP()));
             list.add(new BasicNameValuePair("ReplyFormat", ((OrderCreateRequestDTO)dto).getReplyFormat()));
             list.add(new BasicNameValuePair("InterfaceNumber",((OrderCreateRequestDTO)dto).getInterfaceNumber()));
             list.add(new BasicNameValuePair("Datetime",((OrderCreateRequestDTO)dto).getDatatime()));
             list.add(new BasicNameValuePair("MerURL",((OrderCreateRequestDTO)dto).getMerURL()));
             list.add(new BasicNameValuePair("FlowType ",((OrderCreateRequestDTO)dto).getFlowType()));
             list.add(new BasicNameValuePair("Attach ",((OrderCreateRequestDTO)dto).getAttach()));
             list.add(new BasicNameValuePair("Sign ",((OrderCreateRequestDTO)dto).getSign()));
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

    

    public static JSONObject getJson(HttpGet method) {
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
            JSONObject object = JSON.parseObject(body);
            return object;
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
