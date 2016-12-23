// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import java.io.IOException;
import com.jiam365.modules.net.ZFtpClient;
import com.jiam365.flow.base.web.WebResponse;
import com.jiam365.modules.net.ZFtpClientConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/ftp" })
public class FtpAccountVerifyController
{
    @RequestMapping(value = { "verify" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse verify(final ZFtpClientConfig config) {
        final ZFtpClient client = new ZFtpClient(config);
        try {
            if (client.connect()) {
                return WebResponse.success("测试连接成功");
            }
        }
        catch (IOException ex) {}
        return WebResponse.fail("测试连接失败");
    }
}
