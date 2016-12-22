package com.jiam365.modules.telco;

public enum Telco
{
    CMCC("CMCC", "移动", "1(((3[4-9]|5[012789]|47|8[23478]|78)\\d{8})|(705\\d{7}))"),  UNICOM("UNICOM", "联通", "1(((3[0-2]|5[56]|45|7[56]|8[56])\\d{8})|(709\\d{7}))"),  TELECOM("TELECOM", "电信", "1(((33|53|7[37]|8[019])\\d{8})|(70[012]\\d{7}))");

    private String name;
    private String code;
    private String regex;

    private Telco(String code, String name, String regex)
    {
        this.code = code;
        this.name = name;
        this.regex = regex;
    }

    public String getName()
    {
        return this.name;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getRegex()
    {
        return this.regex;
    }
}
