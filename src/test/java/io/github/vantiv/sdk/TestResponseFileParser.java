package io.github.vantiv.sdk;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TestResponseFileParser {
    @Test
    public void testIsTransactionResponse_authResponse() {
        assertTrue(ResponseFileParser.isTransactionResponse("authorizationResponse"));
    }

    @Test
    public void testIsTransactionResponse_authRequest() {
        assertFalse(ResponseFileParser.isTransactionResponse("authorization"));
    }

    @Test
    public void testIsTransactionResponse_cnpResponse() {
        assertFalse(ResponseFileParser.isTransactionResponse("cnpResponse"));
    }

    @Test
    public void testIsDepositTransactionResponse_depositTransactionReversalResponse() {
        assertTrue(ResponseFileParser.isTransactionResponse("depositTransactionReversalResponse"));
    }

    @Test
    public void testIsRefundTransactionResponse_refundTransactionReversalResponse() {
        assertTrue(ResponseFileParser.isTransactionResponse("refundTransactionReversalResponse"));
    }

    @Test
    public void testIsTransactionResponse_junk() {
        assertFalse(ResponseFileParser.isTransactionResponse("abc"));
    }

    @Test
    public void testIsTransactionResponse_authReversalResponse() {
        assertTrue(ResponseFileParser.isTransactionResponse("authReversalResponse"));
    }
}
