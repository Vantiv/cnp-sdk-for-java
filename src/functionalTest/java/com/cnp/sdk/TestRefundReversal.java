package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.GiftCardCardType;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.cnp.sdk.generate.RefundReversal;
import com.cnp.sdk.generate.RefundReversalResponse;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestRefundReversal {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleRefunfreversal() throws Exception {
     RefundReversal refundReversal=new RefundReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");

        refundReversal.setReportGroup("rptGrp");
        refundReversal.setId("id");



        refundReversal.setCnpTxnId(369852147l);
        refundReversal.setCard(giftCard);
        refundReversal.setOriginalRefCode("ref");
        refundReversal.setOriginalAmount(44455l);
        refundReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        refundReversal.setOriginalSystemTraceId(0);
        refundReversal.setOriginalSequenceNumber("333333");

        RefundReversalResponse response=cnp.refundReversal(refundReversal);
        assertEquals("Approved", response.getMessage());


    }



}
