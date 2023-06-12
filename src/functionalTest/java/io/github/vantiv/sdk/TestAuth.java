package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.Calendar;

//import com.cnp.sdk.generate.*;
import io.github.vantiv.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAuth {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}
	

    @Test
    public void simpleAuthWithCardUTF8() throws Exception {
        Authorization authorization = new Authorization();
        authorization.setReportGroup("русский中文");
        authorization.setOrderId("12344");
        authorization.setAmount(106L);
        authorization.setOrderSource(OrderSourceType.ECOMMERCE);
        authorization.setId("id");
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000000");
        card.setExpDate("1210");
        authorization.setCard(card);
        AuthorizationResponse response = cnp.authorize(authorization);
        assertEquals("русский中文",response.getReportGroup());
        assertEquals("sandbox", response.getLocation());
    }

	@Test
	public void simpleAuthWithCard() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
	    authorization.setId("id");
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleAuthWithBusinessIndcator() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		//authorization.setBusinessIndicator(BusinessIndicatorEnum.CONSUMER_BILL_PAYMENT);
        authorization.setBusinessIndicator(BusinessIndicatorEnum.WALLET_TRANSFER);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleAuthWith_eProtectRegId() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		CardPaypageType eProtectObject = new CardPaypageType();
		eProtectObject.setPaypageRegistrationId("returned_registration_id");
		authorization.setPaypage(eProtectObject);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testAuthWithAndroidpay() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ANDROIDPAY);
	    authorization.setId("id");
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("aHR0cHM6Ly93d3cueW91dHViZS5jb20vd2F0Y2g/dj1kUXc0dzlXZ1hjUQ0K", response.getAndroidpayResponse().getCryptogram());
		assertEquals("01", response.getAndroidpayResponse().getExpMonth());
		assertEquals("2050", response.getAndroidpayResponse().getExpYear());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleAuthWithProcessngType() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
	    authorization.setId("id");
	    authorization.setProcessingType(ProcessingTypeEnum.ACCOUNT_FUNDING);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleAuthWithProcessngTypeCOF() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		authorization.setProcessingType(ProcessingTypeEnum.INITIAL_COF);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());

		authorization.setProcessingType(ProcessingTypeEnum.MERCHANT_INITIATED_COF);
		response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());

		authorization.setProcessingType(ProcessingTypeEnum.CARDHOLDER_INITIATED_COF);
		response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("sandbox", response.getLocation());

	}

	@Test
	public void simpleAuthWithOrigNetworkTxnAndOrigAmount() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
	    authorization.setId("id");
	    authorization.setProcessingType(ProcessingTypeEnum.INITIAL_INSTALLMENT);
	    authorization.setOriginalNetworkTransactionId("zz12547879");
	    authorization.setOriginalTransactionAmount(3955l);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleAuthWithPaypal() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("123456");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
	    authorization.setId("id");
		PayPal paypal = new PayPal();
		paypal.setPayerId("1234");
		paypal.setToken("1234");
		paypal.setTransactionId("123456");
		authorization.setPaypal(paypal);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
    public void simpleAuthWithApplepayAndSecondaryAmount() throws Exception {
        Authorization authorization = new Authorization();
        authorization.setReportGroup("Planets");
        authorization.setOrderId("123456");
        authorization.setAmount(110L);
        authorization.setSecondaryAmount(50L);
        authorization.setOrderSource(OrderSourceType.ECOMMERCE);
        authorization.setId("id");
        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType.setApplicationData("454657413164");
        applepayHeaderType.setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData("user");
        applepayType.setSignature("sign");
        applepayType.setVersion("12345");
        authorization.setApplepay(applepayType);

        AuthorizationResponse response = cnp.authorize(authorization);
        assertEquals(new Long(110),response.getApplepayResponse().getTransactionAmount());
		assertEquals("sandbox", response.getLocation());
    }

    @Test
	public void simpleAuthWithLodgingInfo(){
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("123456");
		authorization.setAmount(110L);
		authorization.setSecondaryAmount(50L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		LodgingInfo lodgingInfo = new LodgingInfo();
		lodgingInfo.setRoomRate(110L);
		lodgingInfo.setRoomTax(50L);
		LodgingCharge lodgingCharge = new LodgingCharge();
		lodgingCharge.setName(LodgingExtraChargeEnum.RESTAURANT);
		lodgingInfo.getLodgingCharges().add(lodgingCharge);
		authorization.setLodgingInfo(lodgingInfo);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals("000", response.getResponse());
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void posWithoutCapabilityAndEntryMode() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Pos pos = new Pos();
		pos.setCardholderId(PosCardholderIdTypeEnum.PIN);
		authorization.setPos(pos);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		try {
			cnp.authorize(authorization);
			fail("expected exception");
		} catch(CnpOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
	}

	@Test
	public void testAccountUpdate() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100100000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals("4100100000000000", response.getAccountUpdater().getOriginalCardInfo().getNumber());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testTrackData() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setId("AX54321678");
		authorization.setReportGroup("RG27");
		authorization.setOrderId("12z58743y1");
		authorization.setAmount(12522L);
		authorization.setOrderSource(OrderSourceType.RETAIL);
		Contact billToAddress = new Contact();
		billToAddress.setZip("95032");
		authorization.setBillToAddress(billToAddress);
		CardType card = new CardType();
		card.setTrack("%B40000001^Doe/JohnP^06041...?;40001=0604101064200?");
		authorization.setCard(card);
		Pos pos = new Pos();
		pos.setCapability(PosCapabilityTypeEnum.MAGSTRIPE);
		pos.setEntryMode(PosEntryModeTypeEnum.COMPLETEREAD);
		pos.setCardholderId(PosCardholderIdTypeEnum.SIGNATURE);
		authorization.setPos(pos);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testListOfTaxAmounts() throws Exception {
	    Authorization authorization = new Authorization();
	    authorization.setId("12345");
	    authorization.setReportGroup("Default");
	    authorization.setOrderId("67890");
	    authorization.setAmount(10000L);
	    authorization.setOrderSource(OrderSourceType.ECOMMERCE);
	    EnhancedData enhanced = new EnhancedData();
	    DetailTax dt1 = new DetailTax();
	    dt1.setTaxAmount(100L);
	    enhanced.getDetailTaxes().add(dt1);
	    DetailTax dt2 = new DetailTax();
	    dt2.setTaxAmount(200L);
	    enhanced.getDetailTaxes().add(dt2);
	    authorization.setEnhancedData(enhanced);
	    CardType card = new CardType();
	    card.setNumber("4100000000000000");
	    card.setExpDate("1215");
	    card.setType(MethodOfPaymentTypeEnum.VI);
        authorization.setCard(card);

        AuthorizationResponse response = cnp.authorize(authorization);
        assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleAuthWithCardSkipRealtimeAUTrue() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		authorization.setSkipRealtimeAU(true);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleAuthWithCardSkipRealtimeAUFalse() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		authorization.setSkipRealtimeAU(false);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleAuthProtocolOne() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		FraudCheckType fraudCheckType = new FraudCheckType();
		fraudCheckType.setAuthenticationProtocolVersion(new BigInteger("1"));
		authorization.setCardholderAuthentication(fraudCheckType);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test (expected = CnpOnlineException.class)
	public void simpleAuthProtocolZero() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		FraudCheckType fraudCheckType = new FraudCheckType();
		fraudCheckType.setAuthenticationProtocolVersion(new BigInteger("0"));
		authorization.setCardholderAuthentication(fraudCheckType);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleAuthProtocolNot() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		FraudCheckType fraudCheckType = new FraudCheckType();
		authorization.setCardholderAuthentication(fraudCheckType);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		assertEquals(null, authorization.getCardholderAuthentication().getAuthenticationProtocolVersion());

		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testAuthWithCustomerInfo() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setId("12345");
		authorization.setReportGroup("Default");
		authorization.setOrderId("67890");
		authorization.setAmount(10000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CustomerInfo cust = new CustomerInfo();
		cust.setAccountUsername("Woolfoo");
		cust.setUserAccountNumber("123456ATY");
		cust.setUserAccountEmail("woolfoo@gmail.com");
		cust.setMembershipId("Member01");
		cust.setMembershipPhone("9765431234");
		cust.setMembershipEmail("mem@abc.com");
		cust.setMembershipName("memName");
		Calendar createDate = Calendar.getInstance();
		createDate.set(1990, Calendar.MARCH, 16);
		cust.setAccountCreatedDate(createDate);
		cust.setUserAccountPhone("123456789");
		authorization.setCustomerInfo(cust);

		CardType card = new CardType();
		card.setNumber("4100000000000000");
		card.setExpDate("1215");
		card.setType(MethodOfPaymentTypeEnum.VI);
		authorization.setCard(card);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testAuthWithEnhancedDataLineItemDataOrdChanelfraudCheckStatusCrypto() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setId("12345");
		authorization.setReportGroup("Default");
		authorization.setOrderId("67890");
		authorization.setAmount(10000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setNumber("4100000000000000");
		card.setExpDate("1215");
		card.setType(MethodOfPaymentTypeEnum.VI);
		authorization.setCard(card);
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
		authorization.setEnhancedData(enhanced);
		authorization.setOrderChannel(OrderChannelEnum.IN_STORE_KIOSK);
		authorization.setFraudCheckStatus("CLOSE");
		authorization.setCrypto(false);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testAuthWithAdditionalCOFData() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setId("12345");
		authorization.setReportGroup("Default");
		authorization.setOrderId("67890");
		authorization.setAmount(10000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setNumber("4100000000000000");
		card.setExpDate("1215");
		card.setType(MethodOfPaymentTypeEnum.VI);
		authorization.setCard(card);
		AdditionalCOFData data = new AdditionalCOFData();
		data.setUniqueId("56655678D");
		data.setTotalPaymentCount("35");
		data.setFrequencyOfMIT(FrequencyOfMITEnum.ANNUALLY);
		data.setPaymentType(PaymentTypeEnum.FIXED_AMOUNT);
		data.setValidationReference("asd123");
		data.setSequenceIndicator(BigInteger.valueOf(12));
		authorization.setAdditionalCOFData(data);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public void simpleAuthWithRetailerAddress() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		FraudCheckType fraudCheckType = new FraudCheckType();
		authorization.setCardholderAuthentication(fraudCheckType);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
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
		authorization.setRetailerAddress(contact);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals( "Approved",response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void AuthWithFsErrorCode_OverridePolicy_ProdEnrolled_MercAcctStatus_FraudSwitchInd_Deci_Purpose() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		FraudCheckType fraudCheckType = new FraudCheckType();
		authorization.setCardholderAuthentication(fraudCheckType);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		authorization.setOverridePolicy("FIS Policy");
		authorization.setFsErrorCode("Not Applicable");
		authorization.setMerchantAccountStatus("Active");
		authorization.setProductEnrolled(ProductEnrolledEnum.GUARPAY_1);
		authorization.setDecisionPurpose(DecisionPurposeEnum.CONSIDER_DECISION);
		authorization.setFraudSwitchIndicator(FraudSwitchIndicatorEnum.PRE);
		AuthorizationResponse response = cnp.authorize(authorization);

		assertEquals( "Approved",response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void AuthWithLodgingInfo_propertyAddress() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		FraudCheckType fraudCheckType = new FraudCheckType();
		authorization.setCardholderAuthentication(fraudCheckType);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		authorization.setOverridePolicy("FIS Policy");
		authorization.setFsErrorCode("Not Applicable");
		authorization.setMerchantAccountStatus("Active");
		authorization.setProductEnrolled(ProductEnrolledEnum.GUARPAY_1);
		authorization.setDecisionPurpose(DecisionPurposeEnum.CONSIDER_DECISION);
		authorization.setFraudSwitchIndicator(FraudSwitchIndicatorEnum.PRE);
		LodgingInfo lodgingInfo = new LodgingInfo();
		lodgingInfo.setBookingID("bKID1");
		lodgingInfo.setPassengerName("HoneyWell");
		PropertyAddress propertyAddress = new PropertyAddress();
		propertyAddress.setName("Bangalore");
		propertyAddress.setCity("Karnataka");
		propertyAddress.setCountry(CountryTypeEnum.IN);
		propertyAddress.setRegion("NTH");
		lodgingInfo.setPropertyAddress(propertyAddress);
		lodgingInfo.setTravelPackageIndicator(TravelPackageIndicatorEnum.CAR_RENTAL_RESERVATION);
		lodgingInfo.setNumberOfRooms(BigInteger.valueOf(1));
		lodgingInfo.setTollFreePhoneNumber("9934579676");
		authorization.setLodgingInfo(lodgingInfo);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals( "Approved",response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public  void simpleAuthWithPassengerTransportData() throws Exception{
		Authorization authorization = new Authorization();
		authorization.setReportGroup("русский中文");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		authorization.setPassengerTransportData(passengerTransportData());
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals("русский中文",response.getReportGroup());
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
	public  void simpleAuthWithAuthMaxData() throws Exception{
		Authorization authorization = new Authorization();
		authorization.setReportGroup("русский中文");
		authorization.setOrderId("12344401");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(true,response.getAuthMax().isAuthMaxApplied());
		assertEquals(true,response.getAuthMax().isNetworkTokenApplied());
		assertEquals("1112000199940085",response.getAuthMax().getNetworkToken());
		assertEquals("000",response.getAuthMax().getAuthMaxResponseCode());
		assertEquals("Approved",response.getAuthMax().getAuthMaxResponseMessage());
		assertEquals("русский中文",response.getReportGroup());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public  void simpleAuthTxnIdWithAuthMaxData() throws Exception{
		Authorization authorization = new Authorization();
		authorization.setId("1");
		authorization.setCustomerId("Cust043");
		authorization.setReportGroup("001550");
		authorization.setCnpTxnId(34659348401L);
		authorization.setAmount(106L);
		authorization.setAuthIndicator(AuthIndicatorEnum.ESTIMATED);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(true,response.getAuthMax().isAuthMaxApplied());
		assertEquals(true,response.getAuthMax().isNetworkTokenApplied());
		assertEquals("1112000199940085",response.getAuthMax().getNetworkToken());
		assertEquals("Approved",response.getAuthMax().getAuthMaxResponseMessage());
		assertEquals("001550",response.getReportGroup());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public  void simpleAuthTxnIdWithAuthMaxDataFalse() throws Exception{
		Authorization authorization = new Authorization();
		authorization.setId("1");
		authorization.setCustomerId("Cust043");
		authorization.setReportGroup("001550");
		authorization.setCnpTxnId(34659348402L);
		authorization.setAmount(106L);
		authorization.setAuthIndicator(AuthIndicatorEnum.ESTIMATED);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(false,response.getAuthMax().isAuthMaxApplied());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public  void simpleAuthWithAuthMaxFalse() throws Exception{
		Authorization authorization = new Authorization();
		authorization.setReportGroup("русский中文");
		authorization.setOrderId("12344402");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(false,response.getAuthMax().isAuthMaxApplied());
		assertEquals("русский中文",response.getReportGroup());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public  void simpleAuthWithAuthMaxNotApplied() throws Exception{
		Authorization authorization = new Authorization();
		authorization.setReportGroup("русский中文");
		authorization.setOrderId("12344403");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals("русский中文",response.getReportGroup());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleAuthWithSellerInfo() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("русский中文");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		authorization.setAmount(1000L);
		authorization.getSellerInfos().add(addSellerInfo());
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals("русский中文",response.getReportGroup());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public void simpleAuthWithSellerInfoNull() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("русский中文");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);
		authorization.setAmount(1000L);
		authorization.getSellerInfos().add(null);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals("русский中文",response.getReportGroup());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public void testAuthWithOrdChanelMITEnum() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setId("12345");
		authorization.setReportGroup("Default");
		authorization.setOrderId("67890");
		authorization.setAmount(10000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setNumber("4100000000000000");
		card.setExpDate("1215");
		card.setType(MethodOfPaymentTypeEnum.VI);
		authorization.setCard(card);
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
		authorization.setEnhancedData(enhanced);
		authorization.setOrderChannel(OrderChannelEnum.MIT);
		authorization.setFraudCheckStatus("CLOSE");
		authorization.setCrypto(false);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testAuthIndicatorEnumEstimated() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setId("12345");
		authorization.setReportGroup("Default");
		authorization.setOrderId("67890");
		authorization.setAmount(10000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setNumber("4100000000000000");
		card.setExpDate("1215");
		card.setType(MethodOfPaymentTypeEnum.VI);
		authorization.setCard(card);
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
		authorization.setEnhancedData(enhanced);
		authorization.setOrderChannel(OrderChannelEnum.MIT);
		authorization.setFraudCheckStatus("CLOSE");
		authorization.setCrypto(false);
		authorization.setAuthIndicator(AuthIndicatorEnum.ESTIMATED);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public  void testAuthIndicatorEnumIncremental() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setId("12345");
		authorization.setCustomerId("Cust044");
		authorization.setReportGroup("Default");
		authorization.setCnpTxnId(34659348401L);
		authorization.setAmount(106L);
		authorization.setAuthIndicator(AuthIndicatorEnum.INCREMENTAL);
		AuthorizationResponse response = cnp.authorize(authorization);
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	private SellerInfo addSellerInfo(){
		SellerInfo sellerInfo=new SellerInfo();
		sellerInfo.setAccountNumber("4485581000000005");
		sellerInfo.setAggregateOrderCount(new BigInteger("4"));
		sellerInfo.setAggregateOrderDollars(100L);
		sellerInfo.setSellerAddress(addSellerAddress());
		sellerInfo.setCreatedDate("2015-11-12T20:33:09");
		sellerInfo.setDomain("vap");
		sellerInfo.setEmail("bob@example.com");
		sellerInfo.setLastUpdateDate("2015-11-12T20:33:09");
		sellerInfo.setName("bob");
		sellerInfo.setOnboardingEmail("bob@example.com");
		sellerInfo.setOnboardingIpAddress("75.100.88.78");
		sellerInfo.setParentEntity("abc");
		sellerInfo.setPhone("9785510040");
		sellerInfo.setSellerId("123456789");
		sellerInfo.setSellerTags(addSellerTags());
		sellerInfo.setUsername("bob123");

		return  sellerInfo;
	}

	private SellerAddress addSellerAddress(){
		SellerAddress sellerAddress=new SellerAddress();
		sellerAddress.setSellerStreetaddress("15 Main Street");
		sellerAddress.setSellerUnit("100 AB");
		sellerAddress.setSellerPostalcode("12345");
		sellerAddress.setSellerCity("San Jose");
		sellerAddress.setSellerProvincecode("MA");
		sellerAddress.setSellerCountrycode("US");
		return  sellerAddress;
	}


	private SellerTagsType addSellerTags(){
		SellerTagsType sellerTagsType=new SellerTagsType();
		sellerTagsType.getTags().add("1");
		sellerTagsType.getTags().add("2");
		sellerTagsType.getTags().add("3");
		sellerTagsType.getTags().add("4");
		sellerTagsType.getTags().add("5");

		return  sellerTagsType;
	}


}
