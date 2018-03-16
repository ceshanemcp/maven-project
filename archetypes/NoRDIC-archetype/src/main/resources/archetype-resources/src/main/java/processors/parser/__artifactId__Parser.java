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
package ${package}.processors.parser;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.devry.integration.nordic.framework.constants.ParserConstants;
import com.devry.integration.nordic.framework.constants.ProcessConstants;
import com.devry.integration.nordic.framework.exceptions.ParseException;
import com.devry.integration.nordic.framework.message.NoRDICMessage;
import com.devry.integration.nordic.framework.parsers.AbstractParser;
import com.devry.integration.nordic.framework.parsers.ValidationResult;
import ${package}.beans.${artifactId}Bean;
import ${package}.beans.${artifactId}Message;

/**
 * This class is a Specific parser that will handle taking in the message
 * from the TODO: {insert source here} and transforming it to the NoRDICMessage
 * 
 * @author ${devName}
 * 
 */
public class ${artifactId}Parser extends AbstractParser {
	private static final Log logger = LogFactory.getLog(${artifactId}Parser.class);

	@Override
	public NoRDICMessage unmarshallMessage(Object objMessage)
			throws ParseException {
		logger.trace("unmarshallMessage(Object)");

		String xmlString = null;
		
		try {
			xmlString = ((TextMessage) objMessage).getText();
		} catch (JMSException e1) {
			logger.error("Unable to parse message.", e1);
			throw new ParseException(e1.getMessage());
		}

		logger.trace("Mesage received: \n" + xmlString);

		// TODO:  either setup the bean to parse the data, or
		${artifactId}Bean bean = new ${artifactId}Bean(xmlString);
		try {
			//  or parse it to an XMLBean.  User choice.
			//BannerIdentityDocument bean = ${artifactId}Document.Factory.parse(xmlString);
			
			${artifactId}Message message = null;
			message = new ${artifactId}Message(bean);
			message.setInMessage(xmlString);
			logger.trace("Parsed Message: " + xmlString);
			
			return message;
		} catch (Exception e) {
			logger.error("Unmarshall Message Parse Failed", e);
			throw new ParseException(e.getMessage());
		}
	}

	@Override
	public ValidationResult validateMessage(NoRDICMessage noRDICMessage) {
		logger.trace("validateMessage(NoRDICMessage)");
		
		ValidationResult result = new ValidationResult(ParserConstants.DATA_VALIDATION);
		result.setValid(true);
		
		${artifactId}Message message = (${artifactId}Message) noRDICMessage;
		${artifactId}Bean bean = message.get${artifactId}Object();
		
		/*  TODO:  Replace with your logic for determining if this is a valid
		 *         message.
		 */
		/*  if (bean.getINSTC() == null || bean.getINSTC().trim().isEmpty()) {
			logger.warn("Institution Code Is empty, Invalid Message");
			result = populateDataValidationResult(false,
					ProcessConstants.TRANS_STAT_INVALID,
					"Record has no Institution Code");
		} */
		
		return result;
	}

	public ValidationResult populateDataValidationResult(boolean valid,
			String reasonCode, String description) {
		logger.trace("populateDataValidationResult(boolean)");
		
		ValidationResult result = new ValidationResult(ParserConstants.DATA_VALIDATION);
		result.setValid(valid);
		result.setReasonCode(reasonCode);
		result.setDescription(description);
		
		return result;
	}

	@Override
	public ValidationResult filterMessage(NoRDICMessage noRDICMessage) {
		// nothing to filter in this case
		logger.trace("filterMessage(NoRDICMessage)");
		
		ValidationResult result = new ValidationResult(ParserConstants.DATA_VALIDATION);
		result.setValid(true);
		
		return result;
	}
}