package com.jiam365.modules.utils;

import com.jiam365.modules.tools.ConfigurationHolder;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class StringIdGenerator
{
    private static String datetimePrefix = "";
    private static final Integer MAX_POSITION = Integer.valueOf(999999);
    private static final int SEQUENCE_LEN = 6;
    private static Integer position = Integer.valueOf(0);
    private static char serverCode;

    static
    {
        try
        {
            String code = ConfigurationHolder.properties().getProperty("sys.nodecode");
            if ((StringUtils.length(code) == 1) && (StringUtils.isNumeric(code))) {
                serverCode = code.charAt(0);
            } else {
                serverCode = '1';
            }
        }
        catch (Throwable ignore)
        {
            serverCode = '1';
        }
    }

    public static synchronized String get()
    {
        StringBuilder sb = new StringBuilder();
        String curTime = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");

        sb.append(curTime).append(serverCode).append(getCurPosition(curTime));
        return sb.toString();
    }

    public static String normalSeq(String pre)
    {
        return pre + Sequences.get();
    }

    private static String getCurPosition(String timestamp)
    {
        if (datetimePrefix.equals(timestamp))
        {
            if ((position = Integer.valueOf(position.intValue() + 1)).intValue() > MAX_POSITION.intValue()) {
                throw new RuntimeException("����IDGenerator��������������");
            }
        }
        else
        {
            datetimePrefix = timestamp;
            position = Integer.valueOf(1);
        }
        Integer curPos = position;
        return StringUtils.leftPad(curPos.toString(), 6, '0');
    }
}
