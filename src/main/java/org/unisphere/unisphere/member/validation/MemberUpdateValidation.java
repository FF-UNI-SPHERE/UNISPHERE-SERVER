package org.unisphere.unisphere.member.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MemberUpdateValidator.class)
public @interface MemberUpdateValidation {

	String message() default "MemberUpdateValidation";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}