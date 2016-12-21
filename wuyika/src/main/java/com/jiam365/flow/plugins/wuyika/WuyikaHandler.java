package com.jiam365.flow.plugins.wuyika;

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
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WuyikaHandler extends AbstractHandler {

    private String MARK = "WUYIKA";
    private static Logger logger = LoggerFactory.getLogger(WuyikaHandler.class);
    private String rechargeUrl;
    private String merID;
    private String key;
    private String callbackUrl;
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

    public String getISP(String productId) {
        String id = productId.substring(2,3);
        if("7".equals(id)){//中国电信
            return "3";
        }else if("8".equals(id)){//中国移动
            return "1";
        }else {//9 中国联通
            return "2";
        }
    }

    @Override
    public ResponseData recharge(RechargeRequest request) throws ChannelConnectionException {
        OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
        dto.setMerID(merID);
        String orderId = StringIdGenerator.get();
        dto.setOrderID(orderId);
        dto.setChgMobile(request.getMobile());
        dto.setFlowAmount(String.valueOf(request.getSize()));
        dto.setISP(getISP(request.getProductId()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dto.setDatetime(dateFormat.format(new Date()));
        if(TextUtils.isEmpty(callbackUrl)) {
            callbackUrl = "http://120.55.71.93/report/wuyika";
        }
        dto.setMerURL(callbackUrl);
        dto.setFlowType(getChargeType(request.getProductId()));
        dto.generateSignature(key);

        HttpPost method = ClientUtils.getPostMethod(rechargeUrl);

        String o = ClientUtils.getJson(method,dto);
        logger.debug(MARK + "_recharge_url ret:" + o);

        OrderCreateResponseDTO resDto = new OrderCreateResponseDTO();
        ResponseData data = new ResponseData();
        data.setSuccessValue("0000");
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(o));
            Element root = document.getRootElement();
            List<Element> childElements = root.elements();
            for (Element child : childElements) {
                String name = child.getQualifiedName();
                if("OrderID".equals(name)) {
                    resDto.setOrderID(child.getText());
                } else if ("TranStat".equals(name)) {
                    resDto.setTranStat(child.getText());
                } else if("TranInfo".equals(name)) {
                    resDto.setTranInfo(child.getText());
                }
            }
            data.setResult(resDto.getTranStat());
            data.setMessage(resDto.getTranInfo());
            data.setRequestNo(orderId);
        }catch (DocumentException e) {
            data.setResult("-1");
            data.setMessage(e.getMessage());
            data.setRequestNo(orderId);
        }

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
            throw new ChannelConnectionException("暂未收到伍壹卡回调，订单号:" + reqNo);
        } else {
            ResponseData data = new ResponseData();
            WuyikaReport rechargeReport = JSON.parseObject(json, WuyikaReport.class);
            data.setRequestNo(rechargeReport.getOrderID());
            String ret_msg = "";
            String ret_code = rechargeReport.getOrderStatus();
            if(ret_code.equals("1")){
                ret_msg = "充值成功";
            } else {
                ret_msg = "充值失败";
            }
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
        merID = params.getMerID();
        key = params.getKey();
        callbackUrl = params.getCallbackUrl();
    }

    @Override
    public String getParamTemplate() {
        return "{" + "\"rechargeUrl\":\"充值地址\"," + "\"merID\":\"\"," + "\"key\":\"密钥\"," + "\"callbackUrl\":\"密钥\"" +"}";
    }

}
