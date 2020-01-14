package com.cnp.sdk;

import com.cnp.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestReserve {
    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void testReserveCredit() throws Exception{
        ReserveCredit rcredit = new ReserveCredit();
        rcredit.setReportGroup("reserveCredit");
        rcredit.setId("111");
        rcredit.setFundingSubmerchantId("reserveCredit");
        rcredit.setFundsTransferId("1001");
        rcredit.setAmount(500l);

        ReserveCreditResponse response = cnp.submerchantCredit(rcredit);
        assertEquals("The account number was changed", response.getMessage());
    }

    @Test
    public void testReserveCreditFundingCustomerId() throws Exception{
        ReserveCredit rcredit = new ReserveCredit();
        rcredit.setReportGroup("reserveCredit");
        rcredit.setId("111");
        rcredit.setFundingCustomerId("reserveCredit");
        rcredit.setFundsTransferId("1001");
        rcredit.setAmount(500l);

        ReserveCreditResponse response = cnp.submerchantCredit(rcredit);
        assertEquals("The account number was changed", response.getMessage());
    }

    @Test
    public void testReserveDebit() throws Exception{
        ReserveDebit rdebit = new ReserveDebit();
        rdebit.setReportGroup("reserveDebit");
        rdebit.setId("111");
        rdebit.setFundingSubmerchantId("reserveDebit");
        rdebit.setFundsTransferId("1001");
        rdebit.setAmount(500l);

        ReserveDebitResponse response = cnp.submerchantDebit(rdebit);
        assertEquals("The account number was changed", response.getMessage());
    }

    @Test
    public void testReserveDebitFundingCustomerId() throws Exception{
        ReserveDebit rdebit = new ReserveDebit();
        rdebit.setReportGroup("reserveDebit");
        rdebit.setId("111");
        rdebit.setFundingCustomerId("reserveDebit");
        rdebit.setFundsTransferId("1001");
        rdebit.setAmount(500l);

        ReserveDebitResponse response = cnp.submerchantDebit(rdebit);
        assertEquals("The account number was changed", response.getMessage());
    }

    @Test
    public void testReserveDebitXMLCharacters() throws Exception{
        ReserveDebit rdebit = new ReserveDebit();
        rdebit.setReportGroup("reserveDebit");
        rdebit.setId("111");
        rdebit.setFundingSubmerchantId("reser<veD>ebit");
        rdebit.setFundsTransferId("1001");
        rdebit.setAmount(500l);

        ReserveDebitResponse response = cnp.submerchantDebit(rdebit);
        assertEquals("The account number was changed", response.getMessage());
    }
}
