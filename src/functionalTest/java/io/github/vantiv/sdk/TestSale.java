package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

//import com.cnp.sdk.generate.*;
import java.math.BigInteger;
import java.util.Calendar;

import io.github.vantiv.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSale {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}
	
	@Test
	public void simpleSaleWithCard() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setId("id");
		SaleResponse response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleSaleWithBusinessIndicator() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setId("id");
		sale.setBusinessIndicator(BusinessIndicatorEnum.CONSUMER_BILL_PAYMENT);
		SaleResponse response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void simpleSaleWithCardError() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4165851242543100");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setId("id");
		SaleResponse response = cnp.sale(sale);
		assertEquals("Processing Network Unavailable", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleSaleWithCardError2() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4165851242543850");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setId("id");
		SaleResponse response = cnp.sale(sale);
		assertEquals("Tax Billing only allowed for MCC 9311", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void simpleSaleWithPayPal() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		PayPal paypal = new PayPal();
		paypal.setPayerId("1234");
		paypal.setToken("1234");
		paypal.setTransactionId("123456");
		sale.setPaypal(paypal);
	    sale.setId("id");
		SaleResponse response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
    public void simpleSaleWithApplepayAndSecondaryAmount() throws Exception{
        Sale sale = new Sale();
        sale.setAmount(110L);
        sale.setSecondaryAmount(20L);
        sale.setCnpTxnId(123456L);
        sale.setOrderId("12347");
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
        applepayType.setVersion("12345");

        sale.setApplepay(applepayType);
        sale.setId("id");
        SaleResponse response = cnp.sale(sale);
        assertEquals("Insufficient Funds", response.getMessage());
        assertEquals(new Long(110),response.getApplepayResponse().getTransactionAmount());
		assertEquals("sandbox", response.getLocation());
    }
	
	@Test
	public void simpleSaleWithToken() throws Exception {
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType token = new CardTokenType();
		token.setCardValidationNum("349");
		token.setExpDate("1214");
		token.setCnpToken("1111222233334000");
		token.setType(MethodOfPaymentTypeEnum.VI);
		sale.setToken(token);
	    sale.setId("id");
		SaleResponse response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}



	@Test
	public void simpleSaleWithPinlessDebitRequest() throws Exception {
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType token = new CardTokenType();
		token.setCardValidationNum("349");
		token.setExpDate("1214");
		token.setCnpToken("1111222233334000");
		token.setType(MethodOfPaymentTypeEnum.VI);
		sale.setToken(token);
		sale.setId("id");
		PinlessDebitRequestType pinlessDebitRequest = new PinlessDebitRequestType();
		pinlessDebitRequest.setRoutingPreference(RoutingPreferenceEnum.REGULAR);
		PreferredDebitNetworksType preferredDebitNetwork = new PreferredDebitNetworksType();
		preferredDebitNetwork.getDebitNetworkNames().add("Visa");
		pinlessDebitRequest.setPreferredDebitNetworks(preferredDebitNetwork);
		sale.setPinlessDebitRequest(pinlessDebitRequest);

		SaleResponse response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void testSaleWithSEPA() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		SepaDirectDebitType sepa = new SepaDirectDebitType();
		sepa.setIban("ZZ79850503003100180568");
		sepa.setMandateProvider("Vantiv");
		sepa.setSequenceType("OneTime");
		sepa.setMandateUrl("http://mandate.url");
		sale.setSepaDirectDebit(sepa);
		sale.setId("id");
		SaleResponse response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("http://redirect.url.vantiv.com", response.getSepaDirectDebitResponse().getRedirectUrl());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void testSaleWithProcessingTypeAndOrigTxnIdAndAmount() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setId("id");
		sale.setProcessingType(ProcessingTypeEnum.INITIAL_INSTALLMENT);
		sale.setOriginalNetworkTransactionId("1029384756");
		sale.setOriginalTransactionAmount(4242l);
		SaleResponse response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testSaleWithProcessingTypeCOF() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setId("id");
		sale.setProcessingType(ProcessingTypeEnum.INITIAL_COF);
		SaleResponse response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());

		sale.setProcessingType(ProcessingTypeEnum.MERCHANT_INITIATED_COF);
		response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());

		sale.setProcessingType(ProcessingTypeEnum.CARDHOLDER_INITIATED_COF);
		response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testSaleWithIdeal() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		sale.setId("id");

		IdealType ideal = new IdealType();
		ideal.setPreferredLanguage(CountryTypeEnum.AD);
		sale.setIdeal(ideal);

		SaleResponse response = cnp.sale(sale);

		assertEquals("Approved", response.getMessage());
		assertEquals("http://redirect.url.vantiv.com", response.getIdealResponse().getRedirectUrl());
		assertEquals("jj2d1d372osmmt7tb8epm0a99q", response.getIdealResponse().getRedirectToken());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testSaleWithGiropay() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		sale.setId("id");

		GiropayType giropay = new GiropayType();
		giropay.setPreferredLanguage(CountryTypeEnum.US);
		sale.setGiropay(giropay);

		SaleResponse response = cnp.sale(sale);

		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testSaleWithSofort() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		sale.setId("id");

		SofortType sofort = new SofortType();
		sofort.setPreferredLanguage(CountryTypeEnum.US);
		sale.setSofort(sofort);

		SaleResponse response = cnp.sale(sale);

		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleSaleWithCardSkipRealtimeAUTrue() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		sale.setSkipRealtimeAU(true);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setId("id");
		SaleResponse response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleSaleWithCardSkipRealtimeAUFalse() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		sale.setSkipRealtimeAU(false);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setId("id");
		SaleResponse response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testSaleWithCustomerInfo() throws Exception {
		Sale sale = new Sale();
		sale.setId("12345");
		sale.setReportGroup("Default");
		sale.setOrderId("67890");
		sale.setAmount(10000L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
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
		sale.setCustomerInfo(cust);
		CardType card = new CardType();
		card.setNumber("4100000000000000");
		card.setExpDate("1215");
		card.setType(MethodOfPaymentTypeEnum.VI);
		sale.setCard(card);
		SaleResponse response = cnp.sale(sale);
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testSaleWithEnhancedDataLineItemDataOrdChanelGpStatusBussinessIndi() throws Exception {
		Sale sale = new Sale();
		sale.setId("12345");
		sale.setReportGroup("Default");
		sale.setOrderId("67890");
		sale.setAmount(10000L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setNumber("4100000000000000");
		card.setExpDate("1215");
		card.setType(MethodOfPaymentTypeEnum.VI);
		sale.setCard(card);
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
		enhanced.setFulfilmentMethodType("LOCKER_PICKUP");
		sale.setEnhancedData(enhanced);
		sale.setBusinessIndicator(BusinessIndicatorEnum.BUY_ONLINE_PICK_UP_IN_STORE);
		sale.setOrderChannel("WEB");
		sale.setGpStatus("close");
		SaleResponse response = cnp.sale(sale);
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void testSaleWithAdditionalCOFData() throws Exception {
		Sale sale = new Sale();
		sale.setId("12345");
		sale.setReportGroup("Default");
		sale.setOrderId("67890");
		sale.setAmount(10000L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setNumber("4100000000000000");
		card.setExpDate("1215");
		card.setType(MethodOfPaymentTypeEnum.VI);
		sale.setCard(card);
		AdditionalCOFData data = new AdditionalCOFData();
		data.setUniqueId("56655678D");
		data.setTotalPaymentCount("35");
		data.setFrequencyOfMIT(FrequencyOfMITEnum.ANUALLY);
		data.setPaymentType(PaymentTypeEnum.FIXED_AMOUNT);
		data.setValidationReference("asd123");
		data.setSequenceIndicator(BigInteger.valueOf(12));
		sale.setAdditionalCOFData(data);
		SaleResponse response = cnp.sale(sale);
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public void simpleSaleWithRetailerAddress() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setCnpTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setId("id");
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
		sale.setRetailerAddress(contact);
		SaleResponse response = cnp.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}


}
