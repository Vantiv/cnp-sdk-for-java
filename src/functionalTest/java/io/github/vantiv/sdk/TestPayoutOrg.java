package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import io.github.vantiv.sdk.generate.PayoutOrgCredit;
import io.github.vantiv.sdk.generate.PayoutOrgCreditResponse;
import io.github.vantiv.sdk.generate.PayoutOrgDebit;
import io.github.vantiv.sdk.generate.PayoutOrgDebitResponse;

public class TestPayoutOrg {
    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void TestPayoutOrgCredit() throws Exception {
        PayoutOrgCredit pocredit = new PayoutOrgCredit();
        pocredit.setReportGroup("payoutOrgCredit");
        pocredit.setId("111");
        pocredit.setFundingCustomerId("payoutOrgCredit");
        pocredit.setFundsTransferId("1001");
        pocredit.setAmount(1512l);

        PayoutOrgCreditResponse response = cnp.payoutOrgCredit(pocredit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test(expected = CnpOnlineException.class)
    public void TestPayoutOrgCreditNullFundingCustomerId() throws Exception {
        PayoutOrgCredit pocredit = new PayoutOrgCredit();
        pocredit.setReportGroup("payoutOrgCredit");
        pocredit.setId("111");
        pocredit.setFundsTransferId("1001");
        pocredit.setAmount(1512l);

        cnp.payoutOrgCredit(pocredit);
    }

    @Test
    public void TestPayoutOrgDebit() throws Exception {
        PayoutOrgDebit podebit = new PayoutOrgDebit();
        podebit.setReportGroup("payoutOrgDebit");
        podebit.setId("111");
        podebit.setFundingCustomerId("payoutOrgDebit");
        podebit.setFundsTransferId("1001");
        podebit.setAmount(1512l);

        PayoutOrgDebitResponse response = cnp.payoutOrgDebit(podebit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test(expected = CnpOnlineException.class)
    public void TestPayoutOrgDebitNullFundingCustomerId() throws Exception {
        PayoutOrgDebit podebit = new PayoutOrgDebit();
        podebit.setReportGroup("payoutOrgDebit");
        podebit.setId("111");
        podebit.setFundsTransferId("1001");
        podebit.setAmount(1512l);

        cnp.payoutOrgDebit(podebit);
    }

    @Test(expected = CnpOnlineException.class)
    public void TestPayoutOrgDebitFundingCustomerIdTooLong() throws Exception {
        PayoutOrgDebit podebit = new PayoutOrgDebit();
        podebit.setReportGroup("payoutOrgDebit");
        podebit.setId("111");
        podebit.setFundingCustomerId("012345678901234567890123456789012345678901234567890123456789");
        podebit.setFundsTransferId("1001");
        podebit.setAmount(1512l);

        PayoutOrgDebitResponse response = cnp.payoutOrgDebit(podebit);
        assertEquals("Approved", response.getMessage());
    }
}