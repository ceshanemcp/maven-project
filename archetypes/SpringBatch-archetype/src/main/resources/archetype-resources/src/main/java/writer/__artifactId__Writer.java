#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set(
$symbol_escape = '\' )
package ${package}.writer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;

import ${package}.${artifactId}.audit.AuditHelper;
import ${package}.${artifactId}.ClientProcess;
import ${package}..${artifactId}.bean.${artifactId}Bean;

public class ${artifactId}Writer implements ItemWriter<${artifactId}Bean> {

	private static final Logger logger = Logger.getLogger(${artifactId}Writer.class);
	private ClientProcess clientProcess;
	private AuditHelper audit;

	public void write(List<? extends ${artifactId}Bean> beans) {
		logger.trace("write(List<${artifactId}Bean>) Start");
		
		for (${artifactId}Bean bean : beans) {
			//TODO:  Call your java process to write a bean
			getClientProcess().createCase(bean);

			audit.auditTransaction(audit.createTransactionAuditBean(bean));
		}
		
		logger.trace("write(List<${artifactId}Bean>) Finish");
	}
	
	public ClientProcess getClientProcess() {
		return clientProcess;
	}

	public void setClientProcess(ClientProcess clientProcess) {
		this.clientProcess = clientProcess;
	}

	public AuditHelper getAudit() {
		return audit;
	}

	public void setAudit(AuditHelper audit) {
		this.audit = audit;
	}
}