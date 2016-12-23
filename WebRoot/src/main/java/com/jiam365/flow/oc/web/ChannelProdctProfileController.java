// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.base.web.WebResponse;
import com.jiam365.flow.server.entity.ChannelProductGroupProfile;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.FlowPackageManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/flowpackage/profile" })
public class ChannelProdctProfileController
{
    @Autowired
    private FlowPackageManager flowPackageManager;
    
    @RequestMapping(value = { "update" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse save(final ChannelProductGroupProfile profile) {
        this.flowPackageManager.saveGroupProfile(profile);
        return WebResponse.success("保存成功");
    }
}
