package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import io.github.vantiv.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Calendar;

public class TestForceCapture {
	
	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}
	
	@Test
	public void simpleForceCaptureWithCard() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		forcecapture.setCard(card);
	    forcecapture.setId("id");
		ForceCaptureResponse response = cnp.forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleForceCaptureWithBusinessIndicator() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		forcecapture.setCard(card);
		forcecapture.setId("id");
		forcecapture.setBusinessIndicator(BusinessIndicatorEnum.CONSUMER_BILL_PAYMENT);
		ForceCaptureResponse response = cnp.forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void testForceCaptureWithProcesingType() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		forcecapture.setProcessingType(ProcessingTypeEnum.INITIAL_RECURRING);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		forcecapture.setCard(card);
	    forcecapture.setId("id");
		ForceCaptureResponse response = cnp.forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
    public void simpleForceCaptureWithSecondaryAmount() throws Exception{
        ForceCapture forcecapture = new ForceCapture();
        forcecapture.setAmount(106L);
        forcecapture.setSecondaryAmount(20L);
        forcecapture.setOrderId("12344");
        forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        forcecapture.setCard(card);
        forcecapture.setId("id");
        ForceCaptureResponse response = cnp.forceCapture(forcecapture);
        assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
    }
	
	@Test
	public void simpleForceCaptureWithToken() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType token = new CardTokenType();
		token.setCnpToken("123456789101112");
		token.setExpDate("1210");
		token.setCardValidationNum("555");
		token.setType(MethodOfPaymentTypeEnum.VI);
		forcecapture.setToken(token);
		forcecapture.setId("id");
		ForceCaptureResponse response = cnp.forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public  void simpleForceCaptureWithPassengerTransportData() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType token = new CardTokenType();
		token.setCnpToken("123456789101112");
		token.setExpDate("1210");
		token.setCardValidationNum("555");
		token.setType(MethodOfPaymentTypeEnum.VI);
		forcecapture.setToken(token);
		forcecapture.setId("id");
		forcecapture.setPassengerTransportData(passengerTransportData());
		ForceCaptureResponse response = cnp.forceCapture(forcecapture);
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
	public void forceCaptureWithForeignRetailerIndicatorEnum() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		forcecapture.setCard(card);
		forcecapture.setId("id");
		forcecapture.setForeignRetailerIndicator(ForeignRetailerIndicatorEnum.F);
		ForceCaptureResponse response = cnp.forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public  void simpleForceCaptureWithSubscription() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType token = new CardTokenType();
		token.setCnpToken("123456789101112");
		token.setExpDate("1210");
		token.setCardValidationNum("555");
		token.setType(MethodOfPaymentTypeEnum.VI);
		forcecapture.setToken(token);
		forcecapture.setId("id");
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
		Subscription sub = new Subscription();
		sub.setSubscriptionId("123");
		sub.setCurrentPeriod(BigInteger.valueOf(1));
		sub.setPeriodUnit("MONTH");
		sub.setNumberOfPeriods(BigInteger.valueOf(2));
		sub.setCurrentPeriod(BigInteger.valueOf(3));
		sub.setNextDeliveryDate(Calendar.getInstance());
		lid.setShipmentId("456");
		lid.setSubscription(sub);
		enhanced.getLineItemDatas().add(lid);
		forcecapture.setEnhancedData(enhanced);
		forcecapture.setPassengerTransportData(passengerTransportData());
		ForceCaptureResponse response = cnp.forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
}

