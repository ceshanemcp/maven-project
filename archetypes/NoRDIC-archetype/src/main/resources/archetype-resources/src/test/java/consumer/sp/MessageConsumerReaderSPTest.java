#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 
 */
package ${package}.consumer.sp;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import ${package}.beans.${artifactId}Bean;

@ContextConfiguration(locations = {"classpath:msgConsumerTest.xml" })
@RunWith(SpringJUnit4ClassRunner.class)

/*
 * 
 * 
 * @author Charles E. Shane
 */
public class MessageConsumerReaderSPTest {


	private static final Log logger = LogFactory.getLog(MessageConsumerReaderSPTest.class);

	@Autowired
	@Qualifier("messageConsumerReaderSP")
	private MessageConsumerReaderSP sp;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		logger.info("Setup worked!");
	}

	/**
	 * Test method for
	 * {@link ${package}.consumer.sp.MessageConsumerReaderSP${symbol_pound}getMessages(java.sql.Timestamp, java.sql.Timestamp)}
	 * .
	 */
	@Test
	public void testGetMessages() {
		logger.info("Starting test.");

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		
		try {
			date = dateFormat.parse("23/09/2014");
			long time = date.getTime();

			Timestamp startTime = new Timestamp(time);
			Timestamp endTime = new Timestamp(Calendar.getInstance().getTime().getTime());

			try {
				ArrayList<${artifactId}Bean> beans = sp.getMessages(startTime, endTime);
				
				for (int i=0 ; i < beans.size() ; i++) {
					${artifactId}Bean bean = beans.get(i);
					
					assertTrue(beans.size() > 0); 

					if (i == 0) {
						logger.info("Count: " + beans.size());
						logger.info(bean.toString());
					}
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}
}