package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.GiftCardAuthReversal;
import com.cnp.sdk.generate.GiftCardAuthReversalResponse;
import com.cnp.sdk.generate.GiftCardCapture;
import com.cnp.sdk.generate.GiftCardCaptureResponse;
import com.cnp.sdk.generate.GiftCardCardType;
import com.cnp.sdk.generate.GiftCardCredit;
import com.cnp.sdk.generate.GiftCardCreditResponse;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.cnp.sdk.generate.OrderSourceType;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestGiftCardTransactions {

	CnpOnline cnp;
	
	@Before
	public void setup() {
		cnp = new CnpOnline();
	}
	
	@Test
	public void testGiftCardCapture() {
		GiftCardCapture gcCapture = new GiftCardCapture();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");
        
        gcCapture.setCnpTxnId(123L);
        gcCapture.setId("id");
        gcCapture.setReportGroup("rptGrp");
        gcCapture.setCaptureAmount(2434l);
        gcCapture.setCard(giftCard);
        gcCapture.setOriginalRefCode("ref");
        gcCapture.setOriginalAmount(44455l);
        gcCapture.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        
        GiftCardCaptureResponse response = cnp.giftCardCapture(gcCapture);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void testGiftCardCredit_withCnpTxnId() {
		GiftCardCredit gcCredit = new GiftCardCredit();
		GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");
        
        gcCredit.setCnpTxnId(369852147l);
        gcCredit.setId("id");
        gcCredit.setReportGroup("rptGrp1");
        gcCredit.setCustomerId("customer_22");
        gcCredit.setCreditAmount(1942l);
        gcCredit.setCard(giftCard);
        
        GiftCardCreditResponse response = cnp.giftCardCredit(gcCredit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void testGiftCardCredit_withOrderId() {
		GiftCardCredit gcCredit = new GiftCardCredit();
		GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");
        
        gcCredit.setId("id");
        gcCredit.setReportGroup("rptGrp1");
        gcCredit.setCustomerId("customer_22");
        gcCredit.setOrderSource(OrderSourceType.ECOMMERCE);
        gcCredit.setCreditAmount(1942l);
        gcCredit.setOrderId("order 4");
        gcCredit.setCard(giftCard);
        
        GiftCardCreditResponse response = cnp.giftCardCredit(gcCredit);
        assertEquals("Approved", response.getMessage());
        assertEquals("123456", response.getGiftCardResponse().getSequenceNumber());
        assertEquals("sandbox", response.getLocation());
	}
	
	@Test
	public void testGiftCardAuthReversal() {
		GiftCardAuthReversal gcAuthReversal = new GiftCardAuthReversal();
		GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");
        
        gcAuthReversal.setId("979797");
        gcAuthReversal.setCustomerId("customer_23");
        gcAuthReversal.setCnpTxnId(8521478963210145l);
        gcAuthReversal.setReportGroup("rptGrp2");
        gcAuthReversal.setOriginalAmount(45l);
        gcAuthReversal.setOriginalSequenceNumber("333333");
        gcAuthReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        gcAuthReversal.setOriginalSystemTraceId(0);
        gcAuthReversal.setOriginalRefCode("ref");
        gcAuthReversal.setCard(giftCard);
        
        GiftCardAuthReversalResponse response = cnp.giftCardAuthReversal(gcAuthReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals(0, (int)response.getGiftCardResponse().getSystemTraceId());
        assertEquals("sandbox", response.getLocation());
	}

}
