/*
 * ===========================================================================
 *  Licensed Materials - Property of DeVry, Inc.
 *
 * (C) Copyright DeVry, Inc 2009 All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 *
 * ===========================================================================
 */
package ${package}.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author ${devName}
 *
 **/
public class ${artifactId}JobRunner {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(${artifactId}JobRunner.class);

	public static void main(String args[]) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}
		
		String logLevel = "";

        if (args == null) {
            logLevel = "INFO";
        }
      
        // Other values - of logging levels
        if (logLevel.equalsIgnoreCase("DEBUG")) {
        	logger.info("Debug Mode Enabled");
        	LogManager.getRootLogger().setLevel(Level.DEBUG);
        } else if (logLevel.equalsIgnoreCase("TRACE")) {
        	logger.info("Trace Mode Enabled");
        	LogManager.getRootLogger().setLevel(Level.TRACE);
        }
	
		ExitStatus exitStatus = null;
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		JobExecution job = null;

		SimpleDateFormat sdfDateWithTime = new SimpleDateFormat(
				"yyyyMMdd.hhmmss");
		String dateWithTime = sdfDateWithTime.format(new Date());
		SimpleDateFormat ambSdfDateWithTime = new SimpleDateFormat(
		"yyyyMMdd_kkmm");
		String dateFormatedTime = ambSdfDateWithTime.format(new Date());
		
		try {
//			jobParametersBuilder.addString(
//					BannerAmbassadorConstants.JOB_RUN_PARAM, dateWithTime);
//			jobParametersBuilder.addString(SomeConstants.FILE_DATE_FORMAT, dateFormatedTime);
			ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
					"${artifactId}-Context.xml");

			${artifactId}JobLauncher jobLauncher = (${artifactId}JobLauncher) ctx
					.getBean("jobLauncher");
			job = jobLauncher.getLauncher().run(
					jobLauncher.getJob(),
					jobParametersBuilder.toJobParameters());
		} catch (Exception e) {
			if ((e instanceof org.springframework.dao.CannotSerializeTransactionException)
					&& (e.getMessage().indexOf(
									"ORA-08177: can't serialize access for this transaction") > -1)) {
				logger.warn(e.getMessage());
				// Exit with standard error code 11 (NOSQLSERIALIZE)
				System.exit(11);
			} else {
				logger.error("Exception while running ${artifactId}", e);
				throw e;
			}
		}

		if (job != null) {
			exitStatus = job.getExitStatus();
		}

		if ((exitStatus != null) && (exitStatus.equals(ExitStatus.FAILED))) {
			System.exit(1);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}
}