package com.jiam365.flow.plugins.migu;

import com.alibaba.fastjson.JSON;
import com.jiam365.flow.sdk.AbstractHandler;
import com.jiam365.flow.sdk.ChannelConnectionException;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.sdk.response.ResponseData;
import com.jiam365.flow.sdk.support.TradeReportServiceProxy;
import com.jiam365.modules.utils.StringIdGenerator;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MiguHandler extends AbstractHandler {

    private String MARK = "QIWEISHU";
    private static Logger logger = LoggerFactory.getLogger(MiguHandler.class);
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
        if (TextUtils.isEmpty(json)) {
            throw new ChannelConnectionException("");
        } else {
            ResponseData data = new ResponseData();
            data.setRequestNo(reqNo);
            MiguReport rechargeReport = JSON.parseObject(json, MiguReport.class);
            String ret_msg = rechargeReport.getReason();
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
        enterpriseCode = params.getEnterpriseCode();
        password = params.getPassword();
    }

    @Override
    public String getParamTemplate() {
        return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"enterpriseCode\":\"企业代码\"," + "\"password\":\"密码\"" + "}";
    }

}
