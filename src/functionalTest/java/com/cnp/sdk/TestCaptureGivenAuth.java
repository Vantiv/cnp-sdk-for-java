package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.AuthInformation;
import com.cnp.sdk.generate.CaptureGivenAuth;
import com.cnp.sdk.generate.CaptureGivenAuthResponse;
import com.cnp.sdk.generate.CardTokenType;
import com.cnp.sdk.generate.CardType;
import com.cnp.sdk.generate.Contact;
import com.cnp.sdk.generate.FraudResult;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.cnp.sdk.generate.OrderSourceType;
import com.cnp.sdk.generate.ProcessingInstructions;
import com.cnp.sdk.generate.ProcessingTypeEnum;

public class TestCaptureGivenAuth {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}
	
	@Test
	public void simpleCaptureGivenAuthWithCard() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		capturegivenauth.setId("id");
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void testCaptureGivenAuthWithProcessingType() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		capturegivenauth.setProcessingType(ProcessingTypeEnum.ACCOUNT_FUNDING);
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		capturegivenauth.setId("id");
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void testCaptureGivenAuthWithOrignetworkTxnIdAndAmount() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		capturegivenauth.setProcessingType(ProcessingTypeEnum.ACCOUNT_FUNDING);
		capturegivenauth.setOriginalNetworkTransactionId("3399485865");
		capturegivenauth.setOriginalTransactionAmount(9922l);
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		capturegivenauth.setId("id");
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void simpleCaptureGivenAuthWithToken() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType cardtoken = new CardTokenType();
		cardtoken.setCnpToken("123456789101112");
		cardtoken.setExpDate("1210");
		cardtoken.setCardValidationNum("555");
		cardtoken.setType(MethodOfPaymentTypeEnum.VI);
		capturegivenauth.setToken(cardtoken);
		capturegivenauth.setId("id");
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void complexCaptureGivenAuth() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setSecondaryAmount(20L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("cnp.com");
		capturegivenauth.setBillToAddress(contact);
		ProcessingInstructions processinginstructions = new ProcessingInstructions();
		processinginstructions.setBypassVelocityCheck(true);
		capturegivenauth.setProcessingInstructions(processinginstructions);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		capturegivenauth.setId("id");
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}
	@Test
	public void authInfo() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		FraudResult fraudresult = new FraudResult();
		fraudresult.setAvsResult("12");
		fraudresult.setCardValidationResult("123");
		fraudresult.setAuthenticationResult("1");
		fraudresult.setAdvancedAVSResult("123");
		authInfo.setFraudResult(fraudresult);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		capturegivenauth.setId("id");
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}

}

