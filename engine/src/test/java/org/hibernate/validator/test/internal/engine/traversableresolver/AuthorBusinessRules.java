/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.test.internal.engine.traversableresolver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Constraint(validatedBy = { AuthorBusinessRules.AuthorBusinessRulesValidator.class })
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorBusinessRules {
	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class AuthorBusinessRulesValidator implements ConstraintValidator<AuthorBusinessRules, Object> {

		@Override
		public void initialize(AuthorBusinessRules constraintAnnotation) {

		}

		@Override
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			return true;
		}
	}
}
