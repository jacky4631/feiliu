package com.jiam365.modules.persistent;

import org.apache.commons.lang3.StringUtils;

public class SearchFilter
{
    public static final String OR_SEPARATOR = "_OR_";
    private String[] propertyNames;
    private String value;
    public SearchFilter() {}

    public static enum MatchType
    {
        EQ,  LIKE,  GT,  GE,  LT,  LE,  ISNOTNULL,  ISNULL,  GTD,  GED,  LTD,  LED;

        private MatchType() {}
    }

    private MatchType matchType = MatchType.EQ;
    private String valueType;

    public SearchFilter(String filterName, String value)
    {
        String matchTypeCode = StringUtils.substringBefore(filterName, "_");
        try
        {
            this.matchType = ((MatchType)Enum.valueOf(MatchType.class, matchTypeCode));
        }
        catch (RuntimeException e)
        {
            throw new IllegalArgumentException("filter名称没有按照规则编写，无法得到属性比较类型", e);
        }
        String propertyNameStr = StringUtils.substringAfter(filterName, "_");
        this.propertyNames = StringUtils.split(propertyNameStr, "_OR_");

        this.value = value;
    }

    public boolean isMultiProperty()
    {
        return this.propertyNames.length > 1;
    }

    public String getValue()
    {
        return this.value;
    }

    public MatchType getMatchType()
    {
        return this.matchType;
    }

    public String[] getPropertyNames()
    {
        return this.propertyNames;
    }

    public String getValueType()
    {
        return this.valueType;
    }

    public void setValueType(String valueType)
    {
        this.valueType = valueType;
    }

    public String getPropertyName()
    {
        if (this.propertyNames.length > 1) {
            throw new IllegalStateException("There are not only one property.");
        }
        return this.propertyNames[0];
    }
}
