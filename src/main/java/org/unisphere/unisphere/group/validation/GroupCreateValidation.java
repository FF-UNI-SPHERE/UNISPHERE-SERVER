package org.unisphere.unisphere.group.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GroupCreateValidator.class)
public @interface GroupCreateValidation {

	String message() default "GroupCreateValidation";

	Class<?>[] groups() default {};

	Class<?>[] payload() default {};
}
