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
package ${package}.consumer.sp;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * @author Neel Prabhu
 *
 * @version 1.0 on Mar 28, 2011
 */
public class LastSourceSyncSP extends StoredProcedure {

	private String processName;
	
	/**
	 * @param DataSource
	 * @param Stored Procedure Name
	 */
	public LastSourceSyncSP(DataSource ds, String storedProcName, String processName) {
		super(ds, storedProcName);
		setProcessName(processName);
		setFunction(true);
		declareParameter(new SqlOutParameter("return",Types.TIMESTAMP));
		declareParameter(new SqlParameter("process_name",Types.VARCHAR));
		compile();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Timestamp getLastSourceSync() {
		Timestamp returnValue = null;
		Map in = new HashMap();
		in.put("process_name", getProcessName());
  
		Object obj = execute(in);

		if (obj != null) {
			Object output = ((HashMap) obj).get("return"); 
			if( output instanceof Timestamp) {
				returnValue = (Timestamp)output;
			}
		}
		return returnValue;
	}

	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * @param processName the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}
}