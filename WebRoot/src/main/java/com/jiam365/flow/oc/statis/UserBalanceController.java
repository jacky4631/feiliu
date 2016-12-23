// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.statis;

import com.jiam365.flow.oc.dto.UserInfo;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/statis" })
public class UserBalanceController
{
    @Autowired
    private UserManager userManager;
    
    @RequestMapping({ "balance-users" })
    public String usersBalance(final Model model) {
        final List<UserInfo> userInfoList = this.userManager.findNotEqualsZeroUsers();
        model.addAttribute("userInfoList", (Object)userInfoList);
        return "oc/statis/balance-users";
    }
}
