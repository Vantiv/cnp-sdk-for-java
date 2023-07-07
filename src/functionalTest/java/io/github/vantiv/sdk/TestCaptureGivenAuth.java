package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Calendar;

import io.github.vantiv.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

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
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleCaptureGivenAuthWithBusinessIndicator() throws Exception{
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
		capturegivenauth.setBusinessIndicator(BusinessIndicatorEnum.CONSUMER_BILL_PAYMENT);
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
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
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testCaptureGivenAuthWithProcessingTypeCOF() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		capturegivenauth.setProcessingType(ProcessingTypeEnum.INITIAL_COF);
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
		assertEquals("sandbox", response.getLocation());

		capturegivenauth.setProcessingType(ProcessingTypeEnum.MERCHANT_INITIATED_COF);
		response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());

		capturegivenauth.setProcessingType(ProcessingTypeEnum.CARDHOLDER_INITIATED_COF);
		response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
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
		assertEquals("sandbox", response.getLocation());
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
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleCaptureGivenAuthWithTokenWithTokenURL() throws Exception{
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
		cardtoken.setTokenURL("http://token.com/sales");
		cardtoken.setExpDate("1210");
		cardtoken.setCardValidationNum("555");
		cardtoken.setType(MethodOfPaymentTypeEnum.VI);
		capturegivenauth.setToken(cardtoken);
		capturegivenauth.setId("id");
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
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
		assertEquals("sandbox", response.getLocation());
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
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testCaptureGivenAuthWithEnhancedDataLineItemDataBussinessIndiCrypto() throws Exception {
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
		cardtoken.setTokenURL("http://token.com/sales");
		cardtoken.setExpDate("1210");
		cardtoken.setCardValidationNum("555");
		cardtoken.setType(MethodOfPaymentTypeEnum.VI);
		capturegivenauth.setToken(cardtoken);
		capturegivenauth.setId("id");
		EnhancedData enhanced = new EnhancedData();
		enhanced.setCustomerReference("Cust Ref");
		enhanced.setSalesTax(1000L);
		LineItemData lid = new LineItemData();
		lid.setItemSequenceNumber(1);
		lid.setItemDescription("Electronics");
		lid.setProductCode("El01");
		lid.setItemCategory("Ele Appiances");
		lid.setItemSubCategory("home appliaces");
		lid.setProductId("1001");
		lid.setProductName("dryer");
		enhanced.getLineItemDatas().add(lid);
		enhanced.setDiscountCode("oneTimeDis");
		enhanced.setDiscountPercent(BigInteger.valueOf(12));
		enhanced.setFulfilmentMethodType(FulfilmentMethodTypeEnum.COUNTER_PICKUP);
		capturegivenauth.setEnhancedData(enhanced);
		capturegivenauth.setBusinessIndicator(BusinessIndicatorEnum.FUND_TRANSFER);
		capturegivenauth.setCrypto(false);
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testCaptureGivenAuthWithAdditionalCOFData() throws Exception {
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
		cardtoken.setTokenURL("http://token.com/sales");
		cardtoken.setExpDate("1210");
		cardtoken.setCardValidationNum("555");
		cardtoken.setType(MethodOfPaymentTypeEnum.VI);
		capturegivenauth.setToken(cardtoken);
		capturegivenauth.setId("id");
		AdditionalCOFData data = new AdditionalCOFData();
		data.setUniqueId("56655678D");
		data.setTotalPaymentCount("35");
		data.setFrequencyOfMIT(FrequencyOfMITEnum.ANNUALLY);
		data.setPaymentType(PaymentTypeEnum.FIXED_AMOUNT);
		data.setValidationReference("asd123");
		data.setSequenceIndicator(BigInteger.valueOf(12));
		capturegivenauth.setAdditionalCOFData(data);
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public void simpleCaptureGivenAuthWithRetailerAddress() throws Exception{
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
		Contact contact = new Contact();
		contact.setSellerId("12386576");
		contact.setCompanyName("fis Global");
		contact.setAddressLine1("Pune East");
		contact.setAddressLine2("Pune west");
		contact.setAddressLine3("Pune north");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setZip("825320");
		contact.setCountry(CountryTypeEnum.IN);
		contact.setEmail("cnp.com");
		contact.setPhone("8880129170");
		contact.setUrl("www.lowel.com");
		capturegivenauth.setRetailerAddress(contact);
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public  void simpleCaptureGivenAuthWithPassengerTransportData() throws Exception{
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
		capturegivenauth.setPassengerTransportData(passengerTransportData());
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	private PassengerTransportData passengerTransportData(){
		PassengerTransportData passengerTransportData = new PassengerTransportData();
		passengerTransportData.setPassengerName("PassengerName");
		passengerTransportData.setTicketNumber("9876543210");
		passengerTransportData.setIssuingCarrier("str4");
		passengerTransportData.setCarrierName("CarrierName");
		passengerTransportData.setRestrictedTicketIndicator("RestrictedIndicator");
		passengerTransportData.setNumberOfAdults(8);
		passengerTransportData.setNumberOfChildren(1);
		passengerTransportData.setCustomerCode("CustomerCode");
		passengerTransportData.setArrivalDate(Calendar.getInstance());
		passengerTransportData.setIssueDate(Calendar.getInstance());
		passengerTransportData.setTravelAgencyCode("TravCode");
		passengerTransportData.setTravelAgencyName("TravelAgencyName");
		passengerTransportData.setComputerizedReservationSystem(ComputerizedReservationSystemEnum.DATS);
		passengerTransportData.setCreditReasonIndicator(CreditReasonIndicatorEnum.C);
		passengerTransportData.setTicketChangeIndicator(TicketChangeIndicatorEnum.C);
		passengerTransportData.setTicketIssuerAddress("IssuerAddress");
		passengerTransportData.setExchangeAmount(new Long(110));
		passengerTransportData.setExchangeFeeAmount(new Long(112));
		passengerTransportData.setExchangeTicketNumber("ExchangeNumber");
		passengerTransportData.getTripLegDatas().add(addTripLegData());
		return  passengerTransportData;
	}

	private TripLegData addTripLegData(){
		TripLegData tripLegData = new TripLegData();
		tripLegData.setTripLegNumber(new BigInteger("4"));
		tripLegData.setDepartureCode("DeptC");
		tripLegData.setCarrierCode("CC");
		tripLegData.setServiceClass(ServiceClassEnum.BUSINESS);
		tripLegData.setStopOverCode("N");
		tripLegData.setDestinationCode("DestC");
		tripLegData.setFareBasisCode("FareBasisCode");
		tripLegData.setDepartureDate(Calendar.getInstance());
		tripLegData.setOriginCity("OCity");
		tripLegData.setTravelNumber("TravN");
		tripLegData.setDepartureTime("01:00");
		tripLegData.setArrivalTime("10:00");
		tripLegData.setRemarks("Remarks");
		return  tripLegData;
	}

	@Test
	public  void simpleCaptureGivenAuthWithForeignRetailerIndicatorEnum() throws Exception{
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
		capturegivenauth.setForeignRetailerIndicator(ForeignRetailerIndicatorEnum.F);
		CaptureGivenAuthResponse response = cnp.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
}