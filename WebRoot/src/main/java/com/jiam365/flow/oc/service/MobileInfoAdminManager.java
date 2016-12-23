package com.jiam365.flow.oc.service;

import com.jiam365.flow.server.dao.MobileLocationDao;
import com.jiam365.flow.server.dao.StateDao;
import com.jiam365.flow.server.dic.MobileLocation;
import com.jiam365.flow.server.dic.State;
import com.jiam365.modules.persistent.Page;
import com.jiam365.modules.persistent.PageParseMongo;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class MobileInfoAdminManager
{
    @Autowired
    private StateDao stateDao;
    @Autowired
    private MobileLocationDao mobileLocationDao;

    public Page<State> searchPage(Page<State> page)
    {
        PageParseMongo<State> builder = new PageParseMongo(page);
        Query<State> q = buildQuery(builder.searchParams());
        page.setTotalCount(this.stateDao.count(q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.stateDao.find(q).asList());
        return page;
    }

    private Query<State> buildQuery(Map<String, String> filters)
    {
        Query<State> q = this.stateDao.createQuery();
        if (filters.containsKey("EQ_name")) {
            q.field("name").contains((String)filters.get("EQ_name"));
        }
        if (filters.containsKey("EQ_code")) {
            q.field("code").equal(filters.get("EQ_code"));
        }
        return q;
    }

    public void saveLocation(MobileLocation location)
    {
        this.mobileLocationDao.save(location);
    }

    public MobileLocation getLocation(String id)
    {
        return (MobileLocation)this.mobileLocationDao.get(id);
    }

    public void removeLocation(String id)
    {
        this.mobileLocationDao.deleteById(id);
    }

    public Page<MobileLocation> locationSearchPage(Page<MobileLocation> page)
    {
        PageParseMongo<MobileLocation> builder = new PageParseMongo(page);
        Query<MobileLocation> q = locationBuildQuery(builder.searchParams());
        page.setTotalCount(this.mobileLocationDao.count(q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.mobileLocationDao.find(q).asList());
        return page;
    }

    private Query<MobileLocation> locationBuildQuery(Map<String, String> filters)
    {
        Query<MobileLocation> q = this.mobileLocationDao.createQuery();
        if (filters.containsKey("EQ_sectionNo"))
        {
            String sectionNo = (String)filters.get("EQ_sectionNo");
            if (sectionNo.length() >= 7) {
                sectionNo = sectionNo.substring(0, 7);
            }
            sectionNo = StringUtils.remove(sectionNo, "*");
            q.field("sectionNo").startsWith(sectionNo);
        }
        if (filters.containsKey("LIKE_area")) {
            q.field("area").contains((String)filters.get("LIKE_area"));
        }
        return q;
    }
}
