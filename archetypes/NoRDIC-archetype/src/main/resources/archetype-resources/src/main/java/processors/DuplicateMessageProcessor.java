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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.devry.integration.audit.bean.TransactionAuditBean;
import com.devry.integration.nordic.framework.constants.ProcessConstants;
import com.devry.integration.nordic.framework.message.NoRDICMessage;
import com.devry.integration.nordic.framework.message.MessageResult.ResultType;
import com.devry.integration.nordic.framework.process.AbstractMessageProcessor;
import com.devry.integration.nordic.framework.process.ProcessResult;
import ${package}.audit.AuditHelper;
import ${package}.beans.${artifactId}Message;

/**
 * Generic Duplicate Message class. Handles duplicate messages based on the
 * NoRDICMessage .compareTo method
 * 
 * @author Charles E. Shane
 * @author ${devName}
 * 
 */
public class DuplicateMessageProcessor extends AbstractMessageProcessor {

	public AuditHelper auditHelper;

	private static final Log logger = LogFactory.getLog(DuplicateMessageProcessor.class);

	public TransactionAuditBean process(NoRDICMessage message) {
		if (logger.isTraceEnabled())
			logger.trace("Duplicate Message Process");
		
		${artifactId}Message noRDICMessage = (${artifactId}Message) message;

		noRDICMessage.populateMessageResult(
				ProcessConstants.TRANS_STAT_DUPLICATE, ResultType.DUPLICATE,
				"Duplicate Message");
		
		return getAuditHelper().createTransactionAuditBean(noRDICMessage);
	}

	public void processMessage(NoRDICMessage message,
			ProcessResult processResult) throws Exception {
		if (logger.isTraceEnabled())
			logger.trace("processMessage(NoRDICMessage, ProcessResult");
		
		getAuditHelper().auditTransaction(process(message));
		processResult.addToProcessDuplicate();
	}

	public int initialize(ProcessResult processResult) {
		return getAuditHelper().getRunId();
	}

	public void finalize(ProcessResult processResult) {
		getAuditHelper().finalize();
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