package com.cnp.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.xml.bind.JAXBElement;

import org.junit.Before;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.CnpOnlineException;
import com.cnp.sdk.Communication;
import com.cnp.sdk.generate.ActionTypeEnum;
import com.cnp.sdk.generate.Activate;
import com.cnp.sdk.generate.ActivateResponse;
import com.cnp.sdk.generate.ActivateReversal;
import com.cnp.sdk.generate.ActivateReversalResponse;
import com.cnp.sdk.generate.AdvancedFraudChecksType;
import com.cnp.sdk.generate.AdvancedFraudResultsType;
import com.cnp.sdk.generate.ApplepayHeaderType;
import com.cnp.sdk.generate.ApplepayType;
import com.cnp.sdk.generate.AuthInformation;
import com.cnp.sdk.generate.AuthReversal;
import com.cnp.sdk.generate.AuthReversalResponse;
import com.cnp.sdk.generate.Authentication;
import com.cnp.sdk.generate.Authorization;
import com.cnp.sdk.generate.AuthorizationResponse;
import com.cnp.sdk.generate.BalanceInquiry;
import com.cnp.sdk.generate.BalanceInquiryResponse;
import com.cnp.sdk.generate.CancelSubscription;
import com.cnp.sdk.generate.CancelSubscriptionResponse;
import com.cnp.sdk.generate.Capture;
import com.cnp.sdk.generate.CaptureGivenAuth;
import com.cnp.sdk.generate.CaptureGivenAuthResponse;
import com.cnp.sdk.generate.CaptureResponse;
import com.cnp.sdk.generate.CardType;
import com.cnp.sdk.generate.Contact;
import com.cnp.sdk.generate.CountryTypeEnum;
import com.cnp.sdk.generate.CreateAddOnType;
import com.cnp.sdk.generate.CreatePlan;
import com.cnp.sdk.generate.CreatePlanResponse;
import com.cnp.sdk.generate.Credit;
import com.cnp.sdk.generate.CreditResponse;
import com.cnp.sdk.generate.CustomerInfo;
import com.cnp.sdk.generate.Deactivate;
import com.cnp.sdk.generate.DeactivateResponse;
import com.cnp.sdk.generate.DeactivateReversal;
import com.cnp.sdk.generate.DeactivateReversalResponse;
import com.cnp.sdk.generate.DepositReversal;
import com.cnp.sdk.generate.DepositReversalResponse;
import com.cnp.sdk.generate.EcheckAccountTypeEnum;
import com.cnp.sdk.generate.EcheckCredit;
import com.cnp.sdk.generate.EcheckCreditResponse;
import com.cnp.sdk.generate.EcheckRedeposit;
import com.cnp.sdk.generate.EcheckRedepositResponse;
import com.cnp.sdk.generate.EcheckSale;
import com.cnp.sdk.generate.EcheckSalesResponse;
import com.cnp.sdk.generate.EcheckType;
import com.cnp.sdk.generate.EcheckVerification;
import com.cnp.sdk.generate.EcheckVerificationResponse;
import com.cnp.sdk.generate.EcheckVoid;
import com.cnp.sdk.generate.EcheckVoidResponse;
import com.cnp.sdk.generate.ForceCapture;
import com.cnp.sdk.generate.ForceCaptureResponse;
import com.cnp.sdk.generate.FraudCheck;
import com.cnp.sdk.generate.FraudCheckResponse;
import com.cnp.sdk.generate.GiftCardAuthReversal;
import com.cnp.sdk.generate.GiftCardAuthReversalResponse;
import com.cnp.sdk.generate.GiftCardCapture;
import com.cnp.sdk.generate.GiftCardCaptureResponse;
import com.cnp.sdk.generate.GiftCardCardType;
import com.cnp.sdk.generate.GiftCardCredit;
import com.cnp.sdk.generate.GiftCardCreditResponse;
import com.cnp.sdk.generate.CnpOnlineRequest;
import com.cnp.sdk.generate.Load;
import com.cnp.sdk.generate.LoadResponse;
import com.cnp.sdk.generate.LoadReversal;
import com.cnp.sdk.generate.LoadReversalResponse;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.cnp.sdk.generate.OrderSourceType;
import com.cnp.sdk.generate.PayFacCredit;
import com.cnp.sdk.generate.PayFacCreditResponse;
import com.cnp.sdk.generate.QueryTransaction;
import com.cnp.sdk.generate.QueryTransactionResponse;
import com.cnp.sdk.generate.QueryTransactionUnavailableResponse;
import com.cnp.sdk.generate.RecurringRequestType;
import com.cnp.sdk.generate.RecurringSubscriptionType;
import com.cnp.sdk.generate.RefundReversal;
import com.cnp.sdk.generate.RefundReversalResponse;
import com.cnp.sdk.generate.RegisterTokenRequestType;
import com.cnp.sdk.generate.RegisterTokenResponse;
import com.cnp.sdk.generate.Sale;
import com.cnp.sdk.generate.SaleResponse;
import com.cnp.sdk.generate.SepaDirectDebitType;
import com.cnp.sdk.generate.TransactionTypeWithReportGroup;
import com.cnp.sdk.generate.Unload;
import com.cnp.sdk.generate.UnloadResponse;
import com.cnp.sdk.generate.UnloadReversal;
import com.cnp.sdk.generate.UnloadReversalResponse;
import com.cnp.sdk.generate.UpdatePlan;
import com.cnp.sdk.generate.UpdatePlanResponse;
import com.cnp.sdk.generate.UpdateSubscription;
import com.cnp.sdk.generate.UpdateSubscriptionResponse;
import com.cnp.sdk.generate.Wallet;
import com.cnp.sdk.generate.WalletSourceType;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestCnpOnline {

	private CnpOnline cnp;

	@Before
	public void before() throws Exception {
		cnp = new CnpOnline();
	}

	@Test
	public void testAuth() throws Exception {

		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><authorizationResponse><cnpTxnId>123</cnpTxnId></authorizationResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		AuthorizationResponse authorize = cnp.authorize(authorization);
		assertEquals(123L, authorize.getCnpTxnId());
	}
	
	@Test
    public void testAuthWithApplepayAndSecondaryAmountAndWallet() throws Exception {

        Authorization authorization = new Authorization();
        authorization.setReportGroup("Planets");
        authorization.setOrderId("12344");
        authorization.setAmount(106L);
        authorization.setSecondaryAmount(10L);
        authorization.setOrderSource(OrderSourceType.ECOMMERCE);
        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType.setApplicationData("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData("user");
        applepayType.setSignature("sign");
        applepayType.setVersion("1");
        authorization.setApplepay(applepayType);
        
        Wallet wallet = new Wallet();
        wallet.setWalletSourceType(WalletSourceType.MASTER_PASS);
        wallet.setWalletSourceTypeId("123");
        authorization.setWallet(wallet);
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                .requestToServer(
                        matches(".*?<cnpOnlineRequest.*?<authorization.*?<secondaryAmount>10</secondaryAmount>.*?<applepay>.*?<data>user</data>.*?</applepay>.*?<wallet>.*?<walletSourceTypeId>123</walletSourceTypeId>.*?</wallet>.*?</authorization>.*?"),
                        any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><authorizationResponse><cnpTxnId>123</cnpTxnId><applepayResponse><applicationPrimaryAccountNumber>123455</applicationPrimaryAccountNumber><transactionAmount>106</transactionAmount></applepayResponse></authorizationResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        AuthorizationResponse authorize = cnp.authorize(authorization);
        assertEquals(123L, authorize.getCnpTxnId());
        assertEquals("123455", authorize.getApplepayResponse().getApplicationPrimaryAccountNumber());
        assertEquals(new Long(106), authorize.getApplepayResponse().getTransactionAmount());
    }

	@Test
	public void testAuthWithOverrides() throws Exception {

		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?merchantId=\"9001\".*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><authorizationResponse><cnpTxnId>123</cnpTxnId></authorizationResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		CnpOnlineRequest overrides = new CnpOnlineRequest();
		overrides.setMerchantId("9001");
		AuthorizationResponse authorize = cnp.authorize(authorization, overrides);
		assertEquals(123L, authorize.getCnpTxnId());
	}

	@Test
	public void testAuthReversal() throws Exception {

		AuthReversal reversal = new AuthReversal();
		reversal.setCnpTxnId(12345678000L);
		reversal.setAmount(106L);
		reversal.setPayPalNotes("Notes");


		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<authReversal.*?<cnpTxnId>12345678000</cnpTxnId>.*?</authReversal>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><authReversalResponse><cnpTxnId>123</cnpTxnId></authReversalResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		AuthReversalResponse authreversal = cnp.authReversal(reversal);
		assertEquals(123L, authreversal.getCnpTxnId());
	}

	@Test
	public void testAuthReversalWithOverrides() throws Exception {

		AuthReversal reversal = new AuthReversal();
		reversal.setCnpTxnId(12345678000L);
		reversal.setAmount(106L);
		reversal.setPayPalNotes("Notes");


		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?merchantId=\"54321\".*?<authReversal.*?<cnpTxnId>12345678000</cnpTxnId>.*?</authReversal>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.11' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><authReversalResponse><cnpTxnId>123</cnpTxnId></authReversalResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		CnpOnlineRequest overrides = new CnpOnlineRequest();
		overrides.setMerchantId("54321");
		AuthReversalResponse authreversal = cnp.authReversal(reversal, overrides);
		assertEquals(123L, authreversal.getCnpTxnId());
	}


	@Test
	public void testCapture() throws Exception {

		Capture capture = new Capture();
		capture.setCnpTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPayPalNotes("Notes");

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<capture.*?<cnpTxnId>123456000</cnpTxnId>.*?</capture>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><captureResponse><cnpTxnId>123</cnpTxnId></captureResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		CaptureResponse captureresponse = cnp.capture(capture);
		assertEquals(123L, captureresponse.getCnpTxnId());
	}

	@Test
	public void testCaptureWithOverrides() throws Exception {

		Capture capture = new Capture();
		capture.setCnpTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPayPalNotes("Notes");
		
		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<password>supersecret</password>.*?<capture.*?<cnpTxnId>123456000</cnpTxnId>.*?</capture>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><captureResponse><cnpTxnId>123</cnpTxnId></captureResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		CnpOnlineRequest overrides = new CnpOnlineRequest();
		overrides.setAuthentication(new Authentication());
		overrides.getAuthentication().setPassword("supersecret");
		CaptureResponse captureresponse = cnp.capture(capture, overrides);
		assertEquals(123L, captureresponse.getCnpTxnId());
	}


	@Test
	public void testCaptureGivenAuth() throws Exception {

		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setSecondaryAmount(10L);
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
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
							matches(".*?<cnpOnlineRequest.*?<captureGivenAuth.*?<secondaryAmount>10</secondaryAmount>.*?<card>.*?<number>4100000000000001</number>.*?</card>.*?</captureGivenAuth>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><captureGivenAuthResponse><cnpTxnId>123</cnpTxnId></captureGivenAuthResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		CaptureGivenAuthResponse capturegivenauthresponse = cnp.captureGivenAuth(capturegivenauth);
		assertEquals(123L, capturegivenauthresponse.getCnpTxnId());
	}

	@Test
	public void testCaptureGivenAuthWithOverrides() throws Exception {

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
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
							matches(".*?<cnpOnlineRequest.*?<user>neweruser</user>.*?<captureGivenAuth.*?<card>.*?<number>4100000000000001</number>.*?</card>.*?</captureGivenAuth>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><captureGivenAuthResponse><cnpTxnId>123</cnpTxnId></captureGivenAuthResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		CnpOnlineRequest overrides = new CnpOnlineRequest();
		overrides.setAuthentication(new Authentication());
		overrides.getAuthentication().setUser("neweruser");
		CaptureGivenAuthResponse capturegivenauthresponse = cnp.captureGivenAuth(capturegivenauth, overrides);
		assertEquals(123L, capturegivenauthresponse.getCnpTxnId());
	}


	@Test
	public void testCredit() throws Exception {

		Credit credit = new Credit();
		credit.setAmount(106L);
        credit.setSecondaryAmount(10L);
		credit.setOrderId("12344");
		credit.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		credit.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<credit.*?<secondaryAmount>10</secondaryAmount>.*?<card>.*?<number>4100000000000001</number>.*?</card>.*?</credit>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><creditResponse><cnpTxnId>123</cnpTxnId></creditResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		CreditResponse creditresponse = cnp.credit(credit);
		assertEquals(123L, creditresponse.getCnpTxnId());
	}

	@Test
	public void testEcheckCredit() throws Exception {
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(12L);
        echeckcredit.setSecondaryAmount(10L);
		echeckcredit.setCnpTxnId(123456789101112L);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<echeckCredit.*?<cnpTxnId>123456789101112</cnpTxnId>.*?<secondaryAmount>10</secondaryAmount>.*?</echeckCredit>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><echeckCreditResponse><cnpTxnId>123</cnpTxnId></echeckCreditResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		EcheckCreditResponse echeckcreditresponse = cnp.echeckCredit(echeckcredit);
		assertEquals(123L, echeckcreditresponse.getCnpTxnId());
	}

	@Test
	public void testEcheckRedeposit() throws Exception {
		EcheckRedeposit echeckredeposit = new EcheckRedeposit();
		echeckredeposit.setCnpTxnId(123456L);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<echeckRedeposit.*?<cnpTxnId>123456</cnpTxnId>.*?</echeckRedeposit>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><echeckRedepositResponse><cnpTxnId>123</cnpTxnId></echeckRedepositResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		EcheckRedepositResponse echeckredepositresponse = cnp.echeckRedeposit(echeckredeposit);
		assertEquals(123L, echeckredepositresponse.getCnpTxnId());
	}

	@Test
	public void testEcheckSale() throws Exception {
		EcheckSale echecksale = new EcheckSale();
		echecksale.setAmount(123456L);
        echecksale.setSecondaryAmount(10L);
		echecksale.setOrderId("12345");
		echecksale.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeck.setCcdPaymentInformation("1234567890");
		echecksale.setEcheck(echeck);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("cnp.com");
		echecksale.setBillToAddress(contact);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<echeckSale.*?<secondaryAmount>10</secondaryAmount>.*?<echeck>.*?<accNum>12345657890</accNum>.*?</echeck>.*?</echeckSale>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><echeckSalesResponse><cnpTxnId>123</cnpTxnId></echeckSalesResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		EcheckSalesResponse echecksaleresponse = cnp.echeckSale(echecksale);
		assertEquals(123L, echecksaleresponse.getCnpTxnId());
	}

	@Test
	public void testEcheckVerification() throws Exception {
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

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<echeckVerification.*?<echeck>.*?<accNum>12345657890</accNum>.*?</echeck>.*?</echeckVerification>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><echeckVerificationResponse><cnpTxnId>123</cnpTxnId></echeckVerificationResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		EcheckVerificationResponse echeckverificationresponse = cnp.echeckVerification(echeckverification);
		assertEquals(123L, echeckverificationresponse.getCnpTxnId());
	}

	@Test
	public void testForceCapture() throws Exception {
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setSecondaryAmount(10L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		forcecapture.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<forceCapture.*?<secondaryAmount>10</secondaryAmount>.*?<card>.*?<number>4100000000000001</number>.*?</card>.*?</forceCapture>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><forceCaptureResponse><cnpTxnId>123</cnpTxnId></forceCaptureResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		ForceCaptureResponse forcecaptureresponse = cnp.forceCapture(forcecapture);
		assertEquals(123L, forcecaptureresponse.getCnpTxnId());
	}

	@Test
	public void testSale() throws Exception {
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<sale.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</sale>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><saleResponse><cnpTxnId>123</cnpTxnId></saleResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		SaleResponse saleresponse = cnp.sale(sale);
		assertEquals(123L, saleresponse.getCnpTxnId());
	}
	
	@Test
	public void testSale_withAndroidpay() throws Exception {
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ANDROIDPAY);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<sale reportGroup=\"Default Report Group\">"
										+ "<cnpTxnId>123456</cnpTxnId>"
										+ "<orderId>12344</orderId>"
										+ "<amount>106</amount>"
										+ "<orderSource>androidpay</orderSource>"
										+ "<card>"
										+ "<type>VI</type>"
										+ "<number>4100000000000002</number>"
										+ "<expDate>1210</expDate>"
										+ "</card>"
										+ "</sale>"
										+ "</cnpOnlineRequest>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><saleResponse><cnpTxnId>123</cnpTxnId></saleResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		SaleResponse saleresponse = cnp.sale(sale);
		assertEquals(123L, saleresponse.getCnpTxnId());
	}
	
	@Test
	public void testSale_withSepa() throws Exception {
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		SepaDirectDebitType sepa = new SepaDirectDebitType();
		sepa.setMandateProvider("Vantiv");
		sepa.setSequenceType("OneTime");
		sepa.setMandateReference("1234567890");
		sepa.setMandateUrl("http://mandate.url");
		sepa.setMandateSignatureDate(new GregorianCalendar());
		sepa.setIban("Iban");
		sepa.setPreferredLanguage(CountryTypeEnum.CH);
		sale.setSepaDirectDebit(sepa);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<sale reportGroup=\"Default Report Group\">"
										+ "<cnpTxnId>123456</cnpTxnId>"
										+ "<orderId>12344</orderId>"
										+ "<amount>106</amount>"
										+ "<orderSource>ecommerce</orderSource>"
										+ "<sepaDirectDebit>"
										+ "<mandateProvider>Vantiv</mandateProvider>"
										+ "<sequenceType>OneTime</sequenceType>"
										+ "<mandateReference>1234567890</mandateReference>"
										+ "<mandateUrl>http://mandate.url</mandateUrl>"
										+ "<mandateSignatureDate>.*?</mandateSignatureDate>"
										+ "<iban>Iban</iban>"
										+ "<preferredLanguage>CH</preferredLanguage>"
										+ "</sepaDirectDebit>"
										+ "</sale>"
										+ "</cnpOnlineRequest>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><saleResponse><cnpTxnId>123</cnpTxnId></saleResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		SaleResponse saleresponse = cnp.sale(sale);
		assertEquals(123L, saleresponse.getCnpTxnId());
	}
	
	@Test
    public void testSaleWithApplepayAndSecondaryAmountAndWallet() throws Exception {
        Sale sale = new Sale();
        sale.setAmount(106L);
        sale.setSecondaryAmount(10L);
        sale.setCnpTxnId(123456L);
        sale.setOrderId("12344");
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType.setApplicationData("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData("user");
        applepayType.setSignature("sign");
        applepayType.setVersion("1");
        sale.setApplepay(applepayType);

        Wallet wallet = new Wallet();
        wallet.setWalletSourceType(WalletSourceType.MASTER_PASS);
        wallet.setWalletSourceTypeId("123");
        sale.setWallet(wallet);
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<sale.*?<secondaryAmount>10</secondaryAmount>.*?<applepay>.*?<data>user</data>.*?</applepay>.*?<wallet>.*?<walletSourceTypeId>123</walletSourceTypeId>.*?</wallet>.*?</sale>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><saleResponse><cnpTxnId>123</cnpTxnId><applepayResponse><applicationPrimaryAccountNumber>123455</applicationPrimaryAccountNumber></applepayResponse></saleResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        SaleResponse saleresponse = cnp.sale(sale);
        assertEquals(123L, saleresponse.getCnpTxnId());
        assertEquals("123455", saleresponse.getApplepayResponse().getApplicationPrimaryAccountNumber());
    }

	@Test
	public void testToken() throws Exception {
		RegisterTokenRequestType token = new RegisterTokenRequestType();
		token.setOrderId("12344");
		token.setAccountNumber("1233456789103801");

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<registerTokenRequest.*?<accountNumber>1233456789103801</accountNumber>.*?</registerTokenRequest>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><registerTokenResponse><cnpTxnId>123</cnpTxnId></registerTokenResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		RegisterTokenResponse registertokenresponse = cnp.registerToken(token);
		assertEquals(123L, registertokenresponse.getCnpTxnId());
	}
	
	@Test
    public void testTokenWithApplepay() throws Exception {
        RegisterTokenRequestType token = new RegisterTokenRequestType();
        token.setOrderId("12344");
        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType.setApplicationData("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData("user");
        applepayType.setSignature("sign");
        applepayType.setVersion("1");
        token.setApplepay(applepayType);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<registerTokenRequest.*?<applepay>.*?<data>user</data>.*?</applepay>.*?</registerTokenRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><registerTokenResponse><cnpTxnId>123</cnpTxnId><applepayResponse><applicationPrimaryAccountNumber>123455</applicationPrimaryAccountNumber></applepayResponse></registerTokenResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        RegisterTokenResponse registertokenresponse = cnp.registerToken(token);
        assertEquals(123L, registertokenresponse.getCnpTxnId());
        assertEquals("123455", registertokenresponse.getApplepayResponse().getApplicationPrimaryAccountNumber());
    }

	@Test
	public void testCnpOnlineException() throws Exception {

		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='1' message='Error validating xml data against the schema' xmlns='http://www.vantivcnp.com/schema'><authorizationResponse><cnpTxnId>123</cnpTxnId></authorizationResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		try{
		cnp.authorize(authorization);
		fail("Expected Exception");
		} catch(CnpOnlineException e){
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
	}

	@Test
	public void testJAXBException() throws Exception {

		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class)))
				.thenReturn(
						"no xml");
		cnp.setCommunication(mockedCommunication);
		try{
		cnp.authorize(authorization);
		fail("Expected Exception");
		} catch(CnpOnlineException e){
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
	}

	@Test
	public void testDefaultReportGroup() throws Exception {


		Authorization authorization = new Authorization();
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<authorization.*? reportGroup=\"Default Report Group\">.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><authorizationResponse reportGroup='Default Report Group'></authorizationResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		AuthorizationResponse authorize = cnp.authorize(authorization);
		assertEquals("Default Report Group", authorize.getReportGroup());
	}

	@Test
	public void testOverrideLoggedInUser() throws Exception {
		Properties config = new Properties();
		config.setProperty("loggedInUser", "avig");
		cnp = new CnpOnline(config);
		Authorization authorization = new Authorization();
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?loggedInUser=\"avig\".*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><authorizationResponse reportGroup='Default Report Group'></authorizationResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		AuthorizationResponse authorize = cnp.authorize(authorization);
		assertEquals("Default Report Group", authorize.getReportGroup());
	}


	@Test
	public void testEcheckVoid() throws Exception {
		EcheckVoid echeckvoid = new EcheckVoid();
		echeckvoid.setCnpTxnId(12345L);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<echeckVoid.*?<cnpTxnId>12345</cnpTxnId>.*?</echeckVoid>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><echeckVoidResponse><cnpTxnId>123</cnpTxnId></echeckVoidResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		EcheckVoidResponse echeckvoidresponse = cnp.echeckVoid(echeckvoid);
		assertEquals(123L, echeckvoidresponse.getCnpTxnId());
	}

	@Test
	public void test_CustomerInfo_dob() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);
		CustomerInfo customerInfo = new CustomerInfo();
		Calendar c = Calendar.getInstance();
		c.set(1980, Calendar.APRIL, 14);
		customerInfo.setDob(c);
		authorization.setCustomerInfo(customerInfo);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<cnpOnlineRequest.*?<authorization.*?<dob>1980-04-14</dob>.*?</authorization>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<cnpOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><authorizationResponse><cnpTxnId>123</cnpTxnId></authorizationResponse></cnpOnlineResponse>");
		cnp.setCommunication(mockedCommunication);
		AuthorizationResponse authorize = cnp.authorize(authorization);
		assertEquals(123L, authorize.getCnpTxnId());
	}

	@Test(expected=CnpOnlineException.class)
	public void testSendToCnpNamespaceHotswap() {
	    Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                any(String.class),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version=\"1.0\" xmlns=\"http://www.vantivcnp.com/schema/online\" response=\"1\" message=\"System Error - Call Cnp &amp; Co.\"></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);


        CardType card = new CardType();
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        card.setType(MethodOfPaymentTypeEnum.VI);

        Sale sale = new Sale();
        sale.setReportGroup("Planets");
        sale.setOrderId("12344");
        sale.setAmount(6000L);
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        sale.setCard(card);

        cnp.sale(sale);
	}

    @Test
    public void testCancelSubscription() throws Exception {
        CancelSubscription cancel = new CancelSubscription();
        cancel.setSubscriptionId(12345L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<cancelSubscription><subscriptionId>12345</subscriptionId></cancelSubscription></cnpOnlineRequest>*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><cancelSubscriptionResponse><subscriptionId>12345</subscriptionId></cancelSubscriptionResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CancelSubscriptionResponse cancelResponse = cnp.cancelSubscription(cancel);
        assertEquals(12345L, cancelResponse.getSubscriptionId());
    }

    @Test
    public void testCancelSubscriptionWithOverrides() throws Exception {

        CancelSubscription cancel = new CancelSubscription();
        cancel.setSubscriptionId(12345L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"9001\".*?<cancelSubscription><subscriptionId>12345</subscriptionId></cancelSubscription></cnpOnlineRequest>*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><cancelSubscriptionResponse><subscriptionId>12345</subscriptionId></cancelSubscriptionResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("9001");
        CancelSubscriptionResponse cancelResponse = cnp.cancelSubscription(cancel, overrides);
        assertEquals(12345L, cancelResponse.getSubscriptionId());
    }

    @Test
    public void testUpdateSubscription() throws Exception {
        UpdateSubscription update = new UpdateSubscription();
        Calendar c = Calendar.getInstance();
        c.set(2013, Calendar.AUGUST, 7);
        update.setBillingDate(c);
        Contact billToAddress = new Contact();
        billToAddress.setName("Greg Dake");
        billToAddress.setCity("Lowell");
        billToAddress.setState("MA");
        billToAddress.setEmail("sdksupport@cnp.com");
        update.setBillToAddress(billToAddress);
        CardType card = new CardType();
        card.setNumber("4100000000000001");
        card.setExpDate("1215");
        card.setType(MethodOfPaymentTypeEnum.VI);
        update.setCard(card);
        update.setPlanCode("abcdefg");
        update.setSubscriptionId(12345L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<updateSubscription><subscriptionId>12345</subscriptionId><planCode>abcdefg</planCode><billToAddress><name>Greg Dake</name><city>Lowell</city><state>MA</state><email>sdksupport@cnp.com</email></billToAddress><card><type>VI</type><number>4100000000000001</number><expDate>1215</expDate></card><billingDate>2013-08-07</billingDate></updateSubscription></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><updateSubscriptionResponse><subscriptionId>12345</subscriptionId></updateSubscriptionResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        UpdateSubscriptionResponse updateResponse = cnp.updateSubscription(update);
        assertEquals(12345L, updateResponse.getSubscriptionId());
    }

    @Test
    public void testUpdateSubscriptionWithOverrides() throws Exception {
        UpdateSubscription update = new UpdateSubscription();
        Calendar c = Calendar.getInstance();
        c.set(2013, Calendar.AUGUST, 7);
        update.setBillingDate(c);
        Contact billToAddress = new Contact();
        billToAddress.setName("Greg Dake");
        billToAddress.setCity("Lowell");
        billToAddress.setState("MA");
        billToAddress.setEmail("sdksupport@cnp.com");
        update.setBillToAddress(billToAddress);
        CardType card = new CardType();
        card.setNumber("4100000000000001");
        card.setExpDate("1215");
        card.setType(MethodOfPaymentTypeEnum.VI);
        update.setCard(card);
        update.setPlanCode("abcdefg");
        update.setSubscriptionId(12345L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?<updateSubscription><subscriptionId>12345</subscriptionId><planCode>abcdefg</planCode><billToAddress><name>Greg Dake</name><city>Lowell</city><state>MA</state><email>sdksupport@cnp.com</email></billToAddress><card><type>VI</type><number>4100000000000001</number><expDate>1215</expDate></card><billingDate>2013-08-07</billingDate></updateSubscription></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><updateSubscriptionResponse><subscriptionId>12345</subscriptionId></updateSubscriptionResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        UpdateSubscriptionResponse updateResponse = cnp.updateSubscription(update, overrides);
        assertEquals(12345L, updateResponse.getSubscriptionId());
    }

    @Test
    public void testUpdatePlan() throws Exception {
        UpdatePlan update = new UpdatePlan();
        update.setActive(true);
        update.setPlanCode("abc");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<updatePlan><planCode>abc</planCode><active>true</active></updatePlan></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><updatePlanResponse><planCode>abc</planCode></updatePlanResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        UpdatePlanResponse updateResponse = cnp.updatePlan(update);
        assertEquals("abc", updateResponse.getPlanCode());
    }

    @Test
    public void testUpdatePlanWithOverrides() throws Exception {
        UpdatePlan update = new UpdatePlan();
        update.setActive(true);
        update.setPlanCode("abc");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?<updatePlan><planCode>abc</planCode><active>true</active></updatePlan></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><updatePlanResponse><planCode>abc</planCode></updatePlanResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        UpdatePlanResponse updateResponse = cnp.updatePlan(update, overrides);
        assertEquals("abc", updateResponse.getPlanCode());
    }

    @Test
    public void testCreatePlan() throws Exception {
        CreatePlan create = new CreatePlan();
        create.setPlanCode("abc");
        create.setActive(true);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<createPlan><planCode>abc</planCode><active>true</active></createPlan></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><createPlanResponse><planCode>abc</planCode></createPlanResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CreatePlanResponse createResponse = cnp.createPlan(create);
        assertEquals("abc", createResponse.getPlanCode());
    }

    @Test
    public void testCreatePlanWithOverrides() throws Exception {
        CreatePlan create = new CreatePlan();
        create.setPlanCode("abc");
        create.setActive(true);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?<createPlan><planCode>abc</planCode><active>true</active></createPlan></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><createPlanResponse><planCode>abc</planCode></createPlanResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        CreatePlanResponse createResponse = cnp.createPlan(create, overrides);
        assertEquals("abc", createResponse.getPlanCode());
    }

    @Test
    public void testActivate() throws Exception {
        Activate activate = new Activate();
        activate.setAmount(100L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<activate><amount>100</amount></activate></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><activateResponse><cnpTxnId>123456</cnpTxnId></activateResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        ActivateResponse response = cnp.activate(activate);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testActivateWithOverrides() throws Exception {
        Activate activate = new Activate();
        activate.setAmount(100L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?<activate><amount>100</amount></activate></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><activateResponse><cnpTxnId>123456</cnpTxnId></activateResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        ActivateResponse response = cnp.activate(activate, overrides);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testDectivate() throws Exception {
        Deactivate deactivate = new Deactivate();
        deactivate.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<deactivate><orderId>123</orderId></deactivate></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><deactivateResponse><cnpTxnId>123456</cnpTxnId></deactivateResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        DeactivateResponse response = cnp.deactivate(deactivate);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testDeactivateWithOverrides() throws Exception {
        Deactivate deactivate = new Deactivate();
        deactivate.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?<deactivate><orderId>123</orderId></deactivate></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><deactivateResponse><cnpTxnId>123456</cnpTxnId></deactivateResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        DeactivateResponse response = cnp.deactivate(deactivate, overrides);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testLoad() throws Exception {
        Load load = new Load();
        load.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<load><orderId>123</orderId></load></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><loadResponse><cnpTxnId>123456</cnpTxnId></loadResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        LoadResponse response = cnp.load(load);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testLoadWithOverrides() throws Exception {
        Load load = new Load();
        load.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?<load><orderId>123</orderId></load></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><loadResponse><cnpTxnId>123456</cnpTxnId></loadResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        LoadResponse response = cnp.load(load, overrides);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testUnload() throws Exception {
        Unload unload = new Unload();
        unload.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<unload><orderId>123</orderId></unload></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><unloadResponse><cnpTxnId>123456</cnpTxnId></unloadResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        UnloadResponse response = cnp.unload(unload);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testUnloadWithOverrides() throws Exception {
        Unload unload = new Unload();
        unload.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?<unload><orderId>123</orderId></unload></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><unloadResponse><cnpTxnId>123456</cnpTxnId></unloadResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        UnloadResponse response = cnp.unload(unload, overrides);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testBalanceInquiry() throws Exception {
        BalanceInquiry balanceInquiry = new BalanceInquiry();
        balanceInquiry.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<balanceInquiry><orderId>123</orderId></balanceInquiry></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><balanceInquiryResponse><cnpTxnId>123456</cnpTxnId></balanceInquiryResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        BalanceInquiryResponse response = cnp.balanceInquiry(balanceInquiry);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testBalanceInquiryWithOverrides() throws Exception {
        BalanceInquiry balanceInquiry = new BalanceInquiry();
        balanceInquiry.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?<balanceInquiry><orderId>123</orderId></balanceInquiry></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><balanceInquiryResponse><cnpTxnId>123456</cnpTxnId></balanceInquiryResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        BalanceInquiryResponse response = cnp.balanceInquiry(balanceInquiry, overrides);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testActivateReversal() throws Exception {
        ActivateReversal activateReversal = new ActivateReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0502");
        giftCard.setNumber("5400000000000000");
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setPin("1234");
        activateReversal.setCnpTxnId(123L);
        activateReversal.setCard(giftCard);
        activateReversal.setId("id");
        activateReversal.setOriginalRefCode("3");
        activateReversal.setOriginalSequenceNumber("999999");
        activateReversal.setOriginalSystemTraceId(5);
        activateReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl());

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<activateReversal id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<card>"
                                		+ "<type>GC</type>"
                                		+ "<number>5400000000000000</number>"
                                		+ "<expDate>0502</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>1234</pin>"
                                		+ "</card>"
                                		+ "<originalRefCode>3</originalRefCode>"
                                		+ "<originalTxnTime></originalTxnTime>"
                                		+ "<originalSystemTraceId>5</originalSystemTraceId>"
                                		+ "<originalSequenceNumber>999999</originalSequenceNumber>"
                                		+ "</activateReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><activateReversalResponse><cnpTxnId>123456</cnpTxnId></activateReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        ActivateReversalResponse response = cnp.activateReversal(activateReversal);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testActivateReversalWithOverrides() throws Exception {
        ActivateReversal activateReversal = new ActivateReversal();
        activateReversal.setCnpTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?"
                                		+ "<activateReversal>"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<originalSystemTraceId>0</originalSystemTraceId>"
                                		+ "</activateReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><activateReversalResponse><cnpTxnId>123456</cnpTxnId></activateReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        ActivateReversalResponse response = cnp.activateReversal(activateReversal, overrides);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testDeactivateReversal() throws Exception {
        DeactivateReversal deactivateReversal = new DeactivateReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0502");
        giftCard.setNumber("5400000000000000");
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setPin("1234");
        deactivateReversal.setCnpTxnId(123L);
        deactivateReversal.setCard(giftCard);
        deactivateReversal.setId("id");
        deactivateReversal.setOriginalRefCode("3");
        deactivateReversal.setOriginalSequenceNumber("999999");
        deactivateReversal.setOriginalSystemTraceId(5);
        deactivateReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl());

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<deactivateReversal id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<card>"
                                		+ "<type>GC</type>"
                                		+ "<number>5400000000000000</number>"
                                		+ "<expDate>0502</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>1234</pin>"
                                		+ "</card>"
                                		+ "<originalRefCode>3</originalRefCode>"
                                		+ "<originalTxnTime></originalTxnTime>"
                                		+ "<originalSystemTraceId>5</originalSystemTraceId>"
                                		+ "<originalSequenceNumber>999999</originalSequenceNumber>"
                                		+ "</deactivateReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><deactivateReversalResponse><cnpTxnId>123456</cnpTxnId></deactivateReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        DeactivateReversalResponse response = cnp.deactivateReversal(deactivateReversal);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testDeActivateReversalWithOverrides() throws Exception {
        DeactivateReversal deactivateReversal = new DeactivateReversal();
        deactivateReversal.setCnpTxnId(123l);


        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest merchantId=\"905\" merchantSdk=.*version=\"12.0\" xmlns=\"http://www.vantivcnp.com/schema\">.*?"
                                		+ "<deactivateReversal>"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<originalSystemTraceId>0</originalSystemTraceId>"
                                		+ "</deactivateReversal"
                                		+ "></cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><deactivateReversalResponse><cnpTxnId>123456</cnpTxnId></deactivateReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        DeactivateReversalResponse response = cnp.deactivateReversal(deactivateReversal, overrides);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testLoadReversal() throws Exception {
        LoadReversal loadReversal = new LoadReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0502");
        giftCard.setNumber("5400000000000000");
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setPin("1234");
        loadReversal.setCnpTxnId(123L);
        loadReversal.setCard(giftCard);
        loadReversal.setId("id");
        loadReversal.setOriginalAmount(45l);
        loadReversal.setOriginalRefCode("3");
        loadReversal.setOriginalSequenceNumber("999999");
        loadReversal.setOriginalSystemTraceId(5);
        loadReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl());

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<loadReversal id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<card>"
                                		+ "<type>GC</type>"
                                		+ "<number>5400000000000000</number>"
                                		+ "<expDate>0502</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>1234</pin>"
                                		+ "</card>"
                                		+ "<originalRefCode>3</originalRefCode>"
                                		+ "<originalAmount>45</originalAmount>"
                                		+ "<originalTxnTime></originalTxnTime>"
                                		+ "<originalSystemTraceId>5</originalSystemTraceId>"
                                		+ "<originalSequenceNumber>999999</originalSequenceNumber>"
                                		+ "</loadReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><loadReversalResponse><cnpTxnId>123456</cnpTxnId></loadReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        LoadReversalResponse response = cnp.loadReversal(loadReversal);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testLoadReversalWithOverrides() throws Exception {
        LoadReversal loadReversal = new LoadReversal();
        loadReversal.setCnpTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?"
                                		+ "<loadReversal>"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<originalSystemTraceId>0</originalSystemTraceId>"
                                		+ "</loadReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><loadReversalResponse><cnpTxnId>123456</cnpTxnId></loadReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        LoadReversalResponse response = cnp.loadReversal(loadReversal, overrides);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testUnLoadReversal() throws Exception {
        UnloadReversal unloadReversal = new UnloadReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0502");
        giftCard.setNumber("5400000000000000");
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setPin("1234");
        unloadReversal.setCnpTxnId(123L);
        unloadReversal.setCard(giftCard);
        unloadReversal.setId("id");
        unloadReversal.setOriginalRefCode("3");
        unloadReversal.setOriginalSequenceNumber("999999");
        unloadReversal.setOriginalSystemTraceId(5);
        unloadReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl());

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<unloadReversal id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<card>"
                                		+ "<type>GC</type>"
                                		+ "<number>5400000000000000</number>"
                                		+ "<expDate>0502</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>1234</pin>"
                                		+ "</card>"
                                		+ "<originalRefCode>3</originalRefCode>"
                                		+ "<originalTxnTime></originalTxnTime>"
                                		+ "<originalSystemTraceId>5</originalSystemTraceId>"
                                		+ "<originalSequenceNumber>999999</originalSequenceNumber>"
                                		+ "</unloadReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><unloadReversalResponse><cnpTxnId>123456</cnpTxnId></unloadReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        UnloadReversalResponse response = cnp.unloadReversal(unloadReversal);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testUnLoadReversalWithOverrides() throws Exception {
        UnloadReversal unloadReversal = new UnloadReversal();
        unloadReversal.setCnpTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?"
                                		+ "<unloadReversal>"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<originalSystemTraceId>0</originalSystemTraceId>"
                                		+ "</unloadReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><unloadReversalResponse><cnpTxnId>123456</cnpTxnId></unloadReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        UnloadReversalResponse response = cnp.unloadReversal(unloadReversal, overrides);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testRefundReversal() throws Exception {
        RefundReversal refundReversal = new RefundReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000001");
        giftCard.setPin("9999");
        giftCard.setTrack("track data goes here");
        
        refundReversal.setCnpTxnId(123L);
        refundReversal.setId("id");
        refundReversal.setReportGroup("rptGrp");
        refundReversal.setCard(giftCard);
        refundReversal.setOriginalRefCode("ref");
        refundReversal.setOriginalAmount(44455l);
        refundReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl());
        refundReversal.setOriginalSystemTraceId(3);
        refundReversal.setOriginalSequenceNumber("222222");
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<refundReversal reportGroup=\"rptGrp\" id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<card>"
                                		+ "<track>track data goes here</track>"
                                		+ "<type>GC</type>"
                                		+ "<number>4100000000000001</number>"
                                		+ "<expDate>0655</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>9999</pin>"
                                		+ "</card><originalRefCode>ref</originalRefCode>"
                                		+ "<originalAmount>44455</originalAmount>"
                                		+ "<originalTxnTime></originalTxnTime>"
                                		+ "<originalSystemTraceId>3</originalSystemTraceId>"
                                		+ "<originalSequenceNumber>222222</originalSequenceNumber>"
                                		+ "</refundReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><refundReversalResponse><cnpTxnId>123456</cnpTxnId></refundReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        RefundReversalResponse response = cnp.refundReversal(refundReversal);
        assertEquals(123456L, response.getCnpTxnId());
    }
    
    @Test
    public void testRefundReversalWithOverrides() throws Exception {
        RefundReversal refundReversal = new RefundReversal();
        refundReversal.setCnpTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?"
                                		+ "<refundReversal>"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<originalSystemTraceId>0</originalSystemTraceId>"
                                		+ "</refundReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><refundReversalResponse><cnpTxnId>123456</cnpTxnId></refundReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        RefundReversalResponse response = cnp.refundReversal(refundReversal, overrides);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testDepositReversal() throws Exception {
        DepositReversal depositReversal = new DepositReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setNumber("400000000000001");
        giftCard.setExpDate("0150");
        giftCard.setCardValidationNum("411");
        giftCard.setPin("1234");
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        
        depositReversal.setCnpTxnId(123L);
        depositReversal.setId("id");
        depositReversal.setReportGroup("Planets");
        depositReversal.setCard(giftCard);
        depositReversal.setOriginalRefCode("101");
        depositReversal.setOriginalAmount(3456l);
        depositReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl());
        depositReversal.setOriginalSystemTraceId(33);
        depositReversal.setOriginalSequenceNumber("111111");
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<depositReversal reportGroup=\"Planets\" id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<card>"
                                		+ "<type>GC</type>"
                                		+ "<number>400000000000001</number>"
                                		+ "<expDate>0150</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>1234</pin>"
                                		+ "</card>"
                                		+ "<originalRefCode>101</originalRefCode>"
                                		+ "<originalAmount>3456</originalAmount>"
                                		+ "<originalTxnTime></originalTxnTime>"
                                		+ "<originalSystemTraceId>33</originalSystemTraceId>"
                                		+ "<originalSequenceNumber>111111</originalSequenceNumber>"
                                		+ "</depositReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><depositReversalResponse><cnpTxnId>123456</cnpTxnId></depositReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        DepositReversalResponse response = cnp.depositReversal(depositReversal);
        assertEquals(123456L, response.getCnpTxnId());
    }

    @Test
    public void testDepositReversalWithOverrides() throws Exception {
        DepositReversal depositReversal = new DepositReversal();
        depositReversal.setCnpTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?"
                                		+ "<depositReversal>"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<originalSystemTraceId>0</originalSystemTraceId>"
                                		+ "</depositReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><depositReversalResponse><cnpTxnId>123456</cnpTxnId></depositReversalResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        DepositReversalResponse response = cnp.depositReversal(depositReversal, overrides);
        assertEquals(123456L, response.getCnpTxnId());
    }
    
    @Test
    public void testGiftCardAuthReversal() throws Exception {
        GiftCardAuthReversal gcAuthReversal = new GiftCardAuthReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000001");
        giftCard.setPin("9999");
        
        gcAuthReversal.setCnpTxnId(123L);
        gcAuthReversal.setId("id");
        gcAuthReversal.setReportGroup("rptGrp");
        gcAuthReversal.setCard(giftCard);
        gcAuthReversal.setOriginalRefCode("ref");
        gcAuthReversal.setOriginalAmount(44455l);
        gcAuthReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl());
        gcAuthReversal.setOriginalSystemTraceId(3);
        gcAuthReversal.setOriginalSequenceNumber("222222");
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<giftCardAuthReversal reportGroup=\"rptGrp\" id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<card>"
                                		+ "<type>GC</type>"
                                		+ "<number>4100000000000001</number>"
                                		+ "<expDate>0655</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>9999</pin>"
                                		+ "</card>"
                                		+ "<originalRefCode>ref</originalRefCode>"
                                		+ "<originalAmount>44455</originalAmount>"
                                		+ "<originalTxnTime></originalTxnTime>"
                                		+ "<originalSystemTraceId>3</originalSystemTraceId>"
                                		+ "<originalSequenceNumber>222222</originalSequenceNumber>"
                                		+ "</giftCardAuthReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version=\"12.0\" xmlns=\"http://www.vantivcnp.com/schema\" response=\"0\" message=\"Valid Format\">"
                        + "<giftCardAuthReversalResponse id=\"id\" reportGroup=\"rptGrp\">"
                        + "<cnpTxnId>21825673457518565</cnpTxnId>"
                        + "<response>330</response>"
                        + "<responseTime>2016-11-21T18:08:48</responseTime>"
                        + "<postDate>2016-11-22</postDate>"
                        + "<message>Invalid Payment Type</message>"
                        + "<giftCardResponse>"
                        + "<txnTime>2016-11-21T13:08:48</txnTime>"
                        + "<systemTraceId>0</systemTraceId>"
                        + "</giftCardResponse>"
                        + "</giftCardAuthReversalResponse>"
                        + "</cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        GiftCardAuthReversalResponse response = cnp.giftCardAuthReversal(gcAuthReversal);
        assertEquals(21825673457518565L, response.getCnpTxnId());
    }
    
    @Test
    public void testGiftCardAuthReversal_withOverrides() throws Exception {
        GiftCardAuthReversal gcAuthReversal = new GiftCardAuthReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000001");
        giftCard.setPin("9999");
        
        gcAuthReversal.setCnpTxnId(123L);
        gcAuthReversal.setId("id");
        gcAuthReversal.setReportGroup("rptGrp");
        gcAuthReversal.setCard(giftCard);
        gcAuthReversal.setOriginalRefCode("ref");
        gcAuthReversal.setOriginalAmount(44455l);
        gcAuthReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl());
        gcAuthReversal.setOriginalSystemTraceId(3);
        gcAuthReversal.setOriginalSequenceNumber("222222");
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?"
                                		+ "<giftCardAuthReversal reportGroup=\"rptGrp\" id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<card>"
                                		+ "<type>GC</type>"
                                		+ "<number>4100000000000001</number>"
                                		+ "<expDate>0655</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>9999</pin>"
                                		+ "</card>"
                                		+ "<originalRefCode>ref</originalRefCode>"
                                		+ "<originalAmount>44455</originalAmount>"
                                		+ "<originalTxnTime></originalTxnTime>"
                                		+ "<originalSystemTraceId>3</originalSystemTraceId>"
                                		+ "<originalSequenceNumber>222222</originalSequenceNumber>"
                                		+ "</giftCardAuthReversal>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version=\"12.0\" xmlns=\"http://www.vantivcnp.com/schema\" response=\"0\" message=\"Valid Format\">"
                        + "<giftCardAuthReversalResponse id=\"id\" reportGroup=\"rptGrp\">"
                        + "<cnpTxnId>21825673457518565</cnpTxnId>"
                        + "<response>330</response>"
                        + "<responseTime>2016-11-21T18:08:48</responseTime>"
                        + "<postDate>2016-11-22</postDate>"
                        + "<message>Invalid Payment Type</message>"
                        + "<giftCardResponse>"
                        + "<txnTime>2016-11-21T13:08:48</txnTime>"
                        + "<systemTraceId>0</systemTraceId>"
                        + "</giftCardResponse>"
                        + "</giftCardAuthReversalResponse>"
                        + "</cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        GiftCardAuthReversalResponse response = cnp.giftCardAuthReversal(gcAuthReversal, overrides);
        assertEquals(21825673457518565L, response.getCnpTxnId());
    }
    
    @Test
    public void testGiftCardCapture() throws Exception {
        GiftCardCapture gcCapture = new GiftCardCapture();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000001");
        giftCard.setPin("9999");
        
        gcCapture.setCnpTxnId(123L);
        gcCapture.setId("id");
        gcCapture.setReportGroup("rptGrp");
        gcCapture.setCaptureAmount(2434l);
        gcCapture.setCard(giftCard);
        gcCapture.setOriginalRefCode("ref");
        gcCapture.setOriginalAmount(44455l);
        gcCapture.setOriginalTxnTime(new XMLGregorianCalendarImpl());
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<giftCardCapture reportGroup=\"rptGrp\" id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<captureAmount>2434</captureAmount>"
                                		+ "<card>"
                                		+ "<type>GC</type>"
                                		+ "<number>4100000000000001</number>"
                                		+ "<expDate>0655</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>9999</pin>"
                                		+ "</card>"
                                		+ "<originalRefCode>ref</originalRefCode>"
                                		+ "<originalAmount>44455</originalAmount>"
                                		+ "<originalTxnTime></originalTxnTime>"
                                		+ "</giftCardCapture>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version=\"12.0\" xmlns=\"http://www.vantivcnp.com/schema\" response=\"0\" message=\"Valid Format\">"
                        + "<giftCardCaptureResponse id=\"id\" reportGroup=\"rptGrp\">"
                        + "<cnpTxnId>21825673457518565</cnpTxnId>"
                        + "<response>330</response>"
                        + "<responseTime>2016-11-21T18:35:55</responseTime>"
                        + "<postDate>2016-11-22</postDate>"
                        + "<message>Invalid Payment Type</message>"
                        + "<giftCardResponse>"
                        + "<txnTime>2016-11-21T13:35:55</txnTime>"
                        + "<systemTraceId>0</systemTraceId>"
                        + "</giftCardResponse>"
                        + "</giftCardCaptureResponse>"
                        + "</cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        GiftCardCaptureResponse response = cnp.giftCardCapture(gcCapture);
        assertEquals(21825673457518565L, response.getCnpTxnId());
    }
    
    @Test
    public void testGiftCardCapture_withOverrides() throws Exception {
    	GiftCardCapture gcCapture = new GiftCardCapture();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000001");
        giftCard.setPin("9999");
        
        gcCapture.setCnpTxnId(123L);
        gcCapture.setId("id");
        gcCapture.setReportGroup("rptGrp");
        gcCapture.setCaptureAmount(2434l);
        gcCapture.setCard(giftCard);
        gcCapture.setOriginalRefCode("ref");
        gcCapture.setOriginalAmount(44455l);
        gcCapture.setOriginalTxnTime(new XMLGregorianCalendarImpl());
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                        		matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?"
                        				+ "<giftCardCapture reportGroup=\"rptGrp\" id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<captureAmount>2434</captureAmount>"
                                		+ "<card>"
                                		+ "<type>GC</type>"
                                		+ "<number>4100000000000001</number>"
                                		+ "<expDate>0655</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>9999</pin>"
                                		+ "</card>"
                                		+ "<originalRefCode>ref</originalRefCode>"
                                		+ "<originalAmount>44455</originalAmount>"
                                		+ "<originalTxnTime></originalTxnTime>"
                                		+ "</giftCardCapture>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                		"<cnpOnlineResponse version=\"12.0\" xmlns=\"http://www.vantivcnp.com/schema\" response=\"0\" message=\"Valid Format\">"
                                + "<giftCardCaptureResponse id=\"id\" reportGroup=\"rptGrp\">"
                                + "<cnpTxnId>21825673457518565</cnpTxnId>"
                                + "<response>330</response>"
                                + "<responseTime>2016-11-21T18:35:55</responseTime>"
                                + "<postDate>2016-11-22</postDate>"
                                + "<message>Invalid Payment Type</message>"
                                + "<giftCardResponse>"
                                + "<txnTime>2016-11-21T13:35:55</txnTime>"
                                + "<systemTraceId>0</systemTraceId>"
                                + "</giftCardResponse>"
                                + "</giftCardCaptureResponse>"
                                + "</cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        GiftCardCaptureResponse response = cnp.giftCardCapture(gcCapture, overrides);
        assertEquals(21825673457518565L, response.getCnpTxnId());
    }
    
    @Test
    public void testGiftCardCredit() throws Exception {
        GiftCardCredit gcCredit = new GiftCardCredit();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000001");
        giftCard.setPin("9999");
        
        gcCredit.setCnpTxnId(123L);
        gcCredit.setId("id");
        gcCredit.setReportGroup("rptGrp");
        gcCredit.setCreditAmount(3399l);
        gcCredit.setCard(giftCard);
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<giftCardCredit reportGroup=\"rptGrp\" id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<creditAmount>3399</creditAmount>"
                                		+ "<card>"
                                		+ "<type>GC</type>"
                                		+ "<number>4100000000000001</number>"
                                		+ "<expDate>0655</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>9999</pin>"
                                		+ "</card>"
                                		+ "</giftCardCredit>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version=\"12.0\" xmlns=\"http://www.vantivcnp.com/schema\" response=\"0\" message=\"Valid Format\">"
                        + "<giftCardCreditResponse id=\"id\" reportGroup=\"rptGrp\">"
                        + "<cnpTxnId>21825673457518565</cnpTxnId>"
                        + "<response>330</response>"
                        + "<responseTime>2016-11-21T18:54:41</responseTime>"
                        + "<postDate>2016-11-22</postDate>"
                        + "<message>Invalid Payment Type</message>"
                        + "<giftCardResponse>"
                        + "<txnTime>2016-11-21T13:54:41</txnTime>"
                        + "<systemTraceId>0</systemTraceId>"
                        + "</giftCardResponse>"
                        + "</giftCardCreditResponse>"
                        + "</cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        GiftCardCreditResponse response = cnp.giftCardCredit(gcCredit);
        assertEquals(21825673457518565L, response.getCnpTxnId());
    }
    
    @Test
    public void testGiftCardCredit_withOverrides() throws Exception {
    	GiftCardCredit gcCredit = new GiftCardCredit();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000001");
        giftCard.setPin("9999");
        
        gcCredit.setCnpTxnId(123L);
        gcCredit.setId("id");
        gcCredit.setReportGroup("rptGrp");
        gcCredit.setCreditAmount(3399l);
        gcCredit.setCard(giftCard);
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                        		matches(".*?<cnpOnlineRequest.*?merchantId=\"905\".*?"
                        				+ "<giftCardCredit reportGroup=\"rptGrp\" id=\"id\">"
                                		+ "<cnpTxnId>123</cnpTxnId>"
                                		+ "<creditAmount>3399</creditAmount>"
                                		+ "<card>"
                                		+ "<type>GC</type>"
                                		+ "<number>4100000000000001</number>"
                                		+ "<expDate>0655</expDate>"
                                		+ "<cardValidationNum>411</cardValidationNum>"
                                		+ "<pin>9999</pin>"
                                		+ "</card>"
                                		+ "</giftCardCredit>"
                                		+ "</cnpOnlineRequest>.*?"),
                                any(Properties.class)))
                .thenReturn(
                		"<cnpOnlineResponse version=\"12.0\" xmlns=\"http://www.vantivcnp.com/schema\" response=\"0\" message=\"Valid Format\">"
                                + "<giftCardCreditResponse id=\"id\" reportGroup=\"rptGrp\">"
                                + "<cnpTxnId>21825673457518565</cnpTxnId>"
                                + "<response>330</response>"
                                + "<responseTime>2016-11-21T18:54:41</responseTime>"
                                + "<postDate>2016-11-22</postDate>"
                                + "<message>Invalid Payment Type</message>"
                                + "<giftCardResponse>"
                                + "<txnTime>2016-11-21T13:54:41</txnTime>"
                                + "<systemTraceId>0</systemTraceId>"
                                + "</giftCardResponse>"
                                + "</giftCardCreditResponse>"
                                + "</cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        overrides.setMerchantId("905");
        GiftCardCreditResponse response = cnp.giftCardCredit(gcCredit, overrides);
        assertEquals(21825673457518565L, response.getCnpTxnId());
    }
    
    @Test
    public void testRecurring() throws Exception{
        Sale sale = new Sale();
        sale.setAmount(106L);
        sale.setCnpTxnId(123456L);
        sale.setOrderId("12344");
        sale.setId("id");
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000002");
        card.setExpDate("1210");
        sale.setCard(card);
        RecurringRequestType recuring = new RecurringRequestType();
        RecurringSubscriptionType sub = new RecurringSubscriptionType();
        sub.setPlanCode("12345");
        sub.setNumberOfPayments(12);
        sub.setStartDate(Calendar.getInstance());
        sub.setAmount(1000L);
        CreateAddOnType cat = new CreateAddOnType();
        cat.setAddOnCode("1234");
        cat.setAmount(500L);
        cat.setEndDate(Calendar.getInstance());
        cat.setName("name");
        cat.setEndDate(Calendar.getInstance());
        sub.getCreateAddOns().add(cat);
        recuring.setCreateSubscription(sub);
        sale.setRecurringRequest(recuring);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<sale.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?<createAddOn>.*?</createAddOn>.*?</sale>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='12.0' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><saleResponse><cnpTxnId>123</cnpTxnId></saleResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);
        SaleResponse saleresponse = cnp.sale(sale);
        assertEquals(123L, saleresponse.getCnpTxnId());
        
    }
    
    @Test
    public void testQueryTransactionResponse_notFound() {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("1234");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("org1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        
        Communication mockedComm = mock (Communication.class);
        when(mockedComm.requestToServer(matches(".*?<cnpOnlineRequest.*?<queryTransaction.*id=\"1234\".*?customerId=\"customerId\".*?<origId>org1</origId>.*?<origActionType>A</origActionType>.*?"),
                any(Properties.class))).thenReturn("<cnpOnlineResponse version='12.0' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><queryTransactionResponse id='1234' customerId='customerId'> <response>150</response> <responseTime>2015-04-14T12:37:26</responseTime> <message>Original transaction not found</message><matchCount>0</matchCount></queryTransactionResponse></cnpOnlineResponse>");
        
        cnp.setCommunication(mockedComm);
        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("1234", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        
    }
    
    @Test
    public void testQueryTransactionResponse_transactionFound() {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("org1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        
        
        
        Communication mockedComm = mock (Communication.class); 
        when(mockedComm.requestToServer(matches(".*?<cnpOnlineRequest.*?<queryTransaction.*id=\"findId\".*?customerId=\"customerId\".*?<origId>org1</origId>.*?<origActionType>A</origActionType>.*?"),
                any(Properties.class))).thenReturn("<cnpOnlineResponse version='12.0' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><queryTransactionResponse id='findId' customerId='customerId'> <response>150</response> <responseTime>2015-04-14T12:37:26</responseTime> " +
                		"<message>Original transaction found</message><matchCount>1</matchCount>" +
                		"<results_max10> <authorizationResponse id=\"findId\" > <cnpTxnId>1111111</cnpTxnId> <orderId>150306_auth</orderId> <response>000</response><responseTime>2015-04-14T12:37:23</responseTime><postDate>2015-04-14</postDate><message>Approved</message></authorizationResponse></results_max10></queryTransactionResponse></cnpOnlineResponse>");
        
        cnp.setCommunication(mockedComm);
        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("150", queryTransactionResponse.getResponse());
        assertEquals(1, queryTransactionResponse.getResultsMax10().getTransactionResponses().size());
        JAXBElement<?> authorization = queryTransactionResponse.getResultsMax10().getTransactionResponses().get(0);
        AuthorizationResponse authResponse = (AuthorizationResponse)authorization.getValue();
        assertEquals(1111111L,authResponse.getCnpTxnId());
    }
    
    @Test
    public void testQueryTransactionResponse_unavailable() {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("1234");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("org1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        
        Communication mockedComm = mock (Communication.class);
        when(mockedComm.requestToServer(matches(".*?<cnpOnlineRequest.*?<queryTransaction.*id=\"1234\".*?customerId=\"customerId\".*?<origId>org1</origId>.*?<origActionType>A</origActionType>.*?"),
                any(Properties.class))).thenReturn("<cnpOnlineResponse version='12.0' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><queryTransactionUnavailableResponse id='1234' customerId='customerId'><cnpTxnId>123456</cnpTxnId> <response>123</response> <message>Sample message</message></queryTransactionUnavailableResponse></cnpOnlineResponse>");
        
        cnp.setCommunication(mockedComm);
        TransactionTypeWithReportGroup response =cnp.queryTransaction(queryTransaction);
        QueryTransactionUnavailableResponse unavailableResponse = (QueryTransactionUnavailableResponse)response;
        assertEquals("1234", unavailableResponse.getId());
        assertEquals(123456L,unavailableResponse.getCnpTxnId());
        assertEquals("Sample message", unavailableResponse.getMessage());
    }
    

    public void testFraudCheck() throws Exception{
        FraudCheck fraudCheck = new FraudCheck();
        AdvancedFraudChecksType advancedFraudChecks = new AdvancedFraudChecksType();
        advancedFraudChecks.setThreatMetrixSessionId("123");
        advancedFraudChecks.setCustomAttribute1("pass");
        advancedFraudChecks.setCustomAttribute2("42");
        advancedFraudChecks.setCustomAttribute3("5");
        fraudCheck.setAdvancedFraudChecks(advancedFraudChecks);
        
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<cnpOnlineRequest.*?<fraudCheck.*?<advancedFraudChecks>.*?</advancedFraudChecks>.*?</fraudCheck>.*?"),
                                any(Properties.class)))
                .thenReturn(
                        "<cnpOnlineResponse version='12.0' response='0' message='Valid Format' xmlns='http://www.vantivcnp.com/schema'><fraudCheckResponse id='' reportGroup='Default Report Group' customerId=''><cnpTxnId>602413782865196123</cnpTxnId><response>123</response><responseTime>2016-07-11T15:12:34</responseTime><message>Call Discover</message><advancedFraudResults><deviceReviewStatus>pass</deviceReviewStatus><deviceReputationScore>42</deviceReputationScore><triggeredRule>triggered_rule_1</triggeredRule><triggeredRule>triggered_rule_2</triggeredRule><triggeredRule>triggered_rule_3</triggeredRule><triggeredRule>triggered_rule_4</triggeredRule><triggeredRule>triggered_rule_5</triggeredRule></advancedFraudResults></fraudCheckResponse></cnpOnlineResponse>");
        cnp.setCommunication(mockedCommunication);        
        
        FraudCheckResponse fraudCheckResponse = cnp.fraudCheck(fraudCheck);
        AdvancedFraudResultsType advancedFraudResultsType = fraudCheckResponse.getAdvancedFraudResults();
        assertEquals("pass", advancedFraudResultsType.getDeviceReviewStatus());
        assertEquals(new Integer(42), advancedFraudResultsType.getDeviceReputationScore());
        assertEquals(5, advancedFraudResultsType.getTriggeredRules().size());
    }

}
