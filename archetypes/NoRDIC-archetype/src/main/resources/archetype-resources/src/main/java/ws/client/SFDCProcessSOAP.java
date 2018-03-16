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
package ${package}.ws.client;

import ${package}.beans.${artifactId}Bean;
import ${package}.beans.${artifactId}Message;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlOptions;

import com.devry.common.client.pojo.Response;
import com.devry.common.client.pojo.UserCredentials;
import com.devry.common.pojo.PropertyBean;
import com.devry.common.ws.client.AbstractDevryWSClient;
import com.devry.crm.enterprise.ccp.dkc.ws.client.InvalidIdFault;
import com.devry.crm.enterprise.ccp.dkc.ws.client.LoginFault;
import com.devry.crm.enterprise.ccp.dkc.ws.client.SforceServiceStub;
import com.devry.crm.enterprise.ccp.dkc.ws.client.UnexpectedErrorFault;
import com.devry.integration.nordic.framework.message.MessageResult;
import com.devry.integration.nordic.framework.message.MessageResult.ResultType;
import com.sforce.soap.enterprise.CreateDocument;
import com.sforce.soap.enterprise.CreateResponseDocument;
import com.sforce.soap.enterprise.LoginDocument;
import com.sforce.soap.enterprise.LoginResponseDocument;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.SessionHeaderDocument;
import com.sforce.soap.enterprise.CreateDocument.Create;
import com.sforce.soap.enterprise.LoginDocument.Login;
import com.sforce.soap.enterprise.SessionHeaderDocument.SessionHeader;
import com.sforce.soap.enterprise.sobject.IntegrateCourseC;
/**
 * SalesForce Class that Wraps the Web Service Calls for the Login and Create
 * calls This is using the Enterprise WSDL
 * 
 * @author Charles E. Shane
 * 
 */
public class SFDCProcessSOAP extends AbstractDevryWSClient {

	private static final Log logger = LogFactory.getLog(SFDCProcessSOAP.class);

	// debugging flag for not posting. Change this flag to true if you do not
	// want to post to SFDC.
	private boolean noPosty = false;
	private SforceServiceStub stub;
	private SessionHeaderDocument sessionDoc;
	private LoginDocument loginDoc;
	private Calendar today;

	@PostConstruct
	public void initialize() throws LoginFault, Exception, RemoteException,
			InvalidIdFault, UnexpectedErrorFault {
		logger.info("Initialize Start. Setting up SFDC authentication");
		// Stub set up to login to sfdc endpoint (test/login.salesforce.com)
		
		try {
			setLoginDoc(LoginDocument.Factory.newInstance());
			Login login = loginDoc.addNewLogin();
			login.setUsername(getUserCred().getUserName());
			login.setPassword(getUserCred().getPassword());
			LoginResponseDocument loginResponseDoc = getStub().login(loginDoc,
					null);
			logger.info("Login to SFDC Successful ");

			if (loginResponseDoc != null) {
				getUserCred().setSecurityToken(
						loginResponseDoc.getLoginResponse().getResult()
								.getSessionId());
				setSessionDoc(SessionHeaderDocument.Factory.newInstance());
				SessionHeader sessionHeader = SessionHeader.Factory
						.newInstance();
				sessionHeader.setSessionId(loginResponseDoc.getLoginResponse()
						.getResult().getSessionId());
				sessionDoc.setSessionHeader(sessionHeader);
				// Login successful set up stub to endpoint returned from login
				setStub(new SforceServiceStub(loginResponseDoc
						.getLoginResponse().getResult().getServerUrl()));
				// debug("sessionDoc"+sessionDoc.toString());
			} else {
				logger.error("No session id returned...Login Error");
			}
		} catch (RemoteException e) {
			logger.error("Remote Exception while login to SFDC. ", e);
			debug("Remote Exception - Username: "
					+ getLoginDoc().getLogin().getUsername());
			trace("Password" + getLoginDoc().getLogin().getPassword());
			logger.error("SFDC Stub:"
					+ getStub()._getServiceClient().getOptions().getTo());
			throw e;
		} catch (InvalidIdFault e) {
			logger.error("InvalidIdFault while login to SFDC ", e);
			throw e;
		} catch (UnexpectedErrorFault e) {
			logger.error("UnexpectedErrorFault while login to SFDC ", e);
			throw e;
		} catch (LoginFault e) {
			logger.error("LoginFault while login to SFDC.  ["
							+ e.getFaultMessage().getLoginFault().getExceptionCode()
							+ "] "
							+ e.getFaultMessage().getLoginFault().getExceptionMessage());
			debug("Username: " + getLoginDoc().getLogin().getUsername());
			debug("Password: " + getLoginDoc().getLogin().getPassword());
			logger.error("SFDC Stub:" + getStub()._getServiceClient().getOptions().getTo());
			
			throw e;
		} catch (Exception e) {
			logger.error("Unknown exception on login to SFDC", e);
			throw e;
		}
		
		today = Calendar.getInstance();

		trace("Initialize End");
	}

