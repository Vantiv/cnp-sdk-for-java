
package com.cnp.sdk;
import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.DepositReversal;
import com.cnp.sdk.generate.DepositReversalResponse;
import com.cnp.sdk.generate.GiftCardCardType;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestDepositReversal {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}

    @Test
    public void simpleDepositReversal() throws Exception {
        DepositReversal depositReversal=new DepositReversal();
     //   DeactivateReversal deactivateReversal=new DeactivateReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");

        depositReversal.setReportGroup("rptGrp");
        depositReversal.setId("id");



        depositReversal.setCnpTxnId(369852147l);
        depositReversal.setCard(giftCard);
        depositReversal.setOriginalRefCode("ref");
        depositReversal.setOriginalAmount(44455l);
        depositReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        depositReversal.setOriginalSystemTraceId(0);
        depositReversal.setOriginalSequenceNumber("333333");

        DepositReversalResponse response=cnp.depositReversal(depositReversal);
        assertEquals("Approved", response.getMessage());


    }



}
