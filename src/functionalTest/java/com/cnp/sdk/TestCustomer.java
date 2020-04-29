package com.cnp.sdk;

import com.cnp.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCustomer {
    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void testCustomerCredit() throws Exception {
        CustomerCredit ccredit = new CustomerCredit();
        ccredit.setReportGroup("customerCredit");
        ccredit.setId("111");
        ccredit.setFundingCustomerId("customerCredit");
        ccredit.setCustomerName("Customer101");
        ccredit.setFundsTransferId("1001");
        ccredit.setAmount(1512l);

        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        ccredit.setAccountInfo(echeck);

        CustomerCreditResponse response = cnp.customerCredit(ccredit);
        assertEquals("Approved", response.getMessage());
    }

    @Test(expected = CnpOnlineException.class)
    public void testCustomerCreditNullName() throws Exception {
        CustomerCredit ccredit = new CustomerCredit();
        ccredit.setReportGroup("customerCredit");
        ccredit.setId("111");
        ccredit.setFundingCustomerId("customerCredit");
        ccredit.setFundsTransferId("1001");
        ccredit.setAmount(1512l);

        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        ccredit.setAccountInfo(echeck);

        cnp.customerCredit(ccredit);
    }

    @Test
    public void testCustomerDebit() throws Exception {
        CustomerDebit cdebit = new CustomerDebit();
        cdebit.setReportGroup("customerDebit");
        cdebit.setId("111");
        cdebit.setFundingCustomerId("customerDebit");
        cdebit.setCustomerName("Customer101");
        cdebit.setFundsTransferId("1001");
        cdebit.setAmount(1512l);

        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        cdebit.setAccountInfo(echeck);

        CustomerDebitResponse response = cnp.customerDebit(cdebit);
        assertEquals("Approved", response.getMessage());
    }

    @Test(expected = CnpOnlineException.class)
    public void testCustomerDebitNullName() throws Exception {
        CustomerDebit cdebit = new CustomerDebit();
        cdebit.setReportGroup("customerDebit");
        cdebit.setId("111");
        cdebit.setFundingCustomerId("customerDebit");
        cdebit.setFundsTransferId("1001");
        cdebit.setAmount(1512l);

        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        cdebit.setAccountInfo(echeck);

        CustomerDebitResponse response = cnp.customerDebit(cdebit);
    }

    @Ignore("Sandbox does not check for negative amounts. Production does check.")
    @Test(expected = CnpOnlineException.class)
    public void testCustomerDebitNegativeAmount() throws Exception {
        CustomerDebit cdebit = new CustomerDebit();
        cdebit.setReportGroup("customerDebit");
        cdebit.setId("111");
        cdebit.setFundingCustomerId("customerDebit");
        cdebit.setCustomerName("Customer101");
        cdebit.setFundsTransferId("1001");
        cdebit.setAmount(-1512l);

        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        cdebit.setAccountInfo(echeck);

        cnp.customerDebit(cdebit);
    }

    @Test(expected = CnpOnlineException.class)
    public void testCustomerDebitIdTooLong() throws Exception {
        CustomerDebit cdebit = new CustomerDebit();
        cdebit.setReportGroup("customerDebit");
        cdebit.setId("111");
        cdebit.setFundingCustomerId("123456789012345678901234567890123456789012345678901234567890");
        cdebit.setCustomerName("Customer101");
        cdebit.setFundsTransferId("1001");
        cdebit.setAmount(1512l);

        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        cdebit.setAccountInfo(echeck);

        CustomerDebitResponse response = cnp.customerDebit(cdebit);
    }
}