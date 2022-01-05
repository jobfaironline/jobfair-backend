package org.capstone.job_fair.validators.impl;

import org.capstone.job_fair.utils.regex.RegexConstraint;
import org.capstone.job_fair.validators.EmailConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {

    @Override
    public void initialize(EmailConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext ctx) {
        Matcher matcher = RegexConstraint.VALID_EMAIL_REGEX.matcher(email);
        return matcher.find();
    }
}
