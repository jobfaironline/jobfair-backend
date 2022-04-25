package org.capstone.job_fair.validators.impl;

import org.capstone.job_fair.constants.RegexPattern;
import org.capstone.job_fair.validators.NameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;

public class NameValidator implements ConstraintValidator<NameConstraint, String> {
    @Override
    public void initialize(NameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        Matcher matcher = RegexPattern.NAME_REGEX.matcher(value);
        return !matcher.find();
    }
}
