// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.rest;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import javax.validation.Constraint;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ChinaMobileValidator.class })
@Documented
public @interface Mobile {
    String message() default "is not a valid mobile number";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
