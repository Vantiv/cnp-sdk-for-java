package com.cnp.sdk;

import com.cnp.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestPhysicalCheck {
    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void testPhysicalCheckCredit() throws Exception{
        PhysicalCheckCredit pccredit = new PhysicalCheckCredit();
        pccredit.setReportGroup("physicalCheckCredit");
        pccredit.setId("111");
        pccredit.setFundingSubmerchantId("physicalCheckCredit");
        pccredit.setFundsTransferId("1001");
        pccredit.setAmount(1512l);

        PhysicalCheckCreditResponse response = cnp.physicalCheckCredit(pccredit);
        assertEquals("Approved", response.getMessage());
    }

    @Test
    public void testPhysicalCheckCreditWithFundingCustomerId() throws Exception{
        PhysicalCheckCredit pccredit = new PhysicalCheckCredit();
        pccredit.setReportGroup("physicalCheckCredit");
        pccredit.setId("111");
        pccredit.setFundingCustomerId("physicalCheckCredit");
        pccredit.setFundsTransferId("1001");
        pccredit.setAmount(1512l);

        PhysicalCheckCreditResponse response = cnp.physicalCheckCredit(pccredit);
        assertEquals("Approved", response.getMessage());
    }

    @Test
    public void testPhysicalCheckDebit() throws Exception{
        PhysicalCheckDebit pcdebit = new PhysicalCheckDebit();
        pcdebit.setReportGroup("physicalCheckDebit");
        pcdebit.setId("111");
        pcdebit.setFundingSubmerchantId("physicalCheckDebit");
        pcdebit.setFundsTransferId("1001");
        pcdebit.setAmount(1512l);

        PhysicalCheckDebitResponse response = cnp.physicalCheckDebit(pcdebit);
        assertEquals("Approved", response.getMessage());
    }

    @Test
    public void testPhysicalCheckDebitWithFundingCustomerId() throws Exception{
        PhysicalCheckDebit pcdebit = new PhysicalCheckDebit();
        pcdebit.setReportGroup("physicalCheckDebit");
        pcdebit.setId("111");
        pcdebit.setFundingCustomerId("physicalCheckDebit");
        pcdebit.setFundsTransferId("1001");
        pcdebit.setAmount(1512l);

        PhysicalCheckDebitResponse response = cnp.physicalCheckDebit(pcdebit);
        assertEquals("The account number was changed", response.getMessage());
    }
}
