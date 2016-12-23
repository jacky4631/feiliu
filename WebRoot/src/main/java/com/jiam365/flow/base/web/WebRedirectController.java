package com.jiam365.flow.base.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebRedirectController
{
    @RequestMapping({"/"})
    public String index()
    {
        Subject currentUser = SecurityUtils.getSubject();
        if ((currentUser.hasRole("admin")) ||
                (currentUser.hasRole("financial")) ||
                (currentUser.hasRole("operator"))) {
            return "redirect:/oc";
        }
        if (currentUser.hasRole("user")) {
            return "redirect:/agent";
        }
        return "/error/401";
    }
}
