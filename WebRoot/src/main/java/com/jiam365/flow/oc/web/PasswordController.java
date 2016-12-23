// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.server.entity.User;
import com.jiam365.flow.oc.utils.EventUtils;
import com.jiam365.flow.base.utils.ShiroUtils;
import org.springframework.http.HttpStatus;
import com.jiam365.flow.base.web.WebResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc" })
public class PasswordController
{
    @Autowired
    private UserManager userManager;
    
    @RequestMapping(value = { "passwd" }, method = { RequestMethod.GET })
    public String passwdForm() {
        return "oc/passwd";
    }
    
    @RequestMapping(value = { "passwd" }, method = { RequestMethod.POST }, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<?> passwd(final String oldPassword, final String password, final String password1) {
        if (!StringUtils.equals((CharSequence)password1, (CharSequence)password) || StringUtils.isBlank((CharSequence)password)) {
            return (ResponseEntity<?>)new ResponseEntity((Object)WebResponse.fail("新密码输入不正确或不一致"), HttpStatus.OK);
        }
        final long userId = ShiroUtils.currentUserId();
        if (!this.userManager.verifyPassword(userId, oldPassword)) {
            return (ResponseEntity<?>)new ResponseEntity((Object)WebResponse.fail("旧密码输入错误"), HttpStatus.OK);
        }
        final User user = this.userManager.getUser(userId);
        user.setPlainPassword(password);
        this.userManager.updateUser(user);
        EventUtils.publishLogEvent("修改个人密码");
        return (ResponseEntity<?>)new ResponseEntity((Object)WebResponse.success("密码修改成功"), HttpStatus.OK);
    }
}
