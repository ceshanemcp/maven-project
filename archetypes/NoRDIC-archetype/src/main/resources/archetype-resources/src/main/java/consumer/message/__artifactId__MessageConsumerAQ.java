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
package ${package}.consumer.message;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import oracle.jms.AQjmsSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.devry.common.messaging.MessageConsumer;
import ${package}.audit.impl.AuditHelperImpl;
import ${package}.consumer.message.util.DataTypeHelper;

/**
 * This class is the specifically called to de-queue a message from a queue.
 * 
 * @author ${devName}
 * 
 */
public class ${artifactId}MessageConsumerAQ implements MessageConsumer {
	Log logger = LogFactory.getLog(${artifactId}MessageConsumerAQ.class);

	private AuditHelperImpl transAudit;

	/** The queue connection factory. */
	private QueueConnectionFactory queueConnectionFactory;

	/** The session acknowledge mode. */
	private String sessionAcknowledgeMode;

	/** The username. */
	private String username;

	/** The password. */
	private String password;

	/** The queue owner. */
	private String queueOwner;

	/** The queue name. */
	private String queueName;

	private DataTypeHelper dataTypeHelper;

	/** The non destructive. */
	private Boolean nonDestructive;

	/** The selector, which will be used to filter messages */
	private String selector;

	/** The queue connection. */
	private QueueConnection queueConnection;

	/** The queue session. */
	private QueueSession queueSession;

	/** The queue. */
	protected Queue queue;

	/** The message. */
	protected Message message;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.devry.integration.common.framework.JMSMsgCommImpl#getMessages()
	 */
	/**
	 * Instantiates a new staff email message consumer.
	 * 
	 * @param qcf
	 *            the qcf
	 * @throws Exception
	 *             the exception
	 */
	public ${artifactId}MessageConsumerAQ(QueueConnectionFactory qcf)
			throws Exception {
		super();
		
		setQueueConnectionFactory(qcf);
	}

