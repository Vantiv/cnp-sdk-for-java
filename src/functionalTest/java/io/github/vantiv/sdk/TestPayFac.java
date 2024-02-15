package io.github.vantiv.sdk;

import io.github.vantiv.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestPayFac {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void testPayFacCredit() throws Exception {
        PayFacCredit payfac = new PayFacCredit();
        payfac.setReportGroup("payFacCredit");
        payfac.setId("111");
        payfac.setFundingSubmerchantId("2345016400556org1200");
        payfac.setFundsTransferId("1201b");
        payfac.setAmount(1000L);
        PayFacCreditResponse response = cnp.payFacCredit(payfac);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test
    public void testPayFacDebit() throws Exception {
        PayFacDebit payfac = new PayFacDebit();
        payfac.setReportGroup("payFacDebit");
        payfac.setId("112");
        payfac.setFundingSubmerchantId("2345016400556org1200");
        payfac.setFundsTransferId("1201a");
        payfac.setAmount(1000L);
        PayFacDebitResponse response = cnp.payFacDebit(payfac);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test
    public void testPayFacCreditAmountNegative() throws Exception {
        try {
            PayFacCredit payfac = new PayFacCredit();
            payfac.setReportGroup("payFacCredit");
            payfac.setId("111");
            payfac.setFundingSubmerchantId("2345016400556org1200");
            payfac.setFundsTransferId("1201b");
            payfac.setAmount(99999999999L); // invalid amount value, number of total digits has been limited to 10.
            PayFacCreditResponse response = cnp.payFacCredit(payfac);
            assertEquals("payFacCredit",response.getReportGroup());
        }
        catch(CnpOnlineException e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith("Error validating xml data against the schema"));
        }
    }
}
