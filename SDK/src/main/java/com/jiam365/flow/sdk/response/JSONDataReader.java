package com.jiam365.flow.sdk.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONDataReader
        implements DataReader
{
    private static Logger logger = LoggerFactory.getLogger(JSONDataReader.class);
    private JsonNode rootNode;

    public String read(String xPath)
    {
        try
        {
            String[] paths = xPath.split("\\.");
            JsonNode node = this.rootNode;
            for (String path : paths) {
                node = node.path(path);
            }
            return node.asText();
        }
        catch (Exception e)
        {
            logger.error("JSON read {} error, {}", xPath, e.getMessage());
        }
        return "";
    }

    public void init(String packet)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            this.rootNode = mapper.readTree(packet);
        }
        catch (IOException e)
        {
            logger.error("JSON data ({}) parse error {}", packet, e.getMessage());
        }
    }

    public void release() {}
}
