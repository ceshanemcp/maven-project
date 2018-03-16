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
package ${package}.constaints;

/**
 * Class containing constants used for ${artifactId} integration.
 *  
 * @author ${devName}
 * 
 */
public class IntegrationConstants {
	
	public static final int PRECEDENCE = 1;
	
	// Used for transaction description - message
	public static final String TRANS_DESC_POSTED_TO_ESB = "Success: msg was posted to Salesforce.";
	public static final String TRANS_DESC_INVALID_MESSAGE = "Not Processed: Invalid Salesforce message.";
	public static final String TRANS_DESC_POST_FAILED = "Exception: Non-successful post. NoRDICMessage: ";
	public static final String TRANS_DESC_DUPLICATE_MESSAGE = "Not Processed: Duplicate ${artifactId} message.";
}