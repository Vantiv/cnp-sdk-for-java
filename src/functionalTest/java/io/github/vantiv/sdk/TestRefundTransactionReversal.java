package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import io.github.vantiv.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Calendar;

public class TestRefundTransactionReversal {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleTransactionReversal() {
        RefundTransactionReversal refundTransactionReversal = new RefundTransactionReversal();
        refundTransactionReversal.setId("id");
        refundTransactionReversal.setCnpTxnId(124785L);

        RefundTransactionReversalResponse response = cnp.refundTransactionReversal(refundTransactionReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
        assertEquals("000", response.getResponse());
        assertEquals("id", response.getId());
        assertEquals(124785L, response.getRecyclingResponse().getCreditCnpTxnId().longValue());
    }

    @Test
    public void filledTransactionReversal() {
        RefundTransactionReversal refundTransactionReversal = new RefundTransactionReversal();
        refundTransactionReversal.setId("id123");
        refundTransactionReversal.setCnpTxnId(987654L);
        refundTransactionReversal.setAmount(4321L);
        refundTransactionReversal.setCustomBilling(new CustomBilling());
        refundTransactionReversal.setEnhancedData(new EnhancedData());
        refundTransactionReversal.setLodgingInfo(new LodgingInfo());
        refundTransactionReversal.setPin("1234");
        refundTransactionReversal.setProcessingInstructions(new ProcessingInstructions());
        refundTransactionReversal.setSurchargeAmount(6789L);

        RefundTransactionReversalResponse response = cnp.refundTransactionReversal(refundTransactionReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
        assertEquals("000", response.getResponse());
        assertEquals("id123", response.getId());
        assertEquals(987654L, response.getRecyclingResponse().getCreditCnpTxnId().longValue());
    }
    @Test
    public  void simpleRefundTransactionReversalWithPassengerTransportData() throws Exception{
        RefundTransactionReversal refundTransactionReversal = new RefundTransactionReversal();
        refundTransactionReversal.setId("id");
        refundTransactionReversal.setCnpTxnId(124785L);
        refundTransactionReversal.setPassengerTransportData(passengerTransportData());
        RefundTransactionReversalResponse response = cnp.refundTransactionReversal(refundTransactionReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
        assertEquals("000", response.getResponse());
        assertEquals("id", response.getId());
        assertEquals(124785L, response.getRecyclingResponse().getCreditCnpTxnId().longValue());
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
}
