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

package ${package}.main;

import org.apache.log4j.Logger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author ${devName}
 *
 **/
public class ${artifactId}JobLauncher {
	/**
	 * Logger for this class
	 **/
	private static final Logger logger = Logger.getLogger(${artifactId}JobLauncher.class);

	private JobLauncher launcher;
	private Job job;

	@Autowired
	public void setLauncher(JobLauncher bootstrap) {
		this.launcher = bootstrap;
	}

	protected JobLauncher getLauncher() {
		return launcher;
	}

	@Autowired
	public void setJob(Job job) {
		this.job = job;
	}

	public Job getJob() {
		return job;
	}

	protected String getJobName() {
		if (logger.isDebugEnabled()) {
			logger.debug("getJobName() - start"); 
		}

		String returnString = job.getName();
		
		if (logger.isDebugEnabled()) {
			logger.debug("getJobName() - end"); 
		}
		return returnString;
	}
}