package org.unisphere.unisphere.group.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AvatarUpdateValidator.class)
public @interface AvatarUpdateValidation {

	String message() default "AvatarUpdateValidation";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
