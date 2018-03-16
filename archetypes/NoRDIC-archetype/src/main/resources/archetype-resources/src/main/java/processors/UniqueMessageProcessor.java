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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.devry.integration.audit.AuditInterface;
import com.devry.integration.audit.bean.ProcessAuditBean;
import com.devry.integration.audit.bean.TransactionAuditBean;
import com.devry.integration.nordic.framework.constants.ProcessConstants;
import com.devry.integration.nordic.framework.message.NoRDICMessage;
import com.devry.integration.nordic.framework.message.MessageResult.ResultType;
import com.devry.integration.nordic.framework.process.AbstractMessageProcessor;
import com.devry.integration.nordic.framework.process.ProcessResult;

import ${package}.consumer.sp.MessageEnricherSP;
import ${package}.topic.post.NoRDICTopicPublisher;

import ${package}.audit.AuditHelper;
import ${package}.beans.${artifactId}Bean;
import ${package}.beans.${artifactId}Message;

/**
 * 
 * @author Charles Shane
 * @author ${DevName}
 * 
 * @version 1.0 on October 3, 2013
 */
public class UniqueMessageProcessor extends AbstractMessageProcessor {
	private static final Log logger = LogFactory
			.getLog(UniqueMessageProcessor.class);

	private AuditHelper transAudit;
	// TODO:  write it to your output process.  This example uses a Topic Publisher
	private NoRDICTopicPublisher publisher;
	private MessageEnricherSP messageEnricherSP;

	public TransactionAuditBean process(NoRDICMessage message) throws Exception {
		${artifactId}Message outMessage = (${artifactId}Message) message;

		this.enrichData(outMessage);
		
		${artifactId}Bean bean = outMessage.get${artifactId}Object();
		String key = bean.get${artifactId}Key();

		if (logger.isTraceEnabled())
			logger.trace("Processing Unique for Key: <" + key + ">");

		try {
			// Add bean data to XML Bean
			// TODO:  write it to your output process.  This example uses a Topic Publisher
			publisher.postMessage(bean.toString());
		} catch (Exception e) {
			// Audit this message and continue on
			outMessage.populateMessageResult(ProcessConstants.TRANS_STAT_FAIL,
					                            ResultType.FAILED,
					                            "Posting failed" + e.getMessage());
			String errorMsg = "Error processing Unique Message for Key: <"
					+ key + ">";
			logger.error(errorMsg, e);
		}

		TransactionAuditBean ta = transAudit.createTransactionAuditBean(outMessage);
		transAudit.auditTransaction(ta);
	
		return ta;
	}

	public int initialize(ProcessResult processResult) {
		if (logger.isTraceEnabled())
			logger.trace("initialize(ProcessResult)");
		
		transAudit.initializeProcess();
		
		return 1;
	}

	/**
	 * Write out xml files and audit.
	 */
	public void finalize(ProcessResult processResult) {
		AuditInterface audit = getAudit();
		
		try {
			List<NoRDICMessage> msgs = processResult.getInvalidMessages();
			msgs.addAll(processResult.getDuplicateMessages());
		} catch (Exception e) {
			logger.error("Error while writing out.", e);
		}

		if (audit != null && audit.isProcessAuditStarted()
				&& !audit.isProcessAuditStopped()) {
			ProcessAuditBean processAuditBean = new ProcessAuditBean();
			load(processResult, processAuditBean);
			java.util.Date today = new java.util.Date();
			processAuditBean.setRunEnd(new java.sql.Timestamp(today.getTime()));
			audit.stopProcessAudit(processAuditBean);
			audit.setProcessAuditStopped(true);
			audit.setProcessAuditStarted(false);
		}
	}

	private void load(ProcessResult processResult,
			ProcessAuditBean processAuditBean) {
		processAuditBean.setProcessName(processResult.getProcessName());
		processAuditBean.setRunId(processResult.getRunId());
		processAuditBean.setTransProcessed(processResult.getProcessTotal());
		processAuditBean.setTransFailed(processResult.getProcessSendFailed());
		processAuditBean.setTransSuccess(processResult.getProcessSuccessful());
		processAuditBean.setProcessId(processResult.getProcessId());
	}

	private void enrichData(${artifactId}Message message) {
		
		/*  
		 * Here is an example of how you might implement the Enrich data.  This 
		 * however is a non-standard method of enriching the data.  Data enrichment
		 * is usually done in the MessageConsumer.
		 * 
		 */
		
		/*String pidm = message.getDoc().getBannerIdentity().getSourcedid().getId();
		
		try {
			message.setBean(this.getMessageEnricherSP().enrichData(Integer.valueOf(pidm)));
		} catch (NumberFormatException e) {
			// TODO Capture invalid Error.  Integer.ValueOf
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public AuditInterface getAudit() {
		return super.getAudit();
	}

	public void setAudit(AuditInterface audit) {
		super.setAudit(audit);
	}

	/**
	 * @return the transAudit
	 */
	public AuditHelper getTransAudit() {
		return transAudit;
	}

	/**
	 * @param transAudit the transAudit to set
	 */
	public void setTransAudit(AuditHelper transAudit) {
		this.transAudit = transAudit;
	}

	/**
	 * @return the publisher
	 */
	public NoRDICTopicPublisher getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(NoRDICTopicPublisher publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the storedProc
	 */
	public MessageEnricherSP getMessageEnricherSP() {
		return messageEnricherSP;
	}

	/**
	 * @param storedProc the storedProc to set
	 */
	public void setMessageEnricherSP(MessageEnricherSP messageEnricherSP) {
		this.messageEnricherSP = messageEnricherSP;
	}
}