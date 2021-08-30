package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.CustomBilling;
import io.github.vantiv.sdk.generate.EnhancedData;
import io.github.vantiv.sdk.generate.LodgingInfo;
import io.github.vantiv.sdk.generate.ProcessingInstructions;
import io.github.vantiv.sdk.generate.RefundTransactionReversal;
import io.github.vantiv.sdk.generate.RefundTransactionReversalResponse;

public class TestRefundTransactionReversal {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleTransactionReversal() {
        RefundTransactionReversal refundTransactionReversal = new RefundTransactionReversal();
        refundTransactionReversal.setId("id");
        refundTransactionReversal.setCnpTxnId(124785L);

        RefundTransactionReversalResponse response = cnp.refundTransactionReversal(refundTransactionReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
        assertEquals("000", response.getResponse());
        assertEquals("id", response.getId());
        assertEquals(124785L, response.getRecyclingResponse().getCreditCnpTxnId().longValue());
    }

    @Test
    public void filledTransactionReversal() {
        RefundTransactionReversal refundTransactionReversal = new RefundTransactionReversal();
        refundTransactionReversal.setId("id123");
        refundTransactionReversal.setCnpTxnId(987654L);
        refundTransactionReversal.setAmount(4321L);
        refundTransactionReversal.setCustomBilling(new CustomBilling());
        refundTransactionReversal.setEnhancedData(new EnhancedData());
        refundTransactionReversal.setLodgingInfo(new LodgingInfo());
        refundTransactionReversal.setPin("1234");
        refundTransactionReversal.setProcessingInstructions(new ProcessingInstructions());
        refundTransactionReversal.setSurchargeAmount(6789L);

        RefundTransactionReversalResponse response = cnp.refundTransactionReversal(refundTransactionReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
        assertEquals("000", response.getResponse());
        assertEquals("id123", response.getId());
        assertEquals(987654L, response.getRecyclingResponse().getCreditCnpTxnId().longValue());
    }
}