	private CreateDocument createSFIntegrateCourseCObject(
			${artifactId}Message message) {
		IntegrateCourseC cc = IntegrateCourseC.Factory.newInstance();
		IntegrateCourseC ccList[] = new IntegrateCourseC[1];

		/* TODO:  Map to the SFDC object you are trying to create.

		${artifactId}Bean bean = message.get${artifactId}Object();
		cc.setATTRCODEC(te.getATTRCODEC());
		cc.setATTRDESCC(te.getATTRDESCC());
		cc.setGRPC(te.getGRPC());
		cc.setINSTC(te.getINSTC());
		cc.setINSTCODEC(te.getINSTCODEC());
		cc.setMINGRADEC(te.getMINGRADEC());
		cc.setPRGMC(te.getPRGMC());
		cc.setPRIMARYINDC(te.getPRIMARYINDC());
		cc.setSTATUSC(te.getSTATUSC());
		cc.setTLVLC(te.getTLVLC());
		cc.setExternalIDC(te.getTEKey());
		
		try {
			if (te.getTRNSFCATLOGC() != null)
				cc.setTRNSFCATLOGC(te.getTRNSFCATLOGC());
		} catch (Exception e) {
			logger.error("Unable to convert TRNSFCATLOGC for posting to SFDC.",
					e);
			logger.info("		TRNSFCATLOGC: " + te.getTRNSFCATLOGC());
		}

		try {
			if (te.getTRSNFEFFTERMC() != null)
				cc.setTRSNFEFFTERMC(te.getTRSNFEFFTERMC());
		} catch (Exception e) {
			logger.error(
					"Unable to convert TRSNFEFFTERMC for posting to SFDC.", e);
			logger.info("		TRSNFEFFTERMC: " + te.getTRSNFEFFTERMC());
		}

		cc.setTRNSFHIHRSC(te.getTRNSFHIHRSC());
		cc.setTRNSFLOWHRSC(te.getTRNSFLOWHRSC());
		cc.setTRNSFSUBJC(te.getTRNSFSUBJC());
		cc.setTRNSFTITLEC(te.getTRNSFTITLEC());
		cc.setTRNSFCRSEC(te.getTRNSFCRSEC());
	*/
		CreateDocument createDocument = CreateDocument.Factory.newInstance();
		Create create = createDocument.addNewCreate();

		
		ccList[0] = cc;
		create.setSObjectsArray(ccList);

		message.setOutMessage(cc.xmlText());

		return createDocument;
	}

	private CreateDocument createSFIntegrateObject(${artifactId}Bean bean, String id) {

		return null;
		// TODO:  Add in the specific SFDC records you need to update in Salesforce 
		
		/*if (bean.getDETAILRECORDS() != null
				&& bean.getDETAILRECORDS().length > 0) {
			TEDetail[] detailBeans = bean.getDETAILRECORDS();

			IntegrateSequenceC ccList[] = new IntegrateSequenceC[detailBeans.length];
			IntegrateCourseC ic = null;

			try {
				ic = IntegrateCourseC.Factory.newInstance();
				ic.setId(id);
				SObject so = SObject.Factory.newInstance();

				so.setId(id);

				ic.getIntegrateSequencesR();
			} catch (Exception e) {
				logger.error("Unable to create SOject for posting to SFDC.", e);
			}

			for (int j = 0; j < detailBeans.length; j++) {
				TEDetail detail = detailBeans[j];

				IntegrateSequenceC cc = IntegrateSequenceC.Factory
						.newInstance();

				cc.setANDORC(detail.getAndOr());
				cc.setCRHIC(detail.getCrHi().toString());
				cc.setCRLOWC(detail.getCrLow());
				cc.setCRUSEDC(detail.getCrUsed());
				cc.setSEQNOC(detail.getSeqno());
				cc.setINSTANDORC(detail.getInstAndOr());
				cc.setINSTCRSEC(detail.getInstCrse());
				cc.setINSTSUBJC(detail.getInstSubj());
				cc.setINSTTITLEC(detail.getInstTitle());
				cc.setSHRTATCINSTLPARENCONNC(detail.getLparenConn());
				cc.setSHRTATCINSTRPARENC(detail.getRparenConn());
				cc.setIntegrateCourseC(id);

				ccList[j] = cc;
			}

			CreateDocument createDocument = CreateDocument.Factory
					.newInstance();
			Create create = createDocument.addNewCreate();

			create.setSObjectsArray(ccList);

			return createDocument;
		} else {
			logger.info("Bean contains no detail records.");

			return null;
		}*/
	}

