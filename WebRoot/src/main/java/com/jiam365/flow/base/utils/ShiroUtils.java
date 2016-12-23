package com.jiam365.flow.base.utils;

import com.jiam365.flow.base.service.account.ShiroDbRealm;
import com.jiam365.flow.base.service.account.ShiroDbRealm.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class ShiroUtils
{
    public static ShiroDbRealm.ShiroUser currentUser()
    {
        return (ShiroDbRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
    }

    private static ShiroDbRealm.ShiroUser curUser()
    {
        ShiroDbRealm.ShiroUser user = currentUser();
        if (user == null) {
            throw new RuntimeException("Can't get the current user");
        }
        return user;
    }

    public static long currentUserId()
    {
        return currentUser().id.longValue();
    }

    public static String currentUsername()
    {
        return currentUser().username;
    }

    public static String currentCompany()
    {
        return currentUser().company;
    }

    public static String currentUserDisplayName()
    {
        return currentUser().getDisplayName();
    }
}
