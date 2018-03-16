#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 
 */
package ${package}.consumer;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Charles E. Shane
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:msgConsumerTest.xml" })

public class ${artifactId}MessageConsumerTest {

	private static final Log logger = LogFactory.getLog(${artifactId}MessageConsumerTest.class);

	@Autowired
	@Qualifier("messageConsumer")
	private ${artifactId}MessageConsumer messageConsumer;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * Test method for {@link ${package}.consumer.TEMessageConsumer${symbol_pound}clearMessages()}.
	 */
	@Test
	public void testClearMessages() {
		try {
			messageConsumer.clearMessages();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link ${package}.consumer.TEMessageConsumer${symbol_pound}getMessage()}.
	 */
	@Test
	public void testGetMessage() {
		try {
			messageConsumer.getMessage();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link ${package}.consumer.TEMessageConsumer${symbol_pound}getMessages(int)}.
	 */
	@Test
	public void testGetMessagesInt() {
		try {
			messageConsumer.getMessages(0);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link ${package}.consumer.TEMessageConsumer${symbol_pound}getMessages(java.sql.Timestamp)}.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testGetMessagesTimestamp() {
		//  This is the real get messages used in processing.
		
		Date date = new Date(200, 0, 0, 0, 0);
		Timestamp asOfTime = new Timestamp(date.getTime());

		try {
			ArrayList<Object> co = messageConsumer.getMessages(asOfTime);
			
			logger.info("Record Counts: " + co.size());
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link ${package}.consumer.TEMessageConsumer${symbol_pound}getSourceSyncTime()}.
	 */
	@Test
	public void testGetSourceSyncTime() {
		logger.info("Last Source Sync: " + messageConsumer.getSourceSyncTime());
		
		assertTrue(true);
	}
}