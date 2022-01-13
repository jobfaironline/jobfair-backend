package org.capstone.job_fair.validators.impl;

import org.capstone.job_fair.constants.RegexPattern;
import org.capstone.job_fair.validators.EmailConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {

    @Override
    public void initialize(EmailConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext ctx) {
        if (email == null) return true;
        Matcher matcher = RegexPattern.VALID_EMAIL_REGEX.matcher(email);
        return matcher.find();
    }
}
