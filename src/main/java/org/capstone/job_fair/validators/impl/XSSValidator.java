package org.capstone.job_fair.validators.impl;

import org.capstone.job_fair.constants.RegexPattern;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;

public class XSSValidator implements ConstraintValidator<XSSConstraint, String> {
    @Override
    public void initialize(XSSConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;
        Matcher matcher = RegexPattern.XSS_TAG_REGEX.matcher(value);
        return !matcher.find();
    }
}
