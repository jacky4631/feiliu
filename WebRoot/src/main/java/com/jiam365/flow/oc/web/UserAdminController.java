// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import com.jiam365.flow.base.web.WebResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiam365.flow.oc.utils.EventUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.server.entity.FundAccount;
import java.util.Iterator;
import com.jiam365.modules.persistent.PageParamLoader;
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import com.jiam365.flow.server.entity.User;
import java.util.ArrayList;
import com.jiam365.modules.mapper.BeanMapper;
import com.jiam365.modules.persistent.Page;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.flow.oc.dto.UserInfo;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import com.jiam365.flow.server.service.MobileService;
import com.jiam365.flow.server.service.FlowPackageManager;
import com.jiam365.flow.server.service.ChannelGroupManager;
import com.jiam365.flow.server.service.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.UserManager;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/users" })
public class UserAdminController
{
    private static Logger logger;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private ChannelGroupManager channelGroupManager;
    @Autowired
    private FlowPackageManager flowPackageManager;
    @Autowired
    private MobileService mobileService;
    
    @RequestMapping({ "agent" })
    public String agentList(final Model model) {
        model.addAttribute("type", (Object)0);
        return "/oc/agent-list";
    }
    
    @RequestMapping({ "list" })
    public String list(final Model model) {
        model.addAttribute("type", (Object)1);
        return "/oc/user-list";
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<UserInfo> listData(final HttpServletRequest request) {
        final PageParamLoader<User> pageParamLoader = (PageParamLoader<User>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<User> page = (Page<User>)pageParamLoader.parse();
        final Page<UserInfo> page2 = (Page<UserInfo>)new Page();
        BeanMapper.copy((Object)page, (Object)page2);
        page = this.userManager.searchPage(page);
        final List<User> users = (List<User>)page.getResult();
        final List<UserInfo> userInfos = new ArrayList<UserInfo>();
        for (final User user : users) {
            final UserInfo userInfo = new UserInfo();
            BeanMapper.copy((Object)user, (Object)userInfo);
            final FundAccount fundAccount = this.userManager.getCredit(user.getId());
            if (fundAccount != null) {
                userInfo.setBalance(fundAccount.getBalance());
                userInfo.setCreditLine(fundAccount.getCreditLine());
            }
            userInfos.add(userInfo);
        }
        page2.setResult((List)userInfos);
        page2.setTotalCount(page.getTotalCount());
        final String draw = request.getParameter("draw");
        return (DataTable<UserInfo>)DataTable.createTable((Page)page2, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
    
    @RequestMapping({ "create" })
    public String createForm(final Model model) {
        return this.doCreate(model, 1);
    }
    
    @RequestMapping({ "create-agent" })
    public String createAgentForm(final Model model) {
        return this.doCreate(model, 0);
    }
    
    private String doCreate(final Model model, final int type) {
        final User user = new User();
        user.setUserType(type);
        model.addAttribute("user", (Object)user);
        model.addAttribute("action", (Object)"create");
        this.prepareUIData(model);
        return "oc/user-form";
    }
    
    private void prepareUIData(final Model model) {
        model.addAttribute("protectedProfile", (Object)this.flowPackageManager.findProtectedProfile());
        model.addAttribute("states", (Object)this.mobileService.findAllStates());
        model.addAttribute("roles", (Object)this.roleManager.findAll());
        model.addAttribute("channelGroups", (Object)this.channelGroupManager.findAll());
    }
    
    @RequestMapping(value = { "create" }, method = { RequestMethod.POST })
    public String create(@Valid final User user, final RedirectAttributes redirectAttributes) {
        try {
            this.userManager.registerUser(user);
            EventUtils.publishLogEvent("注册用户" + user.getDisplayName());
        }
        catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", (Object)(" 保存用户" + user.getUsername() + "失败, " + e.getMessage()));
            if (0 == user.getUserType()) {
                return "redirect:/oc/users/create-agent";
            }
            return "redirect:/oc/users/create";
        }
        redirectAttributes.addFlashAttribute("message", (Object)(" 保存用户" + user.getUsername() + "成功"));
        return "redirect:/oc/users/update/" + user.getId();
    }
    
    @RequestMapping(value = { "update/{id}" }, method = { RequestMethod.GET })
    public String updateForm(@PathVariable("id") final Long id, final Model model) {
        model.addAttribute("user", (Object)this.userManager.getUser(id));
        this.prepareUIData(model);
        model.addAttribute("action", (Object)"update");
        return "oc/user-form";
    }
    
    @RequestMapping(value = { "update" }, method = { RequestMethod.POST })
    public String update(@ModelAttribute("user") final User user, final RedirectAttributes redirectAttributes) {
        try {
            this.userManager.updateUser(user);
            EventUtils.publishLogEvent("更新用户" + user.getDisplayName());
            redirectAttributes.addFlashAttribute("message", (Object)"修改成功");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", (Object)"修改失败");
        }
        return "redirect:/oc/users/update/" + user.getId();
    }
    
    @RequestMapping(value = { "batch-del" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse batchDel(final Long[] ids) {
        int count = 0;
        for (final long id : ids) {
            Label_0095: {
                try {
                    this.userManager.deleteUser(id);
                    EventUtils.publishLogEvent("删除用户ID为" + id + "的用户");
                }
                catch (Exception e) {
                    UserAdminController.logger.warn("删除Id为{}的用户失败, {}", (Object)id, (Object)e.getMessage());
                    break Label_0095;
                }
                ++count;
            }
        }
        if (count == 0) {
            return WebResponse.fail("删除失败, 注意: 管理员账号以及交易记录, 余额不为0的账号不能删除");
        }
        return WebResponse.success("成功删除了" + count + "个账号, 管理员或有交易记录以及余额不为0的账号不能删除");
    }
    
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", defaultValue = "-1") final Long id, final Model model) {
        if (id != -1L) {
            model.addAttribute("user", (Object)this.userManager.getUser(id));
        }
    }
    
    @RequestMapping(value = { "enable" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse enable(final Long[] ids, final String opr) {
        boolean enable = false;
        if ("enable".equals(opr)) {
            enable = true;
        }
        for (final long id : ids) {
            if (id != 1L) {
                EventUtils.publishLogEvent("启用ID为" + id + "的用户");
                this.userManager.enableUser(id, enable);
            }
        }
        return WebResponse.success("操作完成");
    }
    
    @RequestMapping(value = { "passwd" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse passwd(final Long id, final String pass) {
        final User user = this.userManager.getUser(id);
        user.setPlainPassword(pass);
        this.userManager.updateUser(user);
        EventUtils.publishLogEvent("重置用户" + user.getDisplayName() + "的密码");
        return WebResponse.success(user.getDisplayName() + "的密码已重置");
    }
    
    static {
        UserAdminController.logger = LoggerFactory.getLogger((Class)UserAdminController.class);
    }
}
