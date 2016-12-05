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
        String res = controller.parse(mapper.toJson(report));
        assertEquals(res, "000000");
    }

}