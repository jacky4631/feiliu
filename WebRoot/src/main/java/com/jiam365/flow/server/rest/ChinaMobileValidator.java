// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import java.lang.annotation.Annotation;
import com.jiam365.modules.telco.MobileIndentify;
import org.apache.commons.lang3.StringUtils;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidator;

public class ChinaMobileValidator implements ConstraintValidator<Mobile, String>
{
    public void initialize(final Mobile mobile) {
    }
    
    public boolean isValid(final String s, final ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isEmpty((CharSequence)s) || MobileIndentify.getInstance().isValid(s);
    }
}
