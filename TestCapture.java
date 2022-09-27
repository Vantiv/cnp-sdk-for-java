package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import io.github.vantiv.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Calendar;

public class TestCapture {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}

	@Test
	public void simpleCapture() throws Exception{
		Capture capture = new Capture();
		capture.setCnpTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPayPalNotes("Notes");
		capture.setId("id");

		CaptureResponse response = cnp.capture(capture);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void testCaptureWithPin() throws Exception{
		Capture capture = new Capture();
		capture.setCnpTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPayPalNotes("Notes");
		capture.setId("id");
		capture.setPin("1234");

		CaptureResponse response = cnp.capture(capture);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleCaptureWithPartial() throws Exception{
		Capture capture = new Capture();
		capture.setCnpTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPartial(true);
		capture.setPayPalNotes("Notes");
		capture.setId("id");

		CaptureResponse response = cnp.capture(capture);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void complexCapture() throws Exception{
		Capture capture = new Capture();
		capture.setCnpTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPayPalNotes("Notes");
		EnhancedData enhancedData = new EnhancedData();
		enhancedData.setCustomerReference("Cnp");
		enhancedData.setSalesTax(50L);
		enhancedData.setDeliveryType(EnhancedData.DeliveryType.TBD);
		capture.setEnhancedData(enhancedData);
		capture.setPayPalOrderComplete(true);
		capture.setId("id");

		CaptureResponse response = cnp.capture(capture);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	@Test
	public void simpleCaptureWithOrderId() throws Exception{
		Capture capture = new Capture();
		capture.setCnpTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPayPalNotes("Notes");
		capture.setId("id");
		capture.setOrderId("1234567567");

		CaptureResponse response = cnp.capture(capture);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public  void simpleCaptureWithPassengerTransportData() throws Exception{
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
		tripLegData.setDepartureTime("DepartureTime");
		tripLegData.setArrivalTime("ArrivalTime");
		tripLegData.setRemarks("Remarks");
		return  tripLegData;
	}
}


