/**
 * 
 */
package ${package}.listener;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import ${package}.audit.AuditHelper;

/**
 * This class has been created to handle the process Audit outside 
 * of the individual process.  This will make it easier to manage audit.
 * 
 * @author Charles E. Shane
 *
 */
public class ${artifactId}StepExecutionListener implements StepExecutionListener {

	private static final Logger logger = Logger.getLogger(${artifactId}StepExecutionListener.class);
	private AuditHelper audit;

	
	public AuditHelper getAudit() {
		return audit;
	}

	public void setAudit(AuditHelper audit) {
		this.audit = audit;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.springframework.batch.core.StepExecution)
	 */
	public void beforeStep(StepExecution stepExecution) {
		logger.debug("Starting Audit");
		audit.initializeProcess();
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.springframework.batch.core.StepExecution)
	 */
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.debug("Finalize audit");
		audit.finalize();

		// An ExitStatus to combine with the normal value. 
		// Return null to leave the old value unchanged.
		return null;
	}
}