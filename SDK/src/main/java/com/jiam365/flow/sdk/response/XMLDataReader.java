package com.jiam365.flow.sdk.response;

import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLDataReader
        implements DataReader
{
    private static Logger logger = LoggerFactory.getLogger(XMLDataReader.class);
    private Element root;

    public void init(String xmlString)
    {
        try
        {
            Document document = DocumentHelper.parseText(xmlString);
            this.root = document.getRootElement();
        }
        catch (DocumentException e)
        {
            logger.error("Parse XML string error, {} \n {}", e.getMessage(), xmlString);
        }
    }

    public String read(String xPath)
    {
        try
        {
            String[] paths = xPath.split("\\.");
            Node currentNode = this.root;
            String path = xPath;
            if (paths.length > 1)
            {
                for (int i = 0; i < paths.length - 1; i++)
                {
                    List<Node> nodes = currentNode.selectNodes(paths[i]);
                    if (nodes.size() == 0) {
                        return "";
                    }
                    currentNode = (Node)nodes.get(0);
                }
                path = paths[(paths.length - 1)];
            }
            Node resultNode = currentNode.selectSingleNode(path);
            return resultNode == null ? "" : resultNode.getStringValue();
        }
        catch (Exception e)
        {
            logger.error("Read XML node {} error, {}", xPath, e.getMessage());
        }
        return "";
    }

    public void release() {}
}
