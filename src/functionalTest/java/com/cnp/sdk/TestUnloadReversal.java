package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.GiftCardCardType;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.cnp.sdk.generate.UnloadReversal;
import com.cnp.sdk.generate.UnloadReversalResponse;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestUnloadReversal {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleUnloadReversal() throws Exception {
     UnloadReversal unloadReversal=new UnloadReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");

        unloadReversal.setReportGroup("rptGrp");
        unloadReversal.setId("id");

        unloadReversal.setCnpTxnId(369852147l);
        unloadReversal.setCard(giftCard);
        unloadReversal.setOriginalRefCode("ref");
        unloadReversal.setOriginalAmount(44455l);
        unloadReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        unloadReversal.setOriginalSystemTraceId(0);
        unloadReversal.setOriginalSequenceNumber("333333");

        UnloadReversalResponse response=cnp.unloadReversal(unloadReversal);
        assertEquals("Approved", response.getMessage());
    }

}
