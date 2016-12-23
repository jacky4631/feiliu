package com.jiam365.flow.base.web;

import com.jiam365.modules.telco.Telco;
import java.beans.PropertyEditorSupport;

public class TelcoConverter
        extends PropertyEditorSupport
{
    public void setAsText(String text)
            throws IllegalArgumentException
    {
        setValue(Telco.valueOf(text.trim()));
    }
}
