package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import io.github.vantiv.sdk.generate.GiftCardCardType;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.UnloadReversal;
import io.github.vantiv.sdk.generate.UnloadReversalResponse;
import org.junit.BeforeClass;
import org.junit.Test;


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
        XMLGregorianCalendar timestamp = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
        unloadReversal.setOriginalTxnTime(timestamp);
        unloadReversal.setOriginalSystemTraceId(0);
        unloadReversal.setOriginalSequenceNumber("333333");

        UnloadReversalResponse response=cnp.unloadReversal(unloadReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

}
