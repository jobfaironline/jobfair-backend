package org.capstone.job_fair.validators;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.validators.impl.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneConstraint {
    String message() default MessageConstant.InvalidFormatValidationMessage.INVALID_PHONE_FORMAT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
