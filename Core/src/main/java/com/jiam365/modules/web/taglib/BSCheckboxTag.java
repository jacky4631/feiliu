package com.jiam365.modules.web.taglib;

import javax.servlet.jsp.JspException;
import org.springframework.web.servlet.support.BindStatus;
import org.springframework.web.servlet.tags.form.AbstractSingleCheckedElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

public class BSCheckboxTag
        extends AbstractSingleCheckedElementTag
{
    protected int writeTagContent(TagWriter tagWriter)
            throws JspException
    {
        if (!isDisabled())
        {
            tagWriter.startTag("input");
            tagWriter.writeAttribute("type", "hidden");
            String name = "_" + getName();
            tagWriter.writeAttribute("name", name);
            tagWriter.writeAttribute("value", processFieldValue(name, "on", getInputType()));
            tagWriter.endTag();
        }
        super.writeTagContent(tagWriter);
        return 0;
    }

    protected void writeTagDetails(TagWriter tagWriter)
            throws JspException
    {
        tagWriter.writeAttribute("type", getInputType());

        Object boundValue = getBoundValue();
        Class valueType = getBindStatus().getValueType();
        if ((Boolean.class.equals(valueType)) || (Boolean.TYPE.equals(valueType)))
        {
            if ((boundValue instanceof String)) {
                boundValue = Boolean.valueOf((String)boundValue);
            }
            Boolean booleanValue = boundValue != null ? (Boolean)boundValue : Boolean.FALSE;
            renderFromBoolean(booleanValue, tagWriter);
        }
        else
        {
            Object value = getValue();
            if (value == null) {
                throw new IllegalArgumentException("Attribute 'value' is required when binding to non-boolean values");
            }
            Object resolvedValue = (value instanceof String) ? evaluate("value", value) : value;
            renderFromValue(resolvedValue, tagWriter);
        }
    }

    protected String getInputType()
    {
        return "checkbox";
    }
}
