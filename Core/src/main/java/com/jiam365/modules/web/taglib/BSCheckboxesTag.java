package com.jiam365.modules.web.taglib;

import javax.servlet.jsp.JspException;
import org.springframework.web.servlet.tags.form.TagWriter;

public class BSCheckboxesTag
        extends BSAbstractMultiCheckedElementTag
{
    private static final long serialVersionUID = 4310358928301706621L;

    protected int writeTagContent(TagWriter tagWriter)
            throws JspException
    {
        super.writeTagContent(tagWriter);
        if (!isDisabled())
        {
            tagWriter.startTag("input");
            tagWriter.writeAttribute("type", "hidden");
            String name = "_" + getName();
            tagWriter.writeAttribute("name", name);
            tagWriter.writeAttribute("value", processFieldValue(name, "on", getInputType()));
            tagWriter.endTag();
        }
        return 0;
    }

    protected String getInputType()
    {
        return "checkbox";
    }
}