	/**
	 * Initialize connection.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void initializeConnection() throws Exception {
		debug("Entering initializeConnection()", null);
		
		queueConnection = getQueueConnectionFactory().createQueueConnection(
				getUsername(), getPassword());
		
		debug("Queue Connection created" , null);

		startConnection(false);
	}

	/**
	 * Start connection.
	 * 
	 * @param transacted
	 *            the transacted
	 * @throws Exception
	 *             the exception
	 */
	public void startConnection(boolean transacted) throws Exception {
		debug("Starting Queue Connection()", null);
		
		int sessionMode = Integer.parseInt(getSessionAcknowledgeMode());
		queueSession = queueConnection.createQueueSession(transacted,
				sessionMode);
		queueConnection.start();
		queue = ((AQjmsSession) queueSession).getQueue(getQueueOwner(),
				getQueueName());
		
		debug("Queue connection started" , null);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.devry.common.messaging.MessageConsumer#clearMessages()
	 */
	public void clearMessages() throws Exception {
		debug("Entering clearMessages/acknowledgeMessage()", null);
		

		if (this.message != null
				&& Integer.parseInt(getSessionAcknowledgeMode()) == Session.CLIENT_ACKNOWLEDGE) {
			message.acknowledge();
			this.message = null;
			logger.info("Messages in queue cleared.");
		}

		logger.debug("Exiting clearMessages()", null);
	}

	public ArrayList<Object> getMessages(int numberOfMessages) throws Exception {
		logger.trace("Entering getMessages()");
		
		int counter = 0;
		ArrayList<Object> list = new ArrayList<Object>();

		try {
			logger.trace("queue session : " + queueSession);
			QueueBrowser b = queueSession.createBrowser(queue);

			QueueReceiver rcv = queueSession.createReceiver(queue);

			while (b.getEnumeration().hasMoreElements()) {
				if (counter >= numberOfMessages) {
					break;
				}
				
				b.getEnumeration().nextElement();
				javax.jms.Message recmMsg = rcv.receiveNoWait();
				
				if (recmMsg == null) {
					debug("No more messages exiting loop", null);
					break;
				}
				
				if (this.message == null) {
					this.message = recmMsg;
				}
				
				if(recmMsg instanceof TextMessage){
				  TextMessage myMsg = (TextMessage) recmMsg;
				  
				  debug("Queue Message Raw -> " + myMsg.getText(), null);
				
				  list.add(myMsg);
				} else {
					logger
					.warn("Message recieved is not an instance of TextMessage");
					break;
				}
				
				counter++;
			}
		
			logger.info("Received message(s) : " + list.size());
		} catch (Exception e) {
			// log the error
			logger.error("Error in getMessages()", e);
			// throw the error back so appropriate action can be taken
			throw e;
		}

		debug("Exiting getMessage()", null);
		
		return list;
	}

	public AuditHelperImpl getTransAudit() {
		return transAudit;
	}

	public void setTransAudit(AuditHelperImpl transAudit) {
		this.transAudit = transAudit;
	}

	/**
	 * Reset message.
	 */
	public void resetMessage() {
		this.message = null;
	}

	/*
	 * Operation is not supported.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.devry.common.messaging.MessageConsumer#getMessages(java.sql.Timestamp
	 * )
	 */
	public ArrayList<Object> getMessages(Timestamp arg0) throws Exception {
		throw new Exception("Operation not supported");
	}

	/*
	 * Operation is not supported.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.devry.common.messaging.MessageConsumer#getMessage()
	 */
	public Object getMessage() throws Exception {
		throw new Exception("Operation not supported");
	}

	/**
	 * closes the connection
	 */
	public void close() {
		logger.trace("Closing Connection");
	
		try {
			getQueueConnection().close();
			getQueueSession().close();
		} catch (JMSException e) {
			logger.warn("Failed to close connection");
			logger.debug("Exception: ", e);
		}
		
		logger.info("Connection Closed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.devry.common.messaging.MessageConsumer#getSourceSyncTime()
	 */
	public Timestamp getSourceSyncTime() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.devry.common.messaging.MessageConsumer#isNonDestructive()
	 */
	public boolean isNonDestructive() {
		return nonDestructive;
	}

	/**
	 * Sets the non destructive.
	 * 
	 * @param nonDestructive
	 *            the new non destructive
	 */
	public void setNonDestructive(boolean nonDestructive) {

		if (dataTypeHelper.getStringValue() == null)
			this.nonDestructive = false;

		if (dataTypeHelper.getStringValue().equalsIgnoreCase("True")) {
			this.nonDestructive = Boolean.TRUE;
		} else {
			this.nonDestructive = Boolean.FALSE;
		}
	}

	/**
	 * Sets the queue connection factory.
	 * 
	 * @param queueConnectionFactory
	 *            the new queue connection factory
	 */
	public void setQueueConnectionFactory(
			QueueConnectionFactory queueConnectionFactory) {
		this.queueConnectionFactory = queueConnectionFactory;
	}

	public String getSessionAcknowledgeMode() {
		return sessionAcknowledgeMode;
	}

	public void setSessionAcknowledgeMode(String sessionAcknowledgeMode) {
		this.sessionAcknowledgeMode = sessionAcknowledgeMode;
	}

	/**
	 * Gets the queue owner.
	 * 
	 * @return the queue owner
	 */
	public String getQueueOwner() {
		return queueOwner;
	}

	/**
	 * Sets the queue owner.
	 * 
	 * @param queueOwner
	 *            the new queue owner
	 */
	public void setQueueOwner(String queueOwner) {
		this.queueOwner = queueOwner;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	/**
	 * Gets the queue name.
	 * 
	 * @return the queue name
	 */
	public String getQueueName() {
		return queueName;
	}

	/**
	 * Sets the queue name.
	 * 
	 * @param queueName
	 *            the new queue name
	 */
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	/**
	 * Gets the queue connection.
	 * 
	 * @return the queue connection
	 */
	public QueueConnection getQueueConnection() {
		return queueConnection;
	}

	/**
	 * Sets the queue connection.
	 * 
	 * @param queueConnection
	 *            the new queue connection
	 */
	public void setQueueConnection(QueueConnection queueConnection) {
		this.queueConnection = queueConnection;
	}

	/**
	 * Gets the queue session.
	 * 
	 * @return the queue session
	 */
	public QueueSession getQueueSession() {
		return queueSession;
	}

	/**
	 * Sets the queue session.
	 * 
	 * @param queueSession
	 *            the new queue session
	 */
	public void setQueueSession(QueueSession queueSession) {
		this.queueSession = queueSession;
	}

	/**
	 * Gets the queue connection factory.
	 * 
	 * @return the queue connection factory
	 */
	public QueueConnectionFactory getQueueConnectionFactory() {
		return queueConnectionFactory;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public DataTypeHelper getDataTypeHelper() {
		return dataTypeHelper;
	}

	public void setDataTypeHelper(DataTypeHelper dataTypeHelper) {
		this.dataTypeHelper = dataTypeHelper;
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