package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.CustomBilling;
import io.github.vantiv.sdk.generate.EnhancedData;
import io.github.vantiv.sdk.generate.LodgingInfo;
import io.github.vantiv.sdk.generate.ProcessingInstructions;
import io.github.vantiv.sdk.generate.DepositTransactionReversal;
import io.github.vantiv.sdk.generate.DepositTransactionReversalResponse;

public class TestDepositTransactionReversal {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleTransactionReversal() {
        DepositTransactionReversal depositTransactionReversal = new DepositTransactionReversal();
        depositTransactionReversal.setId("id");
        depositTransactionReversal.setCnpTxnId(124785L);

        DepositTransactionReversalResponse response = cnp.depositTransactionReversal(depositTransactionReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
        assertEquals("000", response.getResponse());
        assertEquals("id", response.getId());
        assertEquals(124785L, response.getRecyclingResponse().getCreditCnpTxnId().longValue());
    }

    @Test
    public void filledTransactionReversal() {
        DepositTransactionReversal depositTransactionReversal = new DepositTransactionReversal();
        depositTransactionReversal.setId("id123");
        depositTransactionReversal.setCnpTxnId(987654L);
        depositTransactionReversal.setAmount(4321L);
        depositTransactionReversal.setCustomBilling(new CustomBilling());
        depositTransactionReversal.setEnhancedData(new EnhancedData());
        depositTransactionReversal.setLodgingInfo(new LodgingInfo());
        depositTransactionReversal.setPin("1234");
        depositTransactionReversal.setProcessingInstructions(new ProcessingInstructions());
        depositTransactionReversal.setSurchargeAmount(6789L);

        DepositTransactionReversalResponse response = cnp.depositTransactionReversal(depositTransactionReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
        assertEquals("000", response.getResponse());
        assertEquals("id123", response.getId());
        assertEquals(987654L, response.getRecyclingResponse().getCreditCnpTxnId().longValue());
    }
}
