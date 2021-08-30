package io.github.vantiv.sdk;

//import com.cnp.sdk.generate.*;
import io.github.vantiv.sdk.generate.PhysicalCheckCredit;
import io.github.vantiv.sdk.generate.PhysicalCheckCreditResponse;
import io.github.vantiv.sdk.generate.PhysicalCheckDebit;
import io.github.vantiv.sdk.generate.PhysicalCheckDebitResponse;
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
        assertEquals("sandbox", response.getLocation());
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
        assertEquals("sandbox", response.getLocation());
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
        assertEquals("sandbox", response.getLocation());
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
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }
}
