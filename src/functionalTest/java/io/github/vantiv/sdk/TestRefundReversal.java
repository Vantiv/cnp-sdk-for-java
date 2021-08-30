package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import io.github.vantiv.sdk.generate.GiftCardCardType;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.RefundReversal;
import io.github.vantiv.sdk.generate.RefundReversalResponse;
import org.junit.BeforeClass;
import org.junit.Test;

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

        refundReversal.setCnpTxnId(369852147L);
        refundReversal.setCard(giftCard);
        refundReversal.setOriginalRefCode("ref");
        refundReversal.setOriginalAmount(44455L);
        refundReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        refundReversal.setOriginalSystemTraceId(0);
        refundReversal.setOriginalSequenceNumber("333333");

        RefundReversalResponse response=cnp.refundReversal(refundReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());


    }

}
