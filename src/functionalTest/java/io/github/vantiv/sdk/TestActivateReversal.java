package io.github.vantiv.sdk;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import io.github.vantiv.sdk.generate.ActivateReversal;
import io.github.vantiv.sdk.generate.ActivateReversalResponse;
import io.github.vantiv.sdk.generate.GiftCardCardType;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import org.junit.BeforeClass;
import org.junit.Test;

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
