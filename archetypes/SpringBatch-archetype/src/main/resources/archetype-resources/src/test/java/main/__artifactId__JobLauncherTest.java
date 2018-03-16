package ${package}.main;

import junit.framework.TestCase;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;

public class ${artifactId}JobLauncherTest extends TestCase {

	public void testLauncher() {
		${artifactId}JobLauncher launcher = new ${artifactId}JobLauncher();
		JobLauncher jobLauncher = new SimpleJobLauncher();
		Job job = new SimpleJob();
		launcher.setLauncher(jobLauncher);
		launcher.setJob(job);
		assertNotNull(launcher.getLauncher());
		assertNotNull(launcher.getJob());
		assertNull(launcher.getJobName());

	}

}
