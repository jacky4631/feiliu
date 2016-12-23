// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.flow.server.product.UserProduct;
import com.jiam365.flow.oc.utils.EventUtils;
import com.jiam365.flow.base.web.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.product.UserProductManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc/users/product" })
public class UserProductAdminController
{
    @Autowired
    private UserProductManager userProductManager;
    
    @RequestMapping(value = { "discount" }, produces = { "application/json" })
    @ResponseBody
    public WebResponse discount(final String id, final double discount) {
        final UserProduct userProduct = this.userProductManager.updateDiscount(id, discount);
        EventUtils.publishLogEvent("设置" + userProduct.getUsername() + "的" + userProduct.getName() + "折扣为" + discount);
        return WebResponse.success("设置" + userProduct.getName() + "折扣为" + discount);
    }
    
    @RequestMapping(value = { "apply-dis" }, produces = { "application/json" })
    @ResponseBody
    public WebResponse applyDiscount(final String id, final double discount) {
        final UserProduct userProduct = this.userProductManager.applyDiscount2Group(id, discount);
        EventUtils.publishLogEvent("全部设置" + userProduct.getUsername() + "的" + userProduct.getName() + "同组产品折扣为" + discount);
        return WebResponse.success("全部设置折扣为" + discount);
    }
    
    @RequestMapping(value = { "remove-grp" }, produces = { "application/json" })
    @ResponseBody
    public WebResponse removeGrp(final String id) {
        final UserProduct userProduct = this.userProductManager.removeGroup(id);
        EventUtils.publishLogEvent("全部删除" + userProduct.getUsername() + "的" + userProduct.getName() + "同组产品");
        return WebResponse.success("全部删除了" + userProduct.getName() + "同组产品");
    }
    
    @RequestMapping(value = { "remove" }, produces = { "application/json" })
    @ResponseBody
    public WebResponse remove(final String id) {
        final UserProduct userProduct = this.userProductManager.removeProduct(id);
        if (userProduct != null) {
            EventUtils.publishLogEvent("解除用户" + userProduct.getUsername() + "产品授权" + userProduct.getShortName());
        }
        return WebResponse.success("解除产品授权成功");
    }
    
    @RequestMapping(value = { "auth-products" }, produces = { "application/json" })
    @ResponseBody
    public WebResponse authProducts(final String[] ids, final String username, final boolean roamable) {
        for (final String id : ids) {
            this.userProductManager.authProduct2User(id, username, roamable);
        }
        EventUtils.publishLogEvent("授权产品" + Arrays.toString(ids) + "给" + username);
        return WebResponse.success("成功授权" + ids.length + "个产品给" + username + ",请别忘了设置正确的折扣");
    }
    
    @RequestMapping(value = { "data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<UserProduct> listData(final HttpServletRequest request) {
        final PageParamLoader<UserProduct> pageParamLoader = (PageParamLoader<UserProduct>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<UserProduct> page = (Page<UserProduct>)pageParamLoader.parse();
        page = this.userProductManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<UserProduct>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
}
