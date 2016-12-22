package com.jiam365.modules.utils;

import com.jiam365.modules.tools.ConfigurationHolder;
import org.apache.commons.lang3.StringUtils;

public class Sequences
{
    private static final Integer MAX_POSITION = Integer.valueOf(99999);
    private static final int SEQUENCE_LEN = 5;
    private static Long lastTimeOffset = Long.valueOf(0L);
    private static Integer position = Integer.valueOf(0);
    private static final Long OFFSET = Long.valueOf(1356969600000L);
    private static char clusterId;

    static
    {
        try
        {
            String code = ConfigurationHolder.properties().getProperty("sys.cluster");
            if ((StringUtils.length(code) == 1) && (StringUtils.isNumeric(code))) {
                clusterId = code.charAt(0);
            } else {
                clusterId = '1';
            }
        }
        catch (Throwable ignore)
        {
            clusterId = '1';
        }
    }

    public static synchronized long get()
    {
        StringBuilder sb = new StringBuilder();
        Long curTime = getCurrentTimeOffset();
        sb.append(clusterId).append(curTime).append(getCurPosition(curTime));
        return Long.parseLong(sb.toString());
    }

    private static String getCurPosition(Long timeOffsetSection)
    {
        if (timeOffsetSection.equals(lastTimeOffset))
        {
            if ((position = Integer.valueOf(position.intValue() + 1)).intValue() > MAX_POSITION.intValue()) {
                position = Integer.valueOf(1);
            }
        }
        else
        {
            lastTimeOffset = timeOffsetSection;
            position = Integer.valueOf(1);
        }
        Integer curPos = position;
        return StringUtils.leftPad(curPos.toString(), 5, '0');
    }

    private static Long getCurrentTimeOffset()
    {
        return Long.valueOf((System.currentTimeMillis() - OFFSET.longValue()) / 1000L);
    }
}
