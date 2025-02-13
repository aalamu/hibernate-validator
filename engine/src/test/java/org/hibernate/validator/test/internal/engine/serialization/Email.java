/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.test.internal.engine.serialization;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;

/**
 * Denotes that a field should contain a valid email address.
 * <p/>
 * <p/>
 * Taken from <a href="http://blog.jteam.nl/2009/08/04/bean-validation-integrating-jsr-303-with-spring/"
 * >this blog</a>.
 *
 * @author Hardy Ferentschik
 */
@Documented
@Constraint(validatedBy = { DummyEmailValidator.class })
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = ".+@.+\\.[a-z]+")
@ReportAsSingleViolation
public @interface Email {

	String message() default "{org.hibernate.validator.internal.engine.serialization.Email.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
