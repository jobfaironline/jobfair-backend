package org.capstone.job_fair.validators.impl;

import org.capstone.job_fair.constants.RegexPattern;
import org.capstone.job_fair.validators.PhoneConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;

public class PhoneValidator implements ConstraintValidator<PhoneConstraint, String> {
    @Override
    public void initialize(PhoneConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        if (phone == null) return true;
        Matcher matcher = RegexPattern.VALID_PHONE_REGEX.matcher(phone);
        return matcher.find();
    }
}
