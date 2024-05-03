package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import io.github.vantiv.sdk.generate.GiftCardCardType;
import io.github.vantiv.sdk.generate.LoadReversal;
import io.github.vantiv.sdk.generate.LoadReversalResponse;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import org.junit.BeforeClass;
import org.junit.Test;


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
        XMLGregorianCalendar timestamp = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
        loadReversal.setOriginalTxnTime(timestamp);
        loadReversal.setOriginalSystemTraceId(0);
        loadReversal.setOriginalSequenceNumber("333333");

        LoadReversalResponse response=cnp.loadReversal(loadReversal);
      //  DeactivateReversalResponse response=cnp.deactivateReversal(deactivateReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());


    }

}
