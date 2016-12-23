package com.jiam365.flow.sdk;

public final class OrigiProductId
{
    public final String origiProductId;
    public final boolean roamable;

    public OrigiProductId(String modifiedOrigiProductId)
    {
        if (modifiedOrigiProductId.endsWith("$"))
        {
            this.roamable = false;
            this.origiProductId = modifiedOrigiProductId.substring(0, modifiedOrigiProductId.length() - 1);
        }
        else
        {
            this.roamable = true;
            this.origiProductId = modifiedOrigiProductId;
        }
    }
}
