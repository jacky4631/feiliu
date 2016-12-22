package com.jiam365.modules.telco;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class MobileIndentify
{
    private Map<String, String> regexMap = new HashMap();

    private MobileIndentify()
    {
        initRegexMapDefault();
    }

    public MobileIndentify(Map<String, String> regexMap)
    {
        setRegexMap(regexMap);
    }

    public void setRegexMap(Map<String, String> regexMap)
    {
        this.regexMap = regexMap;
    }

    public Telco indentifyMobile(String mobile)
    {
        if (mobile == null) {
            return null;
        }
        Set<String> key = this.regexMap.keySet();
        for (String s : key) {
            if (mobile.matches((String)this.regexMap.get(s))) {
                return Telco.valueOf(s);
            }
        }
        return null;
    }

    public String getTeleProviderName(String providerCode)
    {
        Telco provider = null;
        try
        {
            provider = Telco.valueOf(providerCode);
        }
        catch (Exception localException) {}
        return provider == null ? "" : provider.getName();
    }

    public boolean isValid(String mobile)
    {
        return indentifyMobile(mobile) != null;
    }

    public boolean isNotValid(String mobile)
    {
        return indentifyMobile(mobile) == null;
    }

    public Map<String, Integer> indentifyMobiles(String[] phones)
    {
        Integer cmccCount = Integer.valueOf(0);Integer unicomCount = Integer.valueOf(0);Integer telecomCount = Integer.valueOf(0);Integer unknowCount = Integer.valueOf(0);
        Integer localInteger1;
        for (String phone : phones)
        {
            Telco provider = indentifyMobile(phone);
            Integer localInteger2;
            if (provider == null)
            {
                localInteger1 = unknowCount;localInteger2 = unknowCount = Integer.valueOf(unknowCount.intValue() + 1);
            }
            else
            {
                switch (provider)
                {
                    case CMCC:
                        localInteger1 = cmccCount;localInteger2 = cmccCount = Integer.valueOf(cmccCount.intValue() + 1);
                        break;
                    case UNICOM:
                        localInteger1 = unicomCount;localInteger2 = unicomCount = Integer.valueOf(unicomCount.intValue() + 1);
                        break;
                    case TELECOM:
                        localInteger1 = telecomCount;localInteger2 = telecomCount = Integer.valueOf(telecomCount.intValue() + 1);
                }
            }
        }
        Object retMap = new HashMap();
        ((Map)retMap).put(Telco.CMCC.getCode(), cmccCount);
        ((Map)retMap).put(Telco.UNICOM.getCode(), unicomCount);
        ((Map)retMap).put(Telco.TELECOM.getCode(), telecomCount);
        ((Map)retMap).put("UNKNOW", unknowCount);
        return (Map<String, Integer>)retMap;
    }

    private void initRegexMapDefault()
    {
        Telco[] providers = Telco.values();
        for (Telco provider : providers) {
            this.regexMap.put(provider.getCode(), provider.getRegex());
        }
    }

    private static MobileIndentify parser = new MobileIndentify();

    public static MobileIndentify getInstance()
    {
        return parser;
    }
}
