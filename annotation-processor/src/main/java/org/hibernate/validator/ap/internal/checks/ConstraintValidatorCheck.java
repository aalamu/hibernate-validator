/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.ap.internal.checks;

import java.util.Collections;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;

import org.hibernate.validator.ap.internal.util.AnnotationApiHelper;
import org.hibernate.validator.ap.internal.util.CollectionHelper;
import org.hibernate.validator.ap.internal.util.ConstraintHelper;
import org.hibernate.validator.ap.internal.util.TypeNames.BeanValidationTypes;

/**
 * Checks, that for each constraint annotation type, which is not a composed constraint,
 * a validator implementation is specified using the {@link jakarta.validation.Constraint} annotation.
 *
 * @author Gunnar Morling
 */
public class ConstraintValidatorCheck extends AbstractConstraintCheck {

	private ConstraintHelper constraintHelper;

	private final AnnotationApiHelper annotationApiHelper;

	public ConstraintValidatorCheck(ConstraintHelper constraintHelper, AnnotationApiHelper annotationApiHelper) {

		this.constraintHelper = constraintHelper;
		this.annotationApiHelper = annotationApiHelper;
	}

	@Override
	public Set<ConstraintCheckIssue> checkAnnotationType(TypeElement element, AnnotationMirror annotation) {

		AnnotationMirror constraintMirror = annotationApiHelper.getMirror(
				element.getAnnotationMirrors(), BeanValidationTypes.CONSTRAINT
		);
		boolean atLeastOneValidatorGiven = !annotationApiHelper.getAnnotationArrayValue(
				constraintMirror, "validatedBy"
		).isEmpty();

		if ( !( atLeastOneValidatorGiven || constraintHelper.isComposedConstraint( element ) ) ) {

			return CollectionHelper.asSet(
					ConstraintCheckIssue.warning(
							element,
							constraintMirror,
							"CONSTRAINT_TYPE_WITHOUT_VALIDATOR"
					)
			);
		}

		return Collections.emptySet();
	}

}
