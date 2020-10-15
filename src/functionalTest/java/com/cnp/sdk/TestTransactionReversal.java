package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.generate.CustomBilling;
import com.cnp.sdk.generate.EnhancedData;
import com.cnp.sdk.generate.LodgingInfo;
import com.cnp.sdk.generate.ProcessingInstructions;
import com.cnp.sdk.generate.TransactionReversal;
import com.cnp.sdk.generate.TransactionReversalResponse;

public class TestTransactionReversal {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleTransactionReversal() {
        TransactionReversal transactionReversal = new TransactionReversal();
        transactionReversal.setId("id");
        transactionReversal.setCnpTxnId(124785L);

        TransactionReversalResponse response = cnp.transactionReversal(transactionReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
        assertEquals("000", response.getResponse());
        assertEquals("id", response.getId());
        assertEquals(124785L, response.getRecyclingResponse().getCreditCnpTxnId().longValue());
    }

    @Test
    public void filledTransactionReversal() {
        TransactionReversal transactionReversal = new TransactionReversal();
        transactionReversal.setId("id123");
        transactionReversal.setCnpTxnId(987654L);
        transactionReversal.setAmount(4321L);
        transactionReversal.setCustomBilling(new CustomBilling());
        transactionReversal.setEnhancedData(new EnhancedData());
        transactionReversal.setLodgingInfo(new LodgingInfo());
        transactionReversal.setPin("1234");
        transactionReversal.setProcessingInstructions(new ProcessingInstructions());
        transactionReversal.setSurchargeAmount(6789L);

        TransactionReversalResponse response = cnp.transactionReversal(transactionReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
        assertEquals("000", response.getResponse());
        assertEquals("id123", response.getId());
        assertEquals(987654L, response.getRecyclingResponse().getCreditCnpTxnId().longValue());
    }
}
