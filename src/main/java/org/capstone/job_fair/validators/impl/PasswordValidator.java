package org.capstone.job_fair.validators.impl;

import org.capstone.job_fair.utils.regex.RegexConstraint;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {



    @Override
    public void initialize(PasswordConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        Matcher matcher = RegexConstraint.VALID_PASSWORD_REGEX.matcher(password);
        return matcher.find();
    }
}
