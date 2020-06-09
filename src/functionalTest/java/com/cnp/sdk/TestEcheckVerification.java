package com.cnp.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.CnpOnlineException;
import com.cnp.sdk.generate.Contact;
import com.cnp.sdk.generate.EcheckAccountTypeEnum;
import com.cnp.sdk.generate.EcheckTokenType;
import com.cnp.sdk.generate.EcheckType;
import com.cnp.sdk.generate.EcheckVerification;
import com.cnp.sdk.generate.EcheckVerificationResponse;
import com.cnp.sdk.generate.OrderSourceType;

public class TestEcheckVerification {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}
	
	@Test
	public void simpleEcheckVerification() throws Exception{
		EcheckVerification echeckverification = new EcheckVerification();
		echeckverification.setAmount(123456L);
		echeckverification.setOrderId("12345");
		echeckverification.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeckverification.setEcheck(echeck);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("cnp.com");
		echeckverification.setBillToAddress(contact);
		echeckverification.setId("id");
		EcheckVerificationResponse response = cnp.echeckVerification(echeckverification);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void echeckVerificationWithEcheckToken() throws Exception{
		EcheckVerification echeckverification = new EcheckVerification();
		echeckverification.setAmount(123456L);
		echeckverification.setOrderId("12345");
		echeckverification.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckTokenType token = new EcheckTokenType();
		token.setAccType(EcheckAccountTypeEnum.CHECKING);
		token.setCnpToken("1234565789012");
		token.setRoutingNum("123456789");
		token.setCheckNum("123455");
		echeckverification.setEcheckToken(token);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("cnp.com");
		echeckverification.setBillToAddress(contact);
		echeckverification.setId("id");
		EcheckVerificationResponse response = cnp.echeckVerification(echeckverification);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void testMissingBillingField() throws Exception {
		EcheckVerification echeckVerification = new EcheckVerification();
		echeckVerification.setReportGroup("Planets");
		echeckVerification.setAmount(123L);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeckVerification.setEcheck(echeck);
		echeckVerification.setOrderId("12345");
		echeckVerification.setOrderSource(OrderSourceType.ECOMMERCE);
		try {
			cnp.echeckVerification(echeckVerification);
			fail("Expected exception");
		} catch(CnpOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
	}

}

