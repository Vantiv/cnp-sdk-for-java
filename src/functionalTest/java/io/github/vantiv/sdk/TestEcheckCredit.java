package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.Contact;
import io.github.vantiv.sdk.generate.EcheckAccountTypeEnum;
import io.github.vantiv.sdk.generate.EcheckCredit;
import io.github.vantiv.sdk.generate.EcheckCreditResponse;
import io.github.vantiv.sdk.generate.EcheckTokenType;
import io.github.vantiv.sdk.generate.EcheckType;
import io.github.vantiv.sdk.generate.OrderSourceType;

public class TestEcheckCredit {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}

	@Test
	public void simpleEcheckCredit() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(12L);
		echeckcredit.setCnpTxnId(123456789101112L);
		echeckcredit.setId("id");
		EcheckCreditResponse response = cnp.echeckCredit(echeckcredit);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void noCnpTxnId() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		try {
			cnp.echeckCredit(echeckcredit);
			fail("Expected exception");
		} catch(CnpOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
	}
	
	@Test
	public void echeckCreditWithEcheck() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(12L);
		echeckcredit.setOrderId("12345");
		echeckcredit.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeckcredit.setEcheck(echeck);
		Contact billToAddress = new Contact();
		billToAddress.setName("Bob");
		billToAddress.setCity("Lowell");
		billToAddress.setState("MA");
		billToAddress.setEmail("cnp.com");
		echeckcredit.setBillToAddress(billToAddress);
		echeckcredit.setId("id");
		EcheckCreditResponse response = cnp.echeckCredit(echeckcredit);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void echeckCreditWithToken() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(12L);
		echeckcredit.setOrderId("12345");
		echeckcredit.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckTokenType token = new EcheckTokenType();
		token.setAccType(EcheckAccountTypeEnum.CHECKING.value());
		token.setCnpToken("1234565789012");
		token.setRoutingNum("123456789");
		token.setCheckNum("123455");
		echeckcredit.setEcheckToken(token);
		Contact billToAddress = new Contact();
		billToAddress.setName("Bob");
		billToAddress.setCity("Lowell");
		billToAddress.setState("MA");
		billToAddress.setEmail("cnp.com");
		echeckcredit.setBillToAddress(billToAddress);
		echeckcredit.setId("id");
		EcheckCreditResponse response = cnp.echeckCredit(echeckcredit);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void missingBilling() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(12L);
		echeckcredit.setOrderId("12345");
		echeckcredit.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeckcredit.setEcheck(echeck);
		try {
			cnp.echeckCredit(echeckcredit);
			fail("Expected exception");
		} catch(CnpOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
	}
	
	@Test
    public void echeckCreditWithSecondryAmount() throws Exception{
        EcheckCredit echeckcredit = new EcheckCredit();
        echeckcredit.setAmount(12L);
        echeckcredit.setSecondaryAmount(50L);
        echeckcredit.setOrderId("12345");
        echeckcredit.setOrderSource(OrderSourceType.ECOMMERCE);
        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("12345657890");
        echeck.setRoutingNum("123456789");
        echeck.setCheckNum("123455");
        echeckcredit.setEcheck(echeck);
        Contact billToAddress = new Contact();
        billToAddress.setName("Bob");
        billToAddress.setCity("Lowell");
        billToAddress.setState("MA");
        billToAddress.setEmail("cnp.com");
        echeckcredit.setBillToAddress(billToAddress);
        echeckcredit.setId("id");
        EcheckCreditResponse response = cnp.echeckCredit(echeckcredit);
        assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
    }
	
	@Test
    public void echeckCreditWithCnpTxnIdAndSecondryAmount() throws Exception{
	    EcheckCredit echeckcredit = new EcheckCredit();
        echeckcredit.setAmount(12L);
        echeckcredit.setCnpTxnId(123456789101112L);
        echeckcredit.setSecondaryAmount(50L);
        echeckcredit.setId("id");
        EcheckCreditResponse response = cnp.echeckCredit(echeckcredit);
        assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
    }

	@Test
	public void echeckCreditWithEcheckCustId() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(12L);
		echeckcredit.setOrderId("12345");
		echeckcredit.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeck.setEcheckCustomerId("22345678");
		echeckcredit.setEcheck(echeck);
		Contact billToAddress = new Contact();
		billToAddress.setName("Bob");
		billToAddress.setCity("Lowell");
		billToAddress.setState("MA");
		billToAddress.setEmail("cnp.com");
		echeckcredit.setBillToAddress(billToAddress);
		echeckcredit.setId("id");
		EcheckCreditResponse response = cnp.echeckCredit(echeckcredit);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

}

