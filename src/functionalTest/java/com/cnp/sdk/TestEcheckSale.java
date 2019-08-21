package com.cnp.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.CnpOnlineException;
import com.cnp.sdk.generate.Contact;
import com.cnp.sdk.generate.CountryTypeEnum;
import com.cnp.sdk.generate.CustomBilling;
import com.cnp.sdk.generate.EcheckAccountTypeEnum;
import com.cnp.sdk.generate.EcheckSale;
import com.cnp.sdk.generate.EcheckSalesResponse;
import com.cnp.sdk.generate.EcheckTokenType;
import com.cnp.sdk.generate.EcheckType;
import com.cnp.sdk.generate.OrderSourceType;

public class TestEcheckSale {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}

	@Test
	public void anotherEcheckSale() throws Exception{
		EcheckSale echecksale = new EcheckSale();
		echecksale.setAmount(1L);
		echecksale.setOrderId("");
		echecksale.setOrderSource(OrderSourceType.ECOMMERCE);
		
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("000123456578");
		echeck.setRoutingNum("122235821");
		echecksale.setEcheck(echeck);
		
		Contact contact = new Contact();
		contact.setFirstName("Test");		
		contact.setLastName("Test");
		contact.setAddressLine1("201 - 123 Test Street");
		contact.setCity("Toronto");
		contact.setState("ID");
		contact.setZip("11111");
		contact.setCountry(CountryTypeEnum.USA);
		contact.setEmail("gfdtest201906021435@globalfacesdirect.com");
		contact.setPhone("(416) 111-1111");
		echecksale.setBillToAddress(contact);
		
		echecksale.setId("GFD3V00000051954120190602085843");
		echecksale.setCustomerId("GFD3V000000519541");
		//echecksale.setReportGroup("Planets");
		
		EcheckSalesResponse response = cnp.echeckSale(echecksale);
		//System.out.println(response.getVerificationCode());
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void simpleEcheckSaleWithEcheck() throws Exception{
		EcheckSale echecksale = new EcheckSale();
		echecksale.setAmount(123456L);
		echecksale.setOrderId("12345");
		echecksale.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echecksale.setEcheck(echeck);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("cnp.com");
		echecksale.setBillToAddress(contact);
		echecksale.setId("id");
		EcheckSalesResponse response = cnp.echeckSale(echecksale);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void noAmount() throws Exception {
		EcheckSale echeckSale = new EcheckSale();
		echeckSale.setReportGroup("Planets");
		echeckSale.setId("id");
		try {
			cnp.echeckSale(echeckSale);
			fail("Expected exception");
		} catch(CnpOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
	}

	@Test
	public void echeckSaleWithShipTo() throws Exception{
		EcheckSale echecksale = new EcheckSale();
		echecksale.setReportGroup("Planets");
		echecksale.setAmount(123456L);
		echecksale.setVerify(true);
		echecksale.setOrderId("12345");
		echecksale.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echecksale.setEcheck(echeck);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("cnp.com");
		echecksale.setBillToAddress(contact);
		echecksale.setShipToAddress(contact);
		echecksale.setId("id");
		EcheckSalesResponse response = cnp.echeckSale(echecksale);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void echeckSaleWithEcheckToken() throws Exception{
		EcheckSale echecksale = new EcheckSale();
		echecksale.setReportGroup("Planets");
		echecksale.setAmount(123456L);
		echecksale.setVerify(true);
		echecksale.setOrderId("12345");
		echecksale.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckTokenType token = new EcheckTokenType();
		token.setAccType(EcheckAccountTypeEnum.CHECKING);
		token.setCnpToken("1234565789012");
		token.setRoutingNum("123456789");
		token.setCheckNum("123455");
		echecksale.setEcheckToken(token);
		CustomBilling customBilling = new CustomBilling();
		customBilling.setPhone("123456789");
		customBilling.setDescriptor("good");
		echecksale.setCustomBilling(customBilling);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("cnp.com");
		echecksale.setBillToAddress(contact);
		echecksale.setId("id");
		EcheckSalesResponse response = cnp.echeckSale(echecksale);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void echeckSaleMissingBilling() throws Exception{
		EcheckSale echecksale = new EcheckSale();
		echecksale.setReportGroup("Planets");
		echecksale.setAmount(123456L);
		EcheckTokenType token = new EcheckTokenType();
		token.setAccType(EcheckAccountTypeEnum.CHECKING);
		token.setCnpToken("1234565789012");
		token.setRoutingNum("123456789");
		token.setCheckNum("123455");
		echecksale.setEcheckToken(token);
		echecksale.setVerify(true);
		echecksale.setOrderId("12345");
		echecksale.setOrderSource(OrderSourceType.ECOMMERCE);
		echecksale.setId("id");
		
		try {
			cnp.echeckSale(echecksale);
			fail("Expected exception");
		} catch(CnpOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
	}
	
	@Test
	public void simpleEcheckSale() throws Exception {
		EcheckSale echecksale = new EcheckSale();
		echecksale.setReportGroup("Planets");
		echecksale.setCnpTxnId(123456789101112L);
		echecksale.setAmount(12L);
		echecksale.setId("id");
		EcheckSalesResponse response = cnp.echeckSale(echecksale);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
    public void echeckSaleWithSecondaryAmountAndCCD() throws Exception {
        EcheckSale echecksale = new EcheckSale();
        echecksale.setReportGroup("Planets");
        echecksale.setAmount(123456L);
        echecksale.setSecondaryAmount(50L);
        echecksale.setOrderId("12345");
        echecksale.setOrderSource(OrderSourceType.ECOMMERCE);
        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("12345657890");
        echeck.setRoutingNum("123456789");
        echeck.setCheckNum("123455");
        echeck.setCcdPaymentInformation("12345678901234567890123456789012345678901234567890123456789012345678901234567890");
        echecksale.setEcheck(echeck);
        Contact contact = new Contact();
        contact.setName("Bob");
        contact.setCity("lowell");
        contact.setState("MA");
        contact.setEmail("cnp.com");
        echecksale.setBillToAddress(contact);
        echecksale.setId("id");
        EcheckSalesResponse response = cnp.echeckSale(echecksale);
        assertEquals("Approved", response.getMessage());
    }
	
	@Test
    public void echeckSaleWithSecoundaryAmountAndCCDLongerThan80() throws Exception {
        EcheckSale echecksale = new EcheckSale();
        echecksale.setReportGroup("Planets");
        echecksale.setAmount(123456L);
        echecksale.setSecondaryAmount(50L);
        echecksale.setOrderId("12345");
        echecksale.setOrderSource(OrderSourceType.ECOMMERCE);
        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("12345657890");
        echeck.setRoutingNum("123456789");
        echeck.setCheckNum("123455");
        echeck.setCcdPaymentInformation("123456789012345678901234567890123456789012345678901234567890123456789012345678901");
        echecksale.setEcheck(echeck);
        Contact contact = new Contact();
        contact.setName("Bob");
        contact.setCity("lowell");
        contact.setState("MA");
        contact.setEmail("cnp.com");
        echecksale.setId("id");
        echecksale.setBillToAddress(contact);
        try {
            cnp.echeckSale(echecksale);
            fail("ccdPaymentInformation too long");
        } catch(CnpOnlineException e) {
            assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
        }
    }
	
	@Test
    public void echeckSaleWithCnpTxnIdAndSecondryAmount() throws Exception {
        EcheckSale echecksale = new EcheckSale();
        echecksale.setCnpTxnId(123456789101112L);
        echecksale.setAmount(12L);
        echecksale.setSecondaryAmount(10L);   
        echecksale.setId("id");
        try {
            cnp.echeckSale(echecksale);
            fail("Secondary Amount conflict with Cnp Txn ID");
        } catch(CnpOnlineException e) {
            assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
        }
    }

}

