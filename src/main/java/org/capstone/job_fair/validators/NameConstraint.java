package org.capstone.job_fair.validators;


import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.validators.impl.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NameConstraint {
    String message() default MessageConstant.InvalidFormatValidationMessage.INVALID_NAME_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
