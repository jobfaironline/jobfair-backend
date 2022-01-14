package org.capstone.job_fair.validators;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.validators.impl.PhoneValidator;
import org.capstone.job_fair.validators.impl.XSSValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = XSSValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XSSConstraint {
    String message() default MessageConstant.InvalidFormat.XSS_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
