package com.jiam365.flow.plugins.xinkedang;

import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class XinkedangCallbackControllerTest extends TestCase {
    private XinkedangCallbackController controller;
    public void setUp() throws Exception {
        super.setUp();
        controller = new XinkedangCallbackController();
    }

    public void tearDown() throws Exception {

    }

    public void testParse() throws Exception {
        String ret = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                " <soap:Body>\n" +
                "  <ns2:notice xmlns:ns2=\"http://callback.webservice.wujun.com/\">\n" +
                "   <ORDER>\n" +
                "    <SERIAL_NUMBER>73175072200</SERIAL_NUMBER>\n" +
                "    <ORDER_ID>N2014050418063489011248</ORDER_ID>\n" +
                "    <FS_CODE>0000</FS_CODE>\n" +
                "    <FS_MESSAGE>成功</FS_MESSAGE>\n" +
                "   </ORDER>\n" +
                "  </ns2:notice>\n" +
                " </soap:Body>\n" +
                "</soap:Envelope>\n";
        String res = controller.parse(ret);
        System.out.println(res);
    }

}