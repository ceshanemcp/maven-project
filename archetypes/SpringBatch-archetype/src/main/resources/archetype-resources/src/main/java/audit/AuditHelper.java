/**
 * 
 */
package ${package}.audit;

import org.springframework.dao.DataAccessException;

import com.devry.integration.audit.bean.TransactionAuditBean;
import ${package}.bean.${artifactId}Bean;

/**
 * Using JRE 1.6.0_25
 * 
 * The Interface AuditHelper.
 * 
 * @author Ian Hamilton
 * @version 1.0
 * @since 1.0
 */
public interface AuditHelper {

	/**
	 * Audit transaction.
	 * 
	 * Audits each individual transaction to the corresponding runId.
	 * 
	 * @param runId
	 *            the run id
	 * @param status
	 *            the status (Success, Failure)
	 * @param message
	 *            the message (Typically the ToString() of a Bean)
	 * @param errorMessage
	 *            the error message (If an Error Message is supplied)
	 */
	public void auditTransaction(TransactionAuditBean auditBean);

	/**
	 * Finalize.
	 * 
	 * Finalize a process audit with number of Success/Failures
	 * 
	 * @param runId
	 *            the run id
	 */
	public void finalize() throws DataAccessException;

	/**
	 * 
	 * @param disMessage
	 * @return
	 */
	public String generateMessage(${artifactId}Bean bean);

	/**
	 * Initialize process.
	 * 
	 * Audits the start up of the process and returns a runId.
	 * 
	 * @return the int
	 */

	public TransactionAuditBean createTransactionAuditBean(${artifactId}Bean bean);

	public void initializeProcess() throws DataAccessException;

	public void setNumberOfFailures(int numberOfFailures);

	public void setNumberOfSuccess(int success);

	public void addToSuccess();

	public void addToFailure();
}