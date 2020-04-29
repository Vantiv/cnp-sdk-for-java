package com.cnp.sdk;

import com.cnp.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestSubmerchant {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void testSubmerchantCredit() throws Exception{
        SubmerchantCredit submerchantcredit = new SubmerchantCredit();
        submerchantcredit.setReportGroup("submerchantCredit");
        submerchantcredit.setId("111");
        submerchantcredit.setFundingSubmerchantId("submerchantCredit");
        submerchantcredit.setSubmerchantName("SubMerchant101");
        submerchantcredit.setFundsTransferId("1001");
        submerchantcredit.setAmount(1512l);

        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        submerchantcredit.setAccountInfo(echeck);
        submerchantcredit.setCustomIdentifier("SCFFISC");

        SubmerchantCreditResponse response = cnp.submerchantCredit(submerchantcredit);
        assertEquals("Approved", response.getMessage());
    }

    @Test
    public void testSubmerchantDebit() throws Exception{
        SubmerchantDebit submerchantdebit = new SubmerchantDebit();
        submerchantdebit.setReportGroup("submerchantdebit");
        submerchantdebit.setId("111");
        submerchantdebit.setFundingSubmerchantId("submerchantdebit");
        submerchantdebit.setSubmerchantName("SubMerchant101");
        submerchantdebit.setFundsTransferId("1001");
        submerchantdebit.setAmount(1512l);

        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        submerchantdebit.setAccountInfo(echeck);
        submerchantdebit.setCustomIdentifier("SCFFISC");

        SubmerchantDebitResponse response = cnp.submerchantDebit(submerchantdebit);
        assertEquals("Approved", response.getMessage());
    }

}
