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
public class MessageEnricherSP extends StoredProcedure {

	private static final Log logger = LogFactory.getLog(MessageEnricherSP.class);
	
    //TODO Set this to the enrichment a message.
	/** 
	 * @param DataSource
	 * @param Stored
	 *            Procedure Name
	 */
	public MessageEnricherSP(DataSource ds, String storedProcName) {
		super(ds, storedProcName);
		setFunction(false);
		declareParameter(new SqlParameter("PIDM", OracleTypes.NUMBER));
		declareParameter(new SqlOutParameter("result", OracleTypes.CURSOR,
				         new ${artifactId}RowMapper(ds)));
		compile();
	}

	/*
	 * Execute stored proc to read messages
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ${artifactId}Bean enrichData(int pidm)
			throws Exception {
		ArrayList<${artifactId}Bean> returnList = new ArrayList<${artifactId}Bean>();
		Map inputParams = new HashMap();
		inputParams.put("PIDM", pidm);
		
		logger.info("Execute MessageEnricherSP()");
		Map returnMap = this.execute(inputParams);
		
		returnList = (ArrayList) returnMap.get("result");

		return returnList.get(0);
	}
}