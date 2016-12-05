package com.jiam365.flow.plugins.fy;

import com.jiam365.modules.mapper.JsonMapper;
import junit.framework.TestCase;

/**
 * Created by 沈吉 on 2016/12/2.
 */
public class FYCallbackControllerTest extends TestCase {
    private FYCallbackController controller;
    public void setUp() throws Exception {
        super.setUp();
        controller = new FYCallbackController();
    }

    public void tearDown() throws Exception {

    }

    public void testParse() throws Exception {
        FYReport report = new FYReport();
        report.setOrderid("201612");
        report.setCode("000000");
        report.setTimestamp("1111111111");
        report.setMsg("abc");
        report.setSgin("sign");
        JsonMapper mapper = new JsonMapper();
//        String res = controller.parse(mapper.toJson(report));
        String res = controller.parse("sign=ba57a39ff708e1a1fec86af54ae56102&timestamp=20161205215014&code=111111&orderid=201612052149591000001&msg=%E5%85%85%E5%80%BC%E6%88%90%E5%8A%9F");
        assertEquals(res, "000000");
    }

}