// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.agent.web;

import org.springframework.web.bind.annotation.RequestParam;
import com.jiam365.flow.base.web.WebResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.jiam365.flow.server.product.UserProduct;
import java.util.List;
import com.jiam365.modules.utils.Identities;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.server.entity.User;
import com.jiam365.flow.base.utils.ShiroUtils;
import org.springframework.ui.Model;
import com.jiam365.flow.server.product.UserProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.UserManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/agent" })
public class ProfileController
{
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserProductManager userProductManager;
    
    @RequestMapping(value = { "apis" }, method = { RequestMethod.GET })
    public String authcode(final Model model) {
        final long uid = ShiroUtils.currentUserId();
        final User user = this.userManager.getUser(uid);
        model.addAttribute("username", (Object)user.getUsername());
        model.addAttribute("authcode", (Object)user.getAuthToken());
        return "agent/apis";
    }
    
    @RequestMapping(value = { "apis/clear" }, method = { RequestMethod.POST })
    @ResponseBody
    public void clearAuthcode() {
        final long uid = ShiroUtils.currentUserId();
        final User user = this.userManager.getUser(uid);
        user.setAuthToken("");
        this.userManager.updateUser(user);
    }
    
    @RequestMapping(value = { "apis/reauth" }, method = { RequestMethod.POST })
    @ResponseBody
    public String newAuthcode() {
        final long uid = ShiroUtils.currentUserId();
        final User user = this.userManager.getUser(uid);
        user.setAuthToken(Identities.uuid2());
        this.userManager.updateUser(user);
        return user.getAuthToken();
    }
    
    @RequestMapping(value = { "profile" }, method = { RequestMethod.GET })
    public String prifile(final Model model) {
        final long uid = ShiroUtils.currentUserId();
        model.addAttribute("user", (Object)this.userManager.getUser(uid));
        final List<UserProduct> products = this.userProductManager.findUsersProduct(ShiroUtils.currentUsername());
        model.addAttribute("products", (Object)products);
        return "agent/profile";
    }
    
    @RequestMapping(value = { "profile" }, method = { RequestMethod.POST }, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<?> update(@ModelAttribute("user") final User user) {
        if (user.getId() != null && user.getId().equals(ShiroUtils.currentUserId())) {
            this.userManager.updateUser(user);
            return (ResponseEntity<?>)new ResponseEntity(HttpStatus.OK);
        }
        return (ResponseEntity<?>)new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value = { "passwd" }, method = { RequestMethod.GET })
    public String passwdForm() {
        return "agent/passwd";
    }
    
    @RequestMapping(value = { "passwd" }, method = { RequestMethod.POST }, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<?> passwd(final String oldPassword, final String password, final String password1) {
        if (!StringUtils.equals((CharSequence)password1, (CharSequence)password) || StringUtils.isBlank((CharSequence)password)) {
            return (ResponseEntity<?>)new ResponseEntity((Object)WebResponse.fail("\u65b0\u5bc6\u7801\u8f93\u5165\u4e0d\u6b63\u786e\u6216\u4e0d\u4e00\u81f4"), HttpStatus.OK);
        }
        final long userId = ShiroUtils.currentUserId();
        if (!this.userManager.verifyPassword(userId, oldPassword)) {
            return (ResponseEntity<?>)new ResponseEntity((Object)WebResponse.fail("\u65e7\u5bc6\u7801\u8f93\u5165\u9519\u8bef"), HttpStatus.OK);
        }
        final User user = this.userManager.getUser(userId);
        user.setPlainPassword(password);
        this.userManager.updateUser(user);
        return (ResponseEntity<?>)new ResponseEntity((Object)WebResponse.success("\u5bc6\u7801\u4fee\u6539\u6210\u529f"), HttpStatus.OK);
    }
    
    @ModelAttribute
    public void loadUser(@RequestParam(value = "id", defaultValue = "-1") final Long id, final Model model) {
        if (id != -1L) {
            model.addAttribute("user", (Object)this.userManager.getUser(id));
        }
    }
}
