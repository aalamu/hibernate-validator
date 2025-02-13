/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.util.classhierarchy;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.internal.util.Contracts;

/**
 * Helper class for dealing with inheritance hierarchies of given types which
 * allows to selectively retrieve elements from such hierarchies, e.g. all
 * super-classes, all implemented interfaces etc.
 *
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 */
public class ClassHierarchyHelper {

	private ClassHierarchyHelper() {
		// Not allowed
	}

	/**
	 * Gets the elements of the hierarchy of the given class which match the
	 * given filters. Classes are added by starting with the class itself and
	 * its implemented interfaces. Then its super class and interfaces are added
	 * and so on.
	 *
	 * @param clazz the class for which to retrieve the hierarchy
	 * @param filters filters applying for the search
	 * @param <T> the type of the class
	 *
	 * @return List of hierarchy classes. Will only contain those types matching
	 *         the given filters. The list contains the given class itself, if
	 *         it is no proxy class.
	 */
	public static <T> List<Class<? super T>> getHierarchy(Class<T> clazz, Filter... filters) {
		Contracts.assertNotNull( clazz );

		List<Class<? super T>> classes = newArrayList();

		List<Filter> allFilters = newArrayList();
		allFilters.addAll( Arrays.asList( filters ) );
		allFilters.add( Filters.excludeProxies() );

		getHierarchy( clazz, classes, allFilters );
		return classes;
	}

	/**
	 * Retrieves all superclasses and interfaces recursively.
	 *
	 * @param clazz the class to start the search with
	 * @param classes list of classes to which to add all found super types matching
	 * the given filters
	 * @param filters filters applying for the search
	 */
	private static <T> void getHierarchy(Class<? super T> clazz, List<Class<? super T>> classes, Iterable<Filter> filters) {
		for ( Class<? super T> current = clazz; current != null; current = current.getSuperclass() ) {
			if ( classes.contains( current ) ) {
				return;
			}

			if ( acceptedByAllFilters( current, filters ) ) {
				classes.add( current );
			}

			for ( Class<?> currentInterface : current.getInterfaces() ) {
				//safe since interfaces are super-types
				@SuppressWarnings("unchecked")
				Class<? super T> currentInterfaceCasted = (Class<? super T>) currentInterface;
				getHierarchy( currentInterfaceCasted, classes, filters );
			}
		}
	}

	private static boolean acceptedByAllFilters(Class<?> clazz, Iterable<Filter> filters) {
		for ( Filter classFilter : filters ) {
			if ( !classFilter.accepts( clazz ) ) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Gets all interfaces (and recursively their super-interfaces) which the
	 * given class directly implements. Interfaces implemented by super-classes
	 * are not contained.
	 *
	 * @param clazz the class for which to retrieve the implemented interfaces
	 * @param <T> the type of the class
	 *
	 * @return Set of all interfaces implemented by the class represented by
	 *         this hierarchy. The empty list is returned if it does not
	 *         implement any interfaces.
	 */
	public static <T> Set<Class<? super T>> getDirectlyImplementedInterfaces(Class<T> clazz) {
		Contracts.assertNotNull( clazz );

		Set<Class<? super T>> classes = newHashSet();
		getImplementedInterfaces( clazz, classes );
		return classes;
	}

	private static <T> void getImplementedInterfaces(Class<? super T> clazz, Set<Class<? super T>> classes) {
		for ( Class<?> currentInterface : clazz.getInterfaces() ) {
			@SuppressWarnings("unchecked") //safe since interfaces are super-types
			Class<? super T> currentInterfaceCasted = (Class<? super T>) currentInterface;
			classes.add( currentInterfaceCasted );
			getImplementedInterfaces( currentInterfaceCasted, classes );
		}
	}
}
