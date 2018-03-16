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
package ${package}.audit.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import com.devry.integration.audit.AuditInterface;
import com.devry.integration.audit.bean.ProcessAuditBean;
import com.devry.integration.audit.bean.TransactionAuditBean;
import com.devry.integration.nordic.framework.message.MessageResult.ResultType;
import ${package}.audit.AuditHelper;
import ${package}.beans.${artifactId}Message;

/**
 * Handles the Audit Process
 * 
 * @author Charles Shane
 * 
 * @version 2.0 on Jun 2, 2014
 */
public class AuditHelperImpl implements AuditHelper {

	private AuditInterface audit;
	private String processName;
	private int numberOfFailures;
	private int numberOfSuccess;
	private int runId;
	private static final Log logger = LogFactory.getLog(AuditHelperImpl.class);
	
	private void logTrace(String message) {
		if (logger.isTraceEnabled()) {
			logger.trace(message);
		}
	}
	
	public AuditHelperImpl() {
		super();
	}

	public void setNumberOfFailures(int numberOfFailures) {
		this.numberOfFailures = numberOfFailures;
	}

	public void setNumberOfSuccess(int numberOfSuccess) {
		this.numberOfSuccess = numberOfSuccess;
	}

	public synchronized TransactionAuditBean createTransactionAuditBean(
			${artifactId}Message message) {
		logTrace("createTransactionAuditBean(${artifactId}NoRDICMessage)");
		TransactionAuditBean auditBean = new TransactionAuditBean();
		java.util.Date today = new java.util.Date();

		auditBean.setAction("CREATE");
		auditBean.setDSI("");
		auditBean.setProcessName(getProcessName());
		auditBean.setExtractTime(new java.sql.Timestamp(today.getTime()));
		auditBean.setExecutionTime(new java.sql.Timestamp(today.getTime()));
		auditBean.setStatus(message.getMessageResult().getResult()
				.toString());
		auditBean.setStatusDetail(message.getMessageResult()
				.getDescription());
		auditBean.setInMessage(message.getInMessage());
		auditBean.setInMessageId(message.getInMessageID());
		auditBean.setOutMessage(message.getOutMessage());
		auditBean.setOutMessageId(message.getOutMessageID());
		auditBean.setRunId(runId);

		return auditBean;
	}

	public synchronized void auditTransaction(TransactionAuditBean auditBean) {
		logTrace("auditTransaction(TransactionAuditBean)");

		getAudit().auditTransaction(auditBean);
		if (auditBean.getStatus().equals(ResultType.SUCCESS.toString())
				|| auditBean.getStatus().equals(
						ResultType.DUPLICATE.toString())) {
			addToSuccess();
		} else
			addToFailure();
	}

	public synchronized void addToSuccess() {
		logTrace("addToSuccess()");
		numberOfSuccess++;
	}

	public synchronized void addToFailure() {
		logTrace("addToFailure()");
		numberOfFailures++;
	}

	public synchronized void finalize() throws DataAccessException {
		logTrace("finalize()");
		
		if (audit != null && audit.isProcessAuditStarted()
				&& !audit.isProcessAuditStopped()) {
			ProcessAuditBean processAuditBean = new ProcessAuditBean();
			processAuditBean.setTransSuccess(1);
			java.util.Date today = new java.util.Date();
			processAuditBean.setRunEnd(new java.sql.Timestamp(today.getTime()));
			processAuditBean.setRunId(runId);
			processAuditBean.setProcessName(processName);
			processAuditBean.setTransProcessed(numberOfSuccess
					+ numberOfFailures);
			processAuditBean.setTransFailed(numberOfFailures);
			processAuditBean.setTransSuccess(numberOfSuccess);
			getAudit().stopProcessAudit(processAuditBean);
			getAudit().setProcessAuditStopped(true);
			getAudit().setProcessAuditStarted(false);
		}
	}

	public String generateMessage(${artifactId}Message message) {
		// Not Required
		return null;
	}

	public void initializeProcess() throws DataAccessException {
		logTrace("initializeProcess()");
		int result = 0;
		numberOfFailures = 0;
		numberOfSuccess = 0;

		if (getAudit() != null && !getAudit().isProcessAuditStarted()) {
			ProcessAuditBean processAuditBean = new ProcessAuditBean();
			processAuditBean.setProcessName(processName);
			java.util.Date today = new java.util.Date();
			processAuditBean.setRunStart(new java.sql.Timestamp(today.getTime()));
			processAuditBean.setSourceSync(new Timestamp(new Date().getTime()));
			result = this.startDBAudit(processAuditBean);

			if (result == -1) {
				for (int i = 0; i < 4; i++) {
					result = this.startDBAudit(processAuditBean);

					if (result != -1)
						break;
				}
			}

			getAudit().setProcessAuditStarted(true);
			getAudit().setProcessAuditStopped(false);

			runId = result;
		}
	}

	private int startDBAudit(ProcessAuditBean pab) {
		int result = 0;
		try {
			result = getAudit().startProcessAudit(pab);
		} catch (Exception e) {
			CharSequence isConstraint = "INT_UTILITY.PROCESS_AUDIT_K2";

			// This error can occur if more than one instance of the job
			// is being executed. Need to try again.
			if (e.getMessage() != null && e.getMessage().contains(isConstraint)) {
				result = -1;
				logger.warn("Duplicate Audit Run ID encountered.  Sleeping 3 seconds then trying again.");
				
				try {
					Thread.sleep(2600);
				} catch (InterruptedException e1) {
					//  No sleep till Brooklyn...
				}
			} else {
				result = -2;
				logger.warn("Unknown DB error when starting audit process.");
				logger.warn("Error: ", e);
			}

		}
		
		return result;
	}
	public AuditInterface getAudit() {
		return audit;
	}

	public void setAudit(AuditInterface audit) {
		this.audit = audit;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public int getNumberOfFailures() {
		return numberOfFailures;
	}

	public int getNumberOfSuccess() {
		return numberOfSuccess;
	}

	public int getRunId() {
		return runId;
	}

	public void setRunId(int runId) {
		this.runId = runId;
	}

	public void auditTransaction(int runId, TransactionAuditBean auditBean) {

	}
}