package org.step.linked.step.model;

import org.step.linked.step.model.validators.BannValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
@Constraint(validatedBy = BannValidator.class)
public @interface BannVerifier {
    String message() default "Bann validation is failed";
    Authority[] authorities();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
