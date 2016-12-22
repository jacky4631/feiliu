package com.jiam365.modules.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

public class BeanValidators
{
    public static void validateWithException(Validator validator, Object object, Class<?>... groups)
            throws ConstraintViolationException
    {
        Set constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public static List<String> extractMessage(ConstraintViolationException e)
    {
        return extractMessage(e.getConstraintViolations());
    }

    public static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations)
    {
        List<String> errorMessages = new ArrayList();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.add(violation.getMessage());
        }
        return errorMessages;
    }

    public static Map<String, String> extractPropertyAndMessage(ConstraintViolationException e)
    {
        return extractPropertyAndMessage(e.getConstraintViolations());
    }

    public static Map<String, String> extractPropertyAndMessage(Set<? extends ConstraintViolation> constraintViolations)
    {
        Map<String, String> errorMessages = new HashMap();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errorMessages;
    }

    public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e)
    {
        return extractPropertyAndMessageAsList(e.getConstraintViolations(), " ");
    }

    public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations)
    {
        return extractPropertyAndMessageAsList(constraintViolations, " ");
    }

    public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e, String separator)
    {
        return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
    }

    public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations, String separator)
    {
        List<String> errorMessages = new ArrayList();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.add(violation.getPropertyPath() + separator + violation.getMessage());
        }
        return errorMessages;
    }
}
