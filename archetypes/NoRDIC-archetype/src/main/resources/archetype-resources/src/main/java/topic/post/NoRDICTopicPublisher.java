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
package ${package}.topic.post;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import oracle.jms.AQjmsSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NoRDICTopicPublisher {
	Log logger = LogFactory.getLog(NoRDICTopicPublisher.class);

	/**
	 * Instantiates a new message publisher.
	 * 
	 * @param tcf
	 *            the tcf
	 * @throws Exception
	 *             the exception
	 */
	
	public NoRDICTopicPublisher (TopicConnectionFactory tcf)
			throws Exception {
		super();
		
		setTopicConnectionFactory(tcf);

	}

	/** The queue connection factory. */
	private TopicConnectionFactory topicConnectionFactory;

	/** The session acknowledge mode. */
	private String sessionAcknowledgeMode;

	/** The username. */
	private String username;

	/** The password. */
	private String password;

	/** The Topic Name. */
	private String topicName;

	/** The Topic Schema. */
	private String topicSchema;

	/** The Topic Connection. */
	private TopicConnection connection;

	/** The Topic session. */
	private TopicSession session;

	/** The Topic. */
	protected Topic topic;

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
	 * @return the topicName
	 */
	public String getTopicName() {
		return topicName;
	}

	/**
	 * @param topicName the topicName to set
	 */
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}


	public void setTopicSchema(String topicSchema) {
		this.topicSchema = topicSchema;
	}

	public String getTopicSchema() {
		return topicSchema;
	}
	
	/**
	 * @return the connection
	 */
	public TopicConnection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(TopicConnection connection) {
		this.connection = connection;
	}

	/**
	 * @return the session
	 */
	public TopicSession getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(TopicSession session) {
		this.session = session;
	}

	/**
	 * @return the topic
	 */
	public Topic getTopic() {
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	/**
	 * @return the topicConnectionFactory
	 */
	public TopicConnectionFactory getTopicConnectionFactory() {
		return topicConnectionFactory;
	}

	/**
	 * Sets the topic connection factory.
	 * 
	 * @param topicConnectionFactory
	 *            the new topic connection factory
	 */
	public void setTopicConnectionFactory(
			TopicConnectionFactory topicConnectionFactory) {
		this.topicConnectionFactory = topicConnectionFactory;
	}
	
	/**
	 * Initialize connection.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void initializeConnection() throws Exception {
			debug("Entering initializeConnection()", null);
		
		connection = getTopicConnectionFactory().createTopicConnection(
				getUsername(), getPassword());
			debug("Topic Connection created" , null);
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
		debug("Starting Queue Connection()", null);
		
		int sessionMode = Integer.parseInt(getSessionAcknowledgeMode());
		session = connection.createTopicSession(transacted,
				sessionMode);
		connection.start();
		topic = ((AQjmsSession) session).getTopic(getTopicSchema(),
				getTopicName());
		debug("Topic connection started" , null);
		
	}
	
    public void postMessage(String textMessage) throws JMSException {
    	debug("posting message", null);

        TopicPublisher publisher = session.createPublisher(topic);
        TextMessage text_message = session.createTextMessage(textMessage);

        publisher.publish(text_message);
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
			logger.debug("Exception: ", e);
		}
		
		logger.info("Connection Closed");
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