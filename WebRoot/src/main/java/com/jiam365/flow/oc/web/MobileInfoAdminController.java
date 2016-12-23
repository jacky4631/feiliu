// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.oc.web;

import org.springframework.web.bind.annotation.RequestParam;
import com.jiam365.flow.oc.utils.EventUtils;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.jiam365.flow.base.web.WebResponse;
import com.jiam365.flow.server.dic.MobileLocation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiam365.modules.persistent.PageParamLoader;
import com.jiam365.modules.persistent.Page;
import org.apache.commons.lang3.StringUtils;
import com.jiam365.modules.web.DataTablePageLoader;
import com.jiam365.flow.server.dic.State;
import com.jiam365.modules.web.DataTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.oc.service.MobileInfoAdminManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/oc" })
public class MobileInfoAdminController
{
    @Autowired
    private MobileInfoAdminManager mobileInfoAdminManager;
    
    @RequestMapping({ "state" })
    public String list() {
        return "oc/state-list";
    }
    
    @RequestMapping(value = { "state/data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<State> listData(final HttpServletRequest request) {
        final PageParamLoader<State> pageParamLoader = (PageParamLoader<State>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<State> page = (Page<State>)pageParamLoader.parse();
        page = this.mobileInfoAdminManager.searchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<State>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
    
    @RequestMapping({ "location" })
    public String listLocation() {
        return "oc/location-list";
    }
    
    @RequestMapping(value = { "location/update/{id}" }, method = { RequestMethod.GET })
    public String updateForm(@PathVariable("id") final String id, final Model model) {
        model.addAttribute("location", (Object)this.mobileInfoAdminManager.getLocation(id));
        return "oc/location-form";
    }
    
    @RequestMapping(value = { "location/{id}" }, method = { RequestMethod.GET })
    @ResponseBody
    public MobileLocation fetchLocation(@PathVariable("id") final String id) {
        return this.mobileInfoAdminManager.getLocation(id);
    }
    
    @RequestMapping(value = { "location/new" }, method = { RequestMethod.GET })
    public String newLocation() {
        return "oc/location-form";
    }
    
    @RequestMapping(value = { "location/remove" }, method = { RequestMethod.POST })
    @ResponseBody
    public WebResponse remove(final String lid) {
        this.mobileInfoAdminManager.removeLocation(lid);
        return WebResponse.success("删除成功");
    }
    
    @RequestMapping(value = { "location/update" }, method = { RequestMethod.POST })
    @ResponseBody
    public MobileLocation update(@ModelAttribute("location") final MobileLocation location, final HttpServletResponse response) {
        try {
            this.mobileInfoAdminManager.saveLocation(location);
            EventUtils.publishLogEvent("更新地区信息" + location.getSectionNo());
        }
        catch (Exception e) {
            response.setStatus(406);
        }
        return location;
    }
    
    @RequestMapping(value = { "location/data" }, produces = { "application/json" })
    @ResponseBody
    public DataTable<MobileLocation> listData2(final HttpServletRequest request) {
        final PageParamLoader<MobileLocation> pageParamLoader = (PageParamLoader<MobileLocation>)new DataTablePageLoader(request, new String[] { "filter_" });
        Page<MobileLocation> page = (Page<MobileLocation>)pageParamLoader.parse();
        page = this.mobileInfoAdminManager.locationSearchPage(page);
        final String draw = request.getParameter("draw");
        return (DataTable<MobileLocation>)DataTable.createTable((Page)page, StringUtils.isNotBlank((CharSequence)draw) ? Integer.parseInt(draw) : 0);
    }
    
    @ModelAttribute
    public void getLocation(@RequestParam(value = "sectionNo", defaultValue = "") final String sectionNo, final Model model) {
        if (StringUtils.isNotBlank((CharSequence)sectionNo)) {
            final MobileLocation location = this.mobileInfoAdminManager.getLocation(sectionNo);
            if (location != null) {
                model.addAttribute("location", (Object)location);
            }
        }
    }
}
