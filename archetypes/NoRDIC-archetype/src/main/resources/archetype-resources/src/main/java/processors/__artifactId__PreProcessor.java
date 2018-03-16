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
package ${package}.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.devry.integration.nordic.framework.exceptions.ParseException;
import com.devry.integration.nordic.framework.message.NoRDICMessage;
import com.devry.integration.nordic.framework.message.UnparsableNoRDICMessage;
import com.devry.integration.nordic.framework.parsers.AbstractParser;
import com.devry.integration.nordic.framework.parsers.ValidationResult;
import com.devry.integration.nordic.framework.process.PreProcessor;
import com.devry.integration.nordic.framework.process.ProcessResult;

/**
 * To pre-process the records retrieved from Banner Database to send to SFDC.
 * 
 * @author ${devName}
 * 
 * @version 1.0 created on October 3, 2013
 */
public class ${artifactId}PreProcessor extends PreProcessor {
	private static final Log logger = LogFactory.getLog(${artifactId}PreProcessor.class);
	private AbstractParser parser;

	@Override
	public ProcessResult processMessages(List<Object> messages)
			throws Exception {
		if (logger.isTraceEnabled())
			logger.trace("Entering processMessages()");
		
		ProcessResult processResult = new ProcessResult();
		parser.resetParser();
		
		List<NoRDICMessage> tempList = new ArrayList<NoRDICMessage>();

		for (Object message : messages) {
			NoRDICMessage noRDICMessage = null;
			
			try {
				noRDICMessage = parser.unmarshallMessage(message);
			} catch (ParseException pe) {
				ValidationResult vr = new ValidationResult(ValidationResult.S_PARSE_TYPE);
				vr.setDescription(pe.getMessage());
				vr.setReasonCode("Could not parse the message");
				vr.setValid(false);
				
				noRDICMessage = new UnparsableNoRDICMessage(message);
				noRDICMessage.setValidationResult(vr);
				processResult.addToUnParsableMessages(noRDICMessage);
				
				continue;
			}

			/* The implementing parsers validation method is being called. */
			ValidationResult result = parser.validateMessage(noRDICMessage);
			
			if (result.isValid()) {
				/*
				 * Filter out any records that this integration doesn't want to
				 * deal with.
				 */
				ValidationResult filterResult = parser.filterMessage(noRDICMessage);
				
				if (filterResult.isValid()) {
					tempList.add(noRDICMessage);
				} else {
					parser.getDuplicateMessages().add(noRDICMessage);
					processResult.addToDuplicateMessages(noRDICMessage);
				}
			} else {
				processResult.addToInvalidMessages(noRDICMessage, result);
			}
		}

		Collections.sort(tempList);
		
		for (int i = 0; i < tempList.size(); i++)
			//Filter out duplicates
			parser.addFilterMessageBean(tempList.get(i));

		processResult.setUniqueMessages(parser.getUniqueMessages());
		processResult.setDuplicateMessages(parser.getDuplicateMessages());
		
		return processResult;
	}

	/**
	 * @return the parser
	 */
	public AbstractParser getParser() {
		return parser;
	}

	/**
	 * @param parser
	 *            the parser to set
	 */
	public void setParser(AbstractParser parser) {
		this.parser = parser;
	}
}