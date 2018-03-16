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
package ${package}.queue.post;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import oracle.jms.AQjmsSession;



public class QueuePublisher {
	Log logger = LogFactory.getLog(QueuePublisher.class);

	/**
	 * Instantiates a new message publisher.
	 * 
	 * @param tcf
	 *            the tcf
	 * @throws Exception
	 *             the exception
	 */
	
	public QueuePublisher (QueueConnectionFactory qcf)
			throws Exception {
		super();
		
		setQueueConnectionFactory(qcf);

	}

	/** The queue connection factory. */
	private QueueConnectionFactory QueueConnectionFactory;

	/** The session acknowledge mode. */
	private String sessionAcknowledgeMode;

	/** The username. */
	private String username;

	/** The password. */
	private String password;

	/** The Queue Name. */
	private String QueueName;

	/** The Queue Schema. */
	private String QueueSchema;

	/** The Queue Connection. */
	private QueueConnection connection;

	/** The Queue session. */
	private QueueSession session;

	/** The Queue. */
	protected Queue Queue;

	/**
	 * @return the sessionAcknowledgeMode
	 */
	public String getSessionAcknowledgeMode() {
		return sessionAcknowledgeMode;
	}

	/**
	 * @param sessionAcknowledgeMode the sessionAcknowledgeMode to set
	 */
	public void setSessionAcknowledgeMode(String sessionAcknowledgeMode) {
		this.sessionAcknowledgeMode = sessionAcknowledgeMode;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the QueueName
	 */
	public String getQueueName() {
		return QueueName;
	}

	/**
	 * @param QueueName the QueueName to set
	 */
	public void setQueueName(String QueueName) {
		this.QueueName = QueueName;
	}


	public void setQueueSchema(String QueueSchema) {
		this.QueueSchema = QueueSchema;
	}

	public String getQueueSchema() {
		return QueueSchema;
	}
	
	/**
	 * @return the connection
	 */
	public QueueConnection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(QueueConnection connection) {
		this.connection = connection;
	}

	/**
	 * @return the session
	 */
	public QueueSession getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(QueueSession session) {
		this.session = session;
	}

	/**
	 * @return the Queue
	 */
	public Queue getQueue() {
		return Queue;
	}

	/**
	 * @param Queue the Queue to set
	 */
	public void setQueue(Queue Queue) {
		this.Queue = Queue;
	}

	/**
	 * @return the QueueConnectionFactory
	 */
	public QueueConnectionFactory getQueueConnectionFactory() {
		return QueueConnectionFactory;
	}

	/**
	 * Sets the Queue connection factory.
	 * 
	 * @param QueueConnectionFactory
	 *            the new Queue connection factory
	 */
	public void setQueueConnectionFactory(
			QueueConnectionFactory QueueConnectionFactory) {
		this.QueueConnectionFactory = QueueConnectionFactory;
	}
	
	/**
	 * Initialize connection.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void initializeConnection() throws Exception {
			debug("Entering initializeConnection()");
		
		connection = getQueueConnectionFactory().createQueueConnection(
				getUsername(), getPassword());
			debug("Queue Connection created");
			startConnection(false);
		
	}

	/**
	 * Start connection.
	 * 
	 * @param transactedo
	 *            the transacted
	 * @throws Exception
	 *             the exception
	 */
	public void startConnection(boolean transacted) throws Exception {
		debug("Starting Queue Connection()");
		
		int sessionMode = Integer.parseInt(getSessionAcknowledgeMode());
		session = connection.createQueueSession(transacted,
				sessionMode);
		connection.start();
		Queue = ((AQjmsSession) session).getQueue(getQueueSchema(),
				getQueueName());
		debug("Queue connection started");
		
	}
	
    public void postMessage(String textMessage) throws JMSException {
    	debug("posting message");

		QueueSender sender = session.createSender(Queue);
		TextMessage text_message = session.createTextMessage(textMessage);
		
    	sender.send(text_message);
    }

	/**
	 * closes the connection
	 */
	public void close() {
		logger.trace("Closing Connection");
		
		try {
			connection.close();
		} catch (JMSException e) {
			logger.warn("Failed to close connection");
			debug("Exception: ", e);
		}
		
		logger.info("Connection Closed");
	}

	private void debug(String message) {
		logger.debug(message, null);
	}
	
	private void debug(String message, Exception e) {
		if (logger.isDebugEnabled()) {
			
			if (e != null) {
				logger.debug(message, e);
			} else
				logger.debug(message);
		}
	}
}