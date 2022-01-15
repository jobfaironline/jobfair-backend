package org.capstone.job_fair.validators;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.validators.impl.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
    String message() default MessageConstant.InvalidFormatValidationMessage.INVALID_PASSWORD_FORMAT;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
