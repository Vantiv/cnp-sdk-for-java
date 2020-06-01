package com.cnp.sdk;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.ActivateReversal;
import com.cnp.sdk.generate.ActivateReversalResponse;
import com.cnp.sdk.generate.GiftCardCardType;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestActivateReversal {
    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleActivate() throws Exception {
        ActivateReversal activateReversal=new ActivateReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");

        activateReversal.setReportGroup("rptGrp");
        activateReversal.setId("id");

        activateReversal.setCard(giftCard);
        activateReversal.setOriginalRefCode("ref");
        activateReversal.setOriginalAmount(44455L);
        activateReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        activateReversal.setOriginalSystemTraceId(0);
        activateReversal.setOriginalSequenceNumber("333333");

        ActivateReversalResponse response=cnp.activateReversal(activateReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

}
