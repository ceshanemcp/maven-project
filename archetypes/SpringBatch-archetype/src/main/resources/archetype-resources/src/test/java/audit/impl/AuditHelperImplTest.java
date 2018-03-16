package ${package}.audit.impl;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.devry.integration.audit.AuditInterface;
import com.devry.integration.audit.bean.ProcessAuditBean;
import com.devry.integration.audit.bean.TransactionAuditBean;
import ${package}.audit.impl.AuditHelperImpl;
import ${package}.bean.${artifactId}Bean;

public class AuditHelperImplTest {

	private AuditHelperImpl auditHelper;
	private AuditInterface audit;

	@Before
	public void setUp() {
		auditHelper = new AuditHelperImpl();
		auditHelper.setProcessName("SomethingNotWorking");
		audit = EasyMock.createMock(AuditInterface.class);
		auditHelper.setAudit(audit);
	}

	@Test
	public void testInitialize() {
		EasyMock.expect(
				audit.startProcessAudit(EasyMock
						.anyObject(ProcessAuditBean.class))).andReturn(10)
				.anyTimes();
		audit.isProcessAuditStarted();
		EasyMock.expectLastCall().andReturn(false).times(1);
		audit.isProcessAuditStarted();
		EasyMock.expectLastCall().andReturn(true).times(1);
		audit.setProcessAuditStarted(true);
		EasyMock.expectLastCall().anyTimes();
		audit.setProcessAuditStopped(false);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(audit);

		/*
		 * Test 1 - Successful
		 */

		auditHelper.initializeProcess();
		Assert.assertEquals(10, auditHelper.getRunId());

		/*
		 * Test 2 - Already Started
		 */
		auditHelper.initializeProcess();
		Assert.assertEquals(0, auditHelper.getRunId());
	}

	@Test
	public void createTransactionAuditBean() {
		${artifactId}Bean toBean = new ${artifactId}Bean();
		//toBean.setEmployeeDSI("D00000000");
		//toBean.setStatus("Failure");
		//toBean.setDescription("This is a Test!");
		//toBean.setOutMessage("Nothing Here");

		TransactionAuditBean auditBean = auditHelper
				.createTransactionAuditBean(toBean);
		Assert.assertEquals("D00000000", auditBean.getDSI());
		Assert.assertEquals("Failure", auditBean.getStatus());
	}

	@Test
	public void auditTransaction() {
		TransactionAuditBean auditBean = new TransactionAuditBean();
		audit.auditTransaction(auditBean);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(audit);

		/*
		 * Test 1 - Failure
		 */
		auditBean.setStatus("FAILURE");
		auditHelper.auditTransaction(auditBean);
		Assert.assertEquals(1, auditHelper.getNumberOfFailures());

		/*
		 * Test 1 - Success
		 */
		auditBean.setStatus("SUCCESS");
		auditHelper.auditTransaction(auditBean);
		Assert.assertEquals(1, auditHelper.getNumberOfSuccess());

	}

	@Test
	public void finalize() {
		EasyMock.expect(audit.isProcessAuditStarted()).andReturn(true).times(1);
		EasyMock.expect(audit.isProcessAuditStarted()).andReturn(false)
				.times(1);
		EasyMock.expect(audit.isProcessAuditStopped()).andReturn(false)
				.anyTimes();
		audit.stopProcessAudit(EasyMock.anyObject(ProcessAuditBean.class));
		EasyMock.expectLastCall();
		audit.setProcessAuditStopped(true);
		EasyMock.expectLastCall();
		audit.setProcessAuditStarted(false);
		EasyMock.expectLastCall();
		EasyMock.replay(audit);
		
		/*
		 * Test 1 - Success
		 */
		auditHelper.finalize();
		/*
		 * Test 2 - Already Stopped
		 */
		auditHelper.finalize();
	}
}
