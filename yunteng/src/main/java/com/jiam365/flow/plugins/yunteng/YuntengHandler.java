package com.jiam365.flow.plugins.yunteng;

import com.alibaba.fastjson.JSON;
import com.jiam365.flow.sdk.AbstractHandler;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.utils.StringIdGenerator;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class YuntengHandler extends AbstractHandler {

    private String MARK = "YUNTENG";
    private static Logger logger = LoggerFactory.getLogger(YuntengHandler.class);
    private String rechargeUrl;
    private String appid;
    private String apiKey;
    private String callbackUrl;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
        OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
        String orderId = StringIdGenerator.get();
        dto.setChannelOrderId(orderId);
        dto.setAmount(request.getSize()*1024);
        dto.setMobile(request.getMobile());
        if(TextUtils.isEmpty(callbackUrl)){
            callbackUrl = "http://120.55.71.93/report/yunteng";
        }
        dto.setCallbackUrl(callbackUrl);
        String sign = dto.generateSignature(apiKey);
        String url="";
        url=rechargeUrl+"/" + appid +"?sign="+sign;

        HttpPost method = ClientUtils.getPostMethod(url);
        logger.debug(MARK + "_recharge_url:" + url);

        String o = ClientUtils.getJson(method, dto);
        logger.debug(MARK + "_recharge_url ret:" + o);

        OrderCreateResponseDTO res = JSON.parseObject(o, OrderCreateResponseDTO.class);
        ResponseData data = new ResponseData();
        data.setSuccessValue("0000");
        data.setResult(res.getStatus());
        data.setMessage(res.getFailReason());
        data.setRequestNo(res.getOrderId());
        return data;
    }

    @Override
    public ResponseData queryReport(RechargeRequest rechargeRequest, String reqNo) throws ChannelConnectionException {
        String json = TradeReportServiceProxy.fetch(reqNo);
        logger.debug(MARK + "_report_json:" + json);
        return callback(json, reqNo);
    }

    public ResponseData callback(String json, String reqNo) {
        if (TextUtils.isEmpty(json)) {
            throw new ChannelConnectionException("没有收到云腾回调");
        } else {
            ResponseData data = new ResponseData();
            data.setRequestNo(reqNo);
            YuntengReport rechargeReport = JSON.parseObject(json, YuntengReport.class);
            String ret_msg = rechargeReport.getFailReason();
            String ret_code = rechargeReport.getStatus();
            data.setMessage(ret_msg);
            data.setResult(ret_code);
            data.setSuccessValue("1");
            return data;
        }
    }

    @Override
    public void loadParams(String paramJson) {
        Params params = JSON.parseObject(paramJson, Params.class);
        rechargeUrl = params.getRechargeUrl();
        appid = params.getAppid();
        apiKey = params.getApiKey();
        callbackUrl = params.getCallbackUrl();
    }

    @Override
    public String getParamTemplate() {
        return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"appid\":\"应用号\"," + "\"apiKey\":\"密钥\"," + "\"callbackUrl\":\"回调地址\"" + "}";
    }

}
