package com.cnp.sdk;

import com.cnp.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestVendor {
    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @org.junit.Test
    public void testVendorCredit() throws Exception{
        VendorCredit vcredit = new VendorCredit();
        vcredit.setReportGroup("vendorCredit");
        vcredit.setId("111");
        vcredit.setFundingSubmerchantId("vendorCredit");
        vcredit.setVendorName("Vendor101");
        vcredit.setFundsTransferId("1001");
        vcredit.setAmount(500l);

        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        vcredit.setAccountInfo(echeck);

        VendorCreditResponse response = cnp.vendorCredit(vcredit);
        assertEquals("Approved", response.getMessage());
    }

    @Test
    public void testVendorDebit() throws Exception{
        VendorDebit vdebit = new VendorDebit();
        vdebit.setReportGroup("VendorDebit");
        vdebit.setId("111");
        vdebit.setFundingSubmerchantId("VendorDebit");
        vdebit.setVendorName("Vendor101");
        vdebit.setFundsTransferId("1001");
        vdebit.setAmount(500l);

        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        vdebit.setAccountInfo(echeck);

        VendorDebitResponse response = cnp.vendorDebit(vdebit);
        assertEquals("Approved", response.getMessage());
    }
}
