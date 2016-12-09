package com.jiam365.flow.plugins.qiweishu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiam365.flow.sdk.AbstractHandler;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.JSONDataReader;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.utils.StringIdGenerator;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QiweishuHandler extends AbstractHandler {

    private String MARK = "QIWEISHU";
    private static Logger logger = LoggerFactory.getLogger(QiweishuHandler.class);
    private String rechargeUrl;
    private String enterpriseCode;
    private String password;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
        OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
        String orderId = StringIdGenerator.get();
        dto.setCustomerOrderId(orderId);
        dto.setEnterpriseCode(enterpriseCode);
        dto.setProductCode(request.getOrigiProductId());
        dto.setMobile(request.getMobile());
        dto.setOrderTime(dateFormat.format(new Date()));
        dto.generateSignature(password);

        HttpPost method = ClientUtils.getPostMethod(rechargeUrl);
        logger.debug(MARK + "_recharge_url:" + rechargeUrl);

        String o = ClientUtils.getJson(method, dto);
        logger.debug(MARK + "_recharge_url ret:" + o);

        OrderCreateResponseDTO res = JSON.parseObject(o, OrderCreateResponseDTO.class);
        ResponseData data = new ResponseData();
        data.setSuccessValue("true");
        data.setResult(String.valueOf(res.isSuccess()));
        data.setMessage(res.getError());
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
        ResponseData data = new ResponseData();
        data.setRetryValues(new String[]{"-2"});
        data.setRequestNo(reqNo);
        if (json != null) {
            QiweishuReport rechargeReport = ClientUtils.getJsonMapper().fromJson(json, QiweishuReport.class);
            logger.debug(MARK + "_report_bean:" + rechargeReport.toString());
            if (rechargeReport.getData() != null) {
                String ret_msg = rechargeReport.getData().message;
                String ret_code = String.valueOf(rechargeReport.getData().code);
                data.setMessage(ret_msg);
                data.setResult(ret_code);
            } else {
                data.setResult("-2");
                data.setMessage("没有data参数");
            }
            data.setSuccessValue("1");
        } else {
            data.setSuccessValue("0");
            data.setResult("-2");
            data.setMessage("没有回调");
        }
        return data;
    }

    @Override
    public void loadParams(String paramJson) {
        JSONDataReader reader = new JSONDataReader();
        reader.init(paramJson);
        rechargeUrl = reader.read("rechargeUrl");
        enterpriseCode = reader.read("enterpriseCode");
        password = reader.read("appsecret");
        reader.release();
    }

    @Override
    public String getParamTemplate() {
        return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"enterpriseCode\":\"企业代码\"," + "\"password\":\"密码\"" + "}";
    }

}
