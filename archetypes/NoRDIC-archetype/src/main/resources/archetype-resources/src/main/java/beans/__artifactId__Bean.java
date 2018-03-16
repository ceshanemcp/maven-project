#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * ===========================================================================
 *  Licensed Materials - Property of DeVry Education Group
 *
 * (C) Copyright DeVry Education Group 2015 All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 *
 * ===========================================================================
*/
package ${package}.beans;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * @author ${devName}
 *
 */
public class ${artifactId}Bean {
	
	public ${artifactId}Bean() {
		super();
		// Parse string to bean
	}
	
	public ${artifactId}Bean(String xmlString) {
		super();
		// Parse string to bean
	}
	
	/*  TODO:  Replace the bean items with the bean details for your project.
	 *         This is sample data for the get${artifactId}Key() and a sample 
	 *         field to populate it.
	 */
	public String get${artifactId}Key() {
		return null;
	}

	//  Standard overrides for our custom beans
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}