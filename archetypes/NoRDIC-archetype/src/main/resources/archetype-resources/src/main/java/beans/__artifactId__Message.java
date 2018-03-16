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
package ${package}.beans;

import com.devry.integration.nordic.framework.message.AbstractNoRDICMessage;
import com.devry.integration.nordic.framework.message.NoRDICMessage;
import ${package}.beans.${artifactId}Bean;

/**
 * This class is the generic NoRDICMessage Implementation for the 
 * ${artifactId}Bean Object
 * 
 * @author ${devName}
 * 
 */
public class ${artifactId}Message extends AbstractNoRDICMessage {

	private String InMessageID = "SourceSystem";
	private String OutMessageID = "TargetSystem";
	private String InMessage;
	private String OutMessage;
	private ${artifactId}Bean bean;

	public ${artifactId}Message(Object parsedMessage) {
		super(parsedMessage);
		
		if (parsedMessage instanceof ${artifactId}Bean) {
			this.bean = (${artifactId}Bean) parsedMessage;
		}
	}

	public String getMessageKey() {
		return get${artifactId}Object().get${artifactId}Key();
	}

	public int compareTo(NoRDICMessage message) {
		return this.getMessageKey().compareTo(message.getMessageKey());
	}

	@Override
	public boolean equals(Object message) {
		if (!(message instanceof ${artifactId}Message)) {
			return false;
		}
		
		${artifactId}Message otherMsg = (${artifactId}Message) message;
		
		return this.getMessageKey().equals(otherMsg.getMessageKey());
	}

	public ${artifactId}Bean get${artifactId}Object() {
		return (${artifactId}Bean) this.bean;
	}

	/**
	 * @param bean the bean to set
	 */
	public void set${artifactId}Bean(${artifactId}Bean bean) {
		this.bean = bean;
	}

	public int hashCode() {
		int h = getMessageKey().hashCode();
		return h;
	}

	public String getInMessageID() {
		return InMessageID;
	}

	public void setInMessageID(String inMessageID) {
		InMessageID = inMessageID;
	}

	public String getOutMessageID() {
		return OutMessageID;
	}

	public void setOutMessageID(String outMessageID) {
		OutMessageID = outMessageID;
	}

	public String getInMessage() {
		return InMessage;
	}

	public void setInMessage(String inMessage) {
		InMessage = inMessage;
	}

	public String getOutMessage() {
		return OutMessage;
	}

	public void setOutMessage(String outMessage) {
		OutMessage = outMessage;
	}
}