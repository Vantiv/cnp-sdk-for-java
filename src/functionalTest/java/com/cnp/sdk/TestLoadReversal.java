package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.GiftCardCardType;
import com.cnp.sdk.generate.LoadReversal;
import com.cnp.sdk.generate.LoadReversalResponse;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestLoadReversal {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleLoad() throws Exception {
     LoadReversal loadReversal=new LoadReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");

        loadReversal.setReportGroup("rptGrp");
        loadReversal.setId("id");

        loadReversal.setCnpTxnId(369852147L);
        loadReversal.setCard(giftCard);
        loadReversal.setOriginalRefCode("ref");
        loadReversal.setOriginalAmount(44455L);
        loadReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        loadReversal.setOriginalSystemTraceId(0);
        loadReversal.setOriginalSequenceNumber("333333");

        LoadReversalResponse response=cnp.loadReversal(loadReversal);
      //  DeactivateReversalResponse response=cnp.deactivateReversal(deactivateReversal);
        assertEquals("Approved", response.getMessage());


    }

}
