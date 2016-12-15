package com.jiam365.flow.plugins.migu;

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
import java.util.Date;

public class MiguHandler extends AbstractHandler {

    private String MARK = "MIGU";
    private static Logger logger = LoggerFactory.getLogger(MiguHandler.class);
    private String rechargeUrl;
    private String account;
    private String apiKey;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public String getChargeType(String productId) {
        if(productId.startsWith("NA")) {
            return "1";//全国流量
        } else if(productId.endsWith("$")) {
            return "3";//省内流量
        } else {
            return "2";//省网流量
        }
    }
    @Override
    public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
        OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
        String orderId = StringIdGenerator.get();
        dto.setAccount(account);
        dto.setChargetype(getChargeType(request.getProductId()));
        dto.setOrderno(orderId);
        dto.setMobile(request.getMobile());
        dto.setProcode(request.getOrigiProductId());
        dto.generateSignature(apiKey);
        String url="";
        url=rechargeUrl+"?action="+dto.getAction()+
                "&account="+dto.getAccount()+
                "&mobile="+dto.getMobile()+
                "&orderno="+dto.getOrderno()+
                "&procode="+dto.getProcode()+
                "&chargesign="+dto.getChargesign()+
                "&chargetype="+dto.getChargetype();

        HttpGet method = ClientUtils.getGetMethod(url);
        logger.debug(MARK + "_recharge_url:" + url);

        String o = ClientUtils.getJson(method);
        logger.debug(MARK + "_recharge_url ret:" + o);

        OrderCreateResponseDTO res = JSON.parseObject(o, OrderCreateResponseDTO.class);
        ResponseData data = new ResponseData();
        data.setSuccessValue("1000");
        data.setResult(res.getCode());
        data.setMessage(res.getMessage());
        data.setRequestNo(orderId);
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
            throw new ChannelConnectionException("");
        } else {
            ResponseData data = new ResponseData();
            MiguReport rechargeReport = JSON.parseObject(json, MiguReport.class);
            data.setRequestNo(rechargeReport.getOrderNumber());
            String ret_msg = rechargeReport.getMessage();
            String ret_code = rechargeReport.getStatus();
            data.setMessage(ret_msg);
            data.setResult(ret_code);
            data.setSuccessValue("2");
            return data;
        }
    }

    @Override
    public void loadParams(String paramJson) {
        Params params = JSON.parseObject(paramJson, Params.class);
        rechargeUrl = params.getRechargeUrl();
        account = params.getAccount();
        apiKey = params.getApiKey();
    }

    @Override
    public String getParamTemplate() {
        return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"account\":\"账号\"," + "\"apiKey\":\"密钥\"" + "}";
    }

}
