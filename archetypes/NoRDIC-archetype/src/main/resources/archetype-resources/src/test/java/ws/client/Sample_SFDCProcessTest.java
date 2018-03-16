package ${package}.ws.client;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ${package}.beans.${artifactId}Message; 

@ContextConfiguration(locations = {"classpath:msgConsumerTest.xml" })
@RunWith(SpringJUnit4ClassRunner.class)

public class Sample_SFDCProcessTest {

	@Autowired
	@Qualifier("tESFDCProcess")
	private SFDCProcessSOAP test;
	
	private ${artifactId}Message nordicMessage;

	@Before
	public void setUp() throws Exception {
		/*TEBean te = new TEBean();
		te.setATTRCODEC("ATTRCODE");
		te.setATTRDESCC("ATTRDESC");
		te.setGRPC("GRP");
		te.setINSTC("INST");
		te.setINSTCODEC("INST_CODE");
		te.setMINGRADEC("MIN_GRADE");
		te.setPRGMC("PRGM");
		te.setPRIMARYINDC("PRIMARY_IND");
		te.setSTATUSC("STATUS");
		te.setTLVLC("TLVL");
		te.setTRNSFCATLOGC(new String("0"));
		te.setTRNSFCRSEC("TRNSF_CRSE");
		te.setTRNSFHIHRSC(new Double(0));
		te.setTRNSFLOWHRSC(new Double(0));
		te.setTRNSFSUBJC("TRNSF_SUBJ");
		te.setTRNSFTITLEC("TRNSF_TITLE");
		te.setTRSNFEFFTERMC(new String("0"));
		
		TEDetail[] teDetails = new TEDetail[1];
		
		TEDetail ted = new TEDetail();
		
		ted.setInstCode("INST_CODE");
		ted.setPrgm("PRGM");
		ted.setTlvl("TLVL");
		ted.setTrnsfCrse("TRNSF_CRSE");
		ted.setTrnsfSubj("TRNSF_SUBJ");
		ted.setInstCrse("InstCrse");
		ted.setInstSubj("InstSubj");
		ted.setSeqno(new Double(1));
		ted.setCrHi(new Double(0));
		ted.setCrLow(new Double(0));
		ted.setCrUsed(new Double(0));
		
		teDetails[0] = ted;
		
		te.setDETAILRECORDS(teDetails);*/
		
		nordicMessage = new ${artifactId}Message(null);
	}

	@Test
	public void testInitialize() {
		try {
			//test.initialize();
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getCause().getMessage());
		}
	}

	@Test
	public void testCreateTEObject() {
		test.create${artifactId}Object(nordicMessage);
	}
	
	@Test
	public void testLookUpCourse() {
		try {
			//IntegrateCourseC cc = test.lookUpCourse("a5TQ00000019O1IMAU");
			
			//logger.info(cc.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}