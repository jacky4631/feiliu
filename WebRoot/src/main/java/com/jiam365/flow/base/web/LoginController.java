package com.jiam365.flow.base.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/login"})
public class LoginController
{
    @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.GET})
    public String login()
    {
        return "login";
    }

    @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.POST})
    public String fail(@RequestParam("username") String userName, Model model)
    {
        model.addAttribute("username", userName);
        return "login";
    }
}
