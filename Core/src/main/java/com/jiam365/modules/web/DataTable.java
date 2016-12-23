package com.jiam365.modules.web;

import com.jiam365.modules.persistent.Page;
import java.util.List;

public class DataTable<T>
{
    private int draw;
    private long recordsTotal;
    private long recordsFiltered;
    private List<T> data;

    public static <T> DataTable<T> createTable(Page<T> page, int draw)
    {
        DataTable<T> dataTable = new DataTable();
        dataTable.setData(page.getResult());
        dataTable.setDraw(draw);
        dataTable.setRecordsTotal(page.getTotalCount());
        dataTable.setRecordsFiltered(page.getTotalCount());
        return dataTable;
    }

    public int getDraw()
    {
        return this.draw;
    }

    public void setDraw(int draw)
    {
        this.draw = draw;
    }

    public List<T> getData()
    {
        return this.data;
    }

    public void setData(List<T> data)
    {
        this.data = data;
    }

    public long getRecordsTotal()
    {
        return this.recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal)
    {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered()
    {
        return this.recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered)
    {
        this.recordsFiltered = recordsFiltered;
    }
}
