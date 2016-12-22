package com.jiam365.modules.json;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FullJsonDateSerializer
        extends JsonSerializer<Date>
{
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
            throws IOException
    {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String output = date != null ? sd.format(date) : "";
        gen.writeString(output);
    }
}
