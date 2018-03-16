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
package ${package}.consumer.sp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.driver.OracleTypes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import ${package}.beans.${artifactId}Bean;
import ${package}.consumer.sp.mapper.${artifactId}RowMapper;

/**
 * @author ${devName}
 * 
 */
public class MessageConsumerReaderSP extends StoredProcedure {

	private static final Log logger = LogFactory
			.getLog(MessageConsumerReaderSP.class);
    //TODO S
	/** 
	 * @param DataSource
	 * @param Stored
	 *            Procedure Name
	 */
	public MessageConsumerReaderSP(DataSource ds, String storedProcName) {
		super(ds, storedProcName);
		setFunction(false);
		declareParameter(new SqlParameter("StartDT", OracleTypes.TIMESTAMP));
		declareParameter(new SqlParameter("EndDT", OracleTypes.TIMESTAMP));
		declareParameter(new SqlOutParameter("result", OracleTypes.CURSOR,
				new ${artifactId}RowMapper(ds)));
		compile();
	}

	/*
	 * Execute stored proc to read messages
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<${artifactId}Bean> getMessages(Timestamp startTime, Timestamp endTime)
			throws Exception {
		ArrayList<${artifactId}Bean> returnList = new ArrayList<${artifactId}Bean>();
		Map inputParams = new HashMap();
		inputParams.put("StartDT", startTime);
		inputParams.put("EndDT", endTime);
		logger.info("Execute MessageConsumerReaderSP.getMessages()");
		Map returnMap = this.execute(inputParams);
		
		returnList = (ArrayList) returnMap.get("result");

		return returnList;
	}
}