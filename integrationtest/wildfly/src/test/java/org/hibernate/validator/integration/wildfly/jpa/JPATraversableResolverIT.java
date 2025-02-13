/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.integration.wildfly.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.TraversableResolver;

import org.hibernate.validator.integration.AbstractArquillianIT;
import org.hibernate.validator.internal.engine.resolver.JPATraversableResolver;
import org.hibernate.validator.internal.engine.resolver.TraversableResolvers;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.persistence20.PersistenceDescriptor;

import org.testng.annotations.Test;

/**
 * Tests that the default {@link TraversableResolver} for a Jakarta Persistence environment is {@code JPATraversableResolver}.
 *
 * @author Guillaume Smet
 */
public class JPATraversableResolverIT extends AbstractArquillianIT {

	private static final String WAR_FILE_NAME = JPATraversableResolverIT.class.getSimpleName() + ".war";

	@Deployment
	public static Archive<?> createTestArchive() {
		return buildTestArchive( WAR_FILE_NAME )
				.addAsResource( persistenceXml(), "META-INF/persistence.xml" )
				.addAsWebInfResource( BEANS_XML, "beans.xml" );
	}

	private static Asset persistenceXml() {
		String persistenceXml = Descriptors.create( PersistenceDescriptor.class )
				.version( "2.0" )
				.createPersistenceUnit()
				.name( "default" )
				.jtaDataSource( "java:jboss/datasources/ExampleDS" )
				.up()
				.exportAsString();
		return new StringAsset( persistenceXml );
	}

	@Test
	public void testDefaultTraversableResolverInJPAEnvironmentIsJPATraversableResolver() throws Exception {
		assertThat( TraversableResolvers.getDefault() ).isInstanceOf( JPATraversableResolver.class );
	}
}
