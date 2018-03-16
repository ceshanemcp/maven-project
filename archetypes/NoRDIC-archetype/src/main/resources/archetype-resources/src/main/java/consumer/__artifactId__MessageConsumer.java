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
package ${package}.consumer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.devry.common.messaging.MessageConsumer;
import ${package}.consumer.sp.LastSourceSyncSP;
import ${package}.consumer.sp.MessageConsumerReaderSP;

/**
 * @author ${devName}
 *
 */
public class ${artifactId}MessageConsumer implements MessageConsumer {

	private static final Log logger = LogFactory.getLog(${artifactId}MessageConsumer.class);

	private boolean nonDestructive;
	private LastSourceSyncSP lastSourceSyncSP;
	private MessageConsumerReaderSP messageConsumerReaderSP;

	/* 
	 */
	public void clearMessages() throws Exception {
		//throw new Exception("Operation not supported");
	}

	/* 
	 * Operation is not supported.
	 */
	public Object getMessage() throws Exception {
		throw new Exception("Operation not supported");
	}

	/* 
	 * Operation is not supported.
	 */
	public ArrayList<Object> getMessages(int numberOfMsgs) throws Exception {
		throw new Exception("Operation not supported");
	}

	/* (non-Javadoc)
	 * @see com.devry.common.messaging.MessageConsumer${symbol_pound}getMessages()
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<Object> getMessages(Timestamp asOfTime) throws Exception {
		
		ArrayList result = messageConsumerReaderSP.getMessages(getSourceSyncTime(), asOfTime);
		
		logger.info("Stored Procedure returned " + result.size() + " messages.");

		return result;
	}

	/*
	 * Get the Timestamp for the last time the source was synchronized. 
	 */
	public Timestamp getSourceSyncTime() {
		Timestamp lastSourceSync = getLastSourceSyncSP().getLastSourceSync();
		logger.info("Last Source Sync: " + lastSourceSync);
		
		if (lastSourceSync == null) {
			Calendar cal = Calendar.getInstance();
			
			cal.add(Calendar.WEEK_OF_YEAR, -1);
			
			//cal.add(Calendar.WEEK_OF_YEAR, -1000);
			lastSourceSync = new Timestamp(cal.getTimeInMillis());
			
			logger.info("Last Source Sync (Overriding null): " + lastSourceSync);
		}
		
		return lastSourceSync;
	}

	/**
	 * @return the nonDestructive
	 */
	public boolean isNonDestructive() {
		return nonDestructive;
	}

	/**
	 * @param nonDestructive the nonDestructive to set
	 */
	public void setNonDestructive(boolean nonDestructive) {
		this.nonDestructive = nonDestructive;
	}

	/**
	 * @return the admissionsLastSourceSyncSP
	 */
	public LastSourceSyncSP getLastSourceSyncSP() {
		return lastSourceSyncSP;
	}

	/**
	 * @param admissionsLastSourceSyncSP the admissionsLastSourceSyncSP to set
	 */
	public void setLastSourceSyncSP(
			LastSourceSyncSP lastSourceSyncSP) {
		this.lastSourceSyncSP = lastSourceSyncSP;
	}

	/**
	 * @return the admissionsMessageReaderSP
	 */
	public MessageConsumerReaderSP getMessageConsumerReaderSP() {
		return messageConsumerReaderSP;
	}

	/**
	 * @param admissionsMessageReaderSP the admissionsMessageReaderSP to set
	 */
	public void setMessageConsumerReaderSP(
			MessageConsumerReaderSP messageConsumerReaderSP) {
		this.messageConsumerReaderSP = messageConsumerReaderSP;
	}
}