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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.devry.integration.audit.bean.TransactionAuditBean;
import com.devry.integration.nordic.framework.constants.ProcessConstants;
import com.devry.integration.nordic.framework.message.NoRDICMessage;
import com.devry.integration.nordic.framework.message.MessageResult.ResultType;
import com.devry.integration.nordic.framework.parsers.ValidationResult;
import com.devry.integration.nordic.framework.process.AbstractMessageProcessor;
import com.devry.integration.nordic.framework.process.ProcessResult;
import ${package}.audit.AuditHelper;
import ${package}.beans.${artifactId}Message;

/**
 * Generic Invalid Message class for messages that fall into the invalid state
 * determined in the PreParser
 * 
 * @author Charles E. Shane
 * 
 */
public class InvalidMessageProcessor extends AbstractMessageProcessor {

	private AuditHelper auditHelper;

	private static final Log logger = LogFactory
			.getLog(InvalidMessageProcessor.class);

	public TransactionAuditBean process(NoRDICMessage message) {
		logger.trace("Invalid Message Processor");
		${artifactId}Message noRDICMessage = (${artifactId}Message) message;
		ValidationResult vr = noRDICMessage.getValidationResult();
		String desc = "Message is invalid";
		
		if (vr != null && StringUtils.isNotBlank(vr.getDescription())) {
			desc = vr.getDescription();
		}
		
		noRDICMessage.populateMessageResult(
				ProcessConstants.TRANS_STAT_FAIL, ResultType.INVALID, desc);
		
		return getAuditHelper().createTransactionAuditBean(noRDICMessage);
	}

	public void processMessage(NoRDICMessage message,
			                   ProcessResult processResult) throws Exception {
		logger.trace("processMessage(NoRDICMessage, ProcessResult");
		getAuditHelper().auditTransaction(process(message));
		processResult.addToProcessInvalid();
	}

	public int initialize(ProcessResult processResult) {
		return 1;
	}

	public void finalize(ProcessResult processResult) {
		// NOT USED
	}

	public TransactionAuditBean processMessage(NoRDICMessage message) {
		return process(message);
	}

	public AuditHelper getAuditHelper() {
		return auditHelper;
	}

	public void setAuditHelper(AuditHelper auditHelper) {
		this.auditHelper = auditHelper;
	}
}