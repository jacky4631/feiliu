package com.jiam365.flow.sdk.utils;

import com.jiam365.modules.telco.Telco;
import org.apache.commons.lang3.StringUtils;

public class ProductIDHelper
{
    public static final String F_PROVIDER_TELECOM = "7";
    public static final String F_PROVIDER_CMCC = "8";
    public static final String F_PROVIDER_UNICOM = "9";
    public static final String NATION_CODE = "NA";
    public static final String F_NOT_ROAMABLE = "$";
    public static final int TEST_ENV_PACKAGE_SIZE = 12;

    public static String productId(Telco provider, String scope, int size)
    {
        return scope + code(provider) + StringUtils.leftPad(String.valueOf(size), 5, '0');
    }

    public static String baseProductId(String productId)
    {
        if (StringUtils.isBlank(productId)) {
            throw new RuntimeException("Invalid productId: " + productId);
        }
        if (productId.endsWith("$")) {
            return StringUtils.substringBefore(productId, "$");
        }
        return productId;
    }

    public static String code(Telco provider)
    {
        switch (provider)
        {
            case TELECOM:
                return "7";
            case CMCC:
                return "8";
            case UNICOM:
                return "9";
        }
        return null;
    }

    public static String code(String productId)
    {
        return productId.substring(2, 3);
    }

    public static Telco telco(String productId)
    {
        String telcoId = code(productId);
        switch (telcoId)
        {
            case "7":
                return Telco.TELECOM;
            case "8":
                return Telco.CMCC;
            case "9":
                return Telco.UNICOM;
        }
        return null;
    }

    public static String stateCode(String productId)
    {
        return productId.substring(0, 2);
    }

    public static String productGroup(String productId)
    {
        if (productId.endsWith("$")) {
            return productId.substring(0, 3) + "$";
        }
        return productId.substring(0, 3);
    }

    public static String groupProfileId(long channelId, String state, Boolean roamable, String telco)
    {
        Telco tel = Telco.valueOf(telco);
        if (tel == null) {
            throw new IllegalArgumentException(telco + " is not a valid Teclo");
        }
        String roamableFlag = roamable.booleanValue() ? "" : "$";
        return channelId + "-" + state + code(tel) + roamableFlag;
    }

    public static String telcoFlag2Name(String flag)
    {
        switch (flag)
        {
            case "7":
                return "电信";
            case "8":
                return "移动";
            case "9":
                return "联通";
        }
        return "";
    }

    public static String replacementProductId(String productId, String stateCode)
    {
        if ((isNationProduct(productId)) && (StringUtils.isNotBlank(stateCode))) {
            return stateCode + productId.substring(2);
        }
        return productId;
    }

    public static boolean isNationProduct(String productId)
    {
        return (productId != null) && (productId.startsWith("NA"));
    }
}
