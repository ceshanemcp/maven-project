/*
 * ===========================================================================
 *  Licensed Materials - Property of DeVry, Inc.
 *
 * (C) Copyright DeVry, Inc 2014 All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 *
 * ===========================================================================
 */
package ${package}.audit.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.devry.integration.audit.AuditInterface;
import com.devry.integration.audit.bean.ProcessAuditBean;
import com.devry.integration.audit.bean.TransactionAuditBean;
import ${package}.audit.AuditHelper;
import ${package}.bean.${artifactId}Bean;

/**
 * Audit helper class that will handle the processing of the audit for the
 * process
 * 
 * @author Robert Williams
 * 
 * @version 2.0 on Jan 20, 2013
 */
public class AuditHelperImpl implements AuditHelper {

	private AuditInterface audit;
	private int runId;
	private String processName;
	private int numberOfFailures;
	private int numberOfSuccess;
	
	private static final Logger logger = Logger.getLogger(AuditHelperImpl.class);

	public void setNumberOfFailures(int numberOfFailures) {
		this.numberOfFailures = numberOfFailures;
	}

	public void setNumberOfSuccess(int numberOfSuccess) {
		this.numberOfSuccess = numberOfSuccess;
	}

	public synchronized TransactionAuditBean createTransactionAuditBean(
			${artifactId}Bean bean) {
		logger.trace("Create TransactionAuditBean");
		
		TransactionAuditBean toAuditBean = new TransactionAuditBean();

		// TODO:  Make the audit data from bean you are creating from  ${artifactId}Bean
		java.util.Date today = new java.util.Date();
		toAuditBean.setAction("CREATE");
		//toAuditBean.setDSI(bean.getEmployeeDSI());
		toAuditBean.setProcessName(getProcessName());
		toAuditBean.setExtractTime(new java.sql.Timestamp(today.getTime()));
		toAuditBean.setExecutionTime(new java.sql.Timestamp(today.getTime()));
		//toAuditBean.setStatus(bean.getStatus());
		//toAuditBean.setStatusDetail(bean.getDescription());
		toAuditBean.setInMessage(bean.toString());
		toAuditBean.setInMessageId("System1");
		//toAuditBean.setOutMessage(bean.getOutMessage());
		toAuditBean.setOutMessageId("System2");

		return toAuditBean;
	}

	public synchronized void auditTransaction(TransactionAuditBean auditBean) {
		logger.trace("Audit Transaction");
		
		auditBean.setRunId(runId);
		getAudit().auditTransaction(auditBean);
		
		if (auditBean.getStatus().equals("FAILURE")) {
			addToFailure();
		} else {
			addToSuccess();
		}
	}

	public synchronized void addToSuccess() {
		numberOfSuccess++;
	}

	public synchronized void addToFailure() {
		numberOfFailures++;
	}

	public synchronized void finalize() throws DataAccessException {
		if (audit != null && audit.isProcessAuditStarted()
				&& !audit.isProcessAuditStopped()) {
			logger.trace("Finalize Audit");
			
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

	public String generateMessage(${artifactId}Bean bean) {
		// Not Required
		return null;
	}

	public void initializeProcess() throws DataAccessException {
		logger.trace("Initialize Audit");
		
		int result = 0;
		numberOfFailures = 0;
		numberOfSuccess = 0;
		
		if (getAudit() != null && !getAudit().isProcessAuditStarted()) {
			ProcessAuditBean processAuditBean = new ProcessAuditBean();
			processAuditBean.setProcessName(processName);
			java.util.Date today = new java.util.Date();
			processAuditBean.setRunStart(new java.sql.Timestamp(today.getTime()));
		
			processAuditBean.setSourceSync(new Timestamp(new Date().getTime()));
		
			result = getAudit().startProcessAudit(processAuditBean);
			getAudit().setProcessAuditStarted(true);
			getAudit().setProcessAuditStopped(false);
		}
		
		runId = result;
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
}