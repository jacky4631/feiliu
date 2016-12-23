package com.jiam365.flow.oc.service;

import com.jiam365.flow.server.channel.FlowChannel;
import com.jiam365.flow.server.dao.FlowChannelDao;
import com.jiam365.flow.server.service.FlowPackageManager;
import com.jiam365.flow.server.service.TradeLogManager;
import com.jiam365.modules.persistent.Page;
import com.jiam365.modules.persistent.PageParseMongo;
import java.util.List;
import java.util.Map;
import org.mongodb.morphia.query.FieldEnd;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;

public class ChannelAdminManager
{
    @Autowired
    private FlowPackageManager flowPackageManager;
    @Autowired
    private FlowChannelDao flowChannelDao;
    @Autowired
    private TradeLogManager tradeLogManager;

    public void removeChannel(long id)
    {
        if (this.tradeLogManager.getChannelTradeLogCount(id) > 0L) {
            throw new RuntimeException("无法删除存在交易记录的供应商" + id);
        }
        this.flowPackageManager.removeByChannelId(id);
        this.flowChannelDao.deleteById(Long.valueOf(id));
    }

    public FlowChannel get(long id)
    {
        return (FlowChannel)this.flowChannelDao.get(Long.valueOf(id));
    }

    public void createNew(FlowChannel channel)
    {
        this.flowChannelDao.insertNew(channel);
    }

    public void save(FlowChannel channel)
    {
        this.flowChannelDao.save(channel);
    }

    public Page<FlowChannel> searchPage(Page<FlowChannel> page)
    {
        PageParseMongo<FlowChannel> builder = new PageParseMongo(page);
        Query<FlowChannel> q = buildQuery(builder.searchParams());
        page.setTotalCount(this.flowChannelDao.count(q));
        q.order(builder.sortParams());
        q.offset((int)page.getFirst());
        q.limit((int)page.getPageSize());
        page.setResult(this.flowChannelDao.find(q).asList());
        return page;
    }

    public List<FlowChannel> findAll()
    {
        return this.flowChannelDao.find().asList();
    }

    public List<FlowChannel> findEnabledChannels()
    {
        return this.flowChannelDao.findEnabledChannels();
    }

    public List<FlowChannel> findChannelsByHandlerClass(String className)
    {
        return this.flowChannelDao.findChannelsByHandlerClass(className);
    }

    private Query<FlowChannel> buildQuery(Map<String, String> filters)
    {
        Query<FlowChannel> q = this.flowChannelDao.createQuery();
        if (filters.containsKey("EQ_id")) {
            q.field("id").equal(Long.valueOf(Long.parseLong((String)filters.get("EQ_id"))));
        }
        if (filters.containsKey("EQ_orgcode")) {
            q.field("orgcode").equal(filters.get("EQ_orgcode"));
        }
        if (filters.containsKey("LIKE_name")) {
            q.field("name").containsIgnoreCase((String)filters.get("LIKE_name"));
        }
        if (filters.containsKey("EQ_status")) {
            q.field("status").equal(Integer.valueOf((String)filters.get("EQ_status")));
        }
        return q;
    }
}
