package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

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
}