	public ${artifactId}Message create${artifactId}Object(${artifactId}Message message) {
		trace("create${artifactId}Object(${artifactId}NoRDICMessage)");
		MessageResult messageResult = new MessageResult();

		XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();

		try {
			CreateDocument createDocument = this.createSFIntegrateCourseCObject(message);
			trace("Message to SFDC: " + createDocument);
			message.setOutMessage(createDocument.xmlText(options));

			CreateResponseDocument createResponseDocument = this
					.postObject(createDocument);

			message.setOutMessage(message.getOutMessage()
					+ " ${symbol_escape}n${symbol_escape}n " + createResponseDocument.getCreateResponse().xmlText(options));
			
			if (createResponseDocument.getCreateResponse().getResultArray(0)
					.getSuccess()) {
				logger.info("Success for Integrate_Course__c: "
						+ createResponseDocument.getCreateResponse()
								.getResultArray(0).getId());
				messageResult.setResult(ResultType.SUCCESS);
				messageResult.setStatus("SUCCESS");
				messageResult.setDescription("Successful TE Object Created.");
				message.setOutMessageID(createResponseDocument
						.getCreateResponse().getResultArray(0).getId());

				CreateDocument createChildDoc = createSFIntegrateObject(
						message.get${artifactId}Object(), createResponseDocument
								.getCreateResponse().getResultArray(0).getId());

				// Child records to post?
				if (createChildDoc != null) {
					message.setOutMessage(message.getOutMessage()
							+ " ${symbol_escape}n${symbol_escape}n " + createChildDoc.xmlText(options));
					CreateResponseDocument createdDetailResponseDocument = this
							.postObject(createChildDoc);

					message.setOutMessage(message.getOutMessage()
							+ " ${symbol_escape}n${symbol_escape}n " + createdDetailResponseDocument.getCreateResponse().xmlText(options));

					if (createdDetailResponseDocument.getCreateResponse()
							.getResultArray().length > 0) {
						for (int i = 0; i < createdDetailResponseDocument
								.getCreateResponse().getResultArray().length; i++) {
							SaveResult saveElement = createdDetailResponseDocument
									.getCreateResponse().getResultArray()[i];

							if (saveElement.getSuccess()) {
								debug("Success for Integrate_Section__c: " + saveElement.getId());
								messageResult.setResult(ResultType.SUCCESS);
								messageResult.setStatus("SUCCESS");
								messageResult.setDescription("Successful Detail Object(s) Created.");
							} else {
								logger.warn("SFDC Detail Record Response: "
											 + saveElement.getErrorsArray(0).getMessage());
								messageResult.setResult(ResultType.FAILED);
								messageResult.setStatus("FAILURE");
								messageResult.setDescription("Detail Object Failed: "
												+ saveElement.getErrorsArray(0).getMessage());
							}
						}
					}
				}
			} else {
				logger.error("SFDC Response: "
						+ createResponseDocument.getCreateResponse()
								.getResultArray(0).getErrorsArray(0)
								.getMessage());
				messageResult.setResult(ResultType.FAILED);
				messageResult.setStatus("FAILURE");
				messageResult.setDescription("${artifactId} Object Failed: "
						+ createResponseDocument.getCreateResponse()
								.getResultArray(0).getErrorsArray(0)
								.getMessage());
			}
		} catch (Exception e) {
			logger.error("SFDC ${artifactId} Object Creation Failed with Exception: ", e);
			messageResult.setResult(ResultType.FAILED);
			messageResult.setStatus("FAILURE");
			messageResult.setDescription("SFDC Exception: " + e.getMessage());
		}

		message.setMessageResult(messageResult);

		trace("Return create${artifactId}Object(${artifactId}NoRDICMessage)");

		return message;
	}

	private CreateResponseDocument postObject(CreateDocument cd)
			throws Exception {
		if (noPosty) {
			throw new Exception(
					"Debugging flag set to test mode.  No posting to salesforce during this execution.  ");
		}

		CreateResponseDocument createResponseDocument = null;
		createResponseDocument = stub.create(cd, getSessionDoc(), null, null,
				null, null, null, null, null, null, null, null);
		trace("SFDC Response: " + createResponseDocument);

		return createResponseDocument;
	}

	private void debug(String debug) {
		if (logger.isDebugEnabled()) {
			logger.debug(debug);
		}
	}

	private void trace(String trace) {
		if (logger.isTraceEnabled()) {
			logger.trace(trace);
		}
	}

	public SforceServiceStub getStub() {
		return stub;
	}

	public void setStub(SforceServiceStub stub) {
		this.stub = stub;
	}

	public SessionHeaderDocument getSessionDoc() {
		return sessionDoc;
	}

	public void setSessionDoc(SessionHeaderDocument sessionDoc) {
		this.sessionDoc = sessionDoc;
	}

	public LoginDocument getLoginDoc() {
		return loginDoc;
	}

	public void setLoginDoc(LoginDocument loginDoc) {
		this.loginDoc = loginDoc;
	}

	public Calendar getToday() {
		return today;
	}

	public void setToday(Calendar today) {
		this.today = today;
	}

	@Override
	public Response run(UserCredentials arg0, PropertyBean arg1)
			throws Exception {
		return null;
	}
}