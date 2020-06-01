package com.cnp.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.CardType;
import com.cnp.sdk.generate.Credit;
import com.cnp.sdk.generate.Credit.Paypal;
import com.cnp.sdk.generate.CreditResponse;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.cnp.sdk.generate.OrderSourceType;
import com.cnp.sdk.generate.ProcessingInstructions;

public class TestCredit {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleCreditWithCard() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setOrderId("12344");
        credit.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        credit.setCard(card);
        credit.setId("id");
        CreditResponse response = cnp.credit(credit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test
    public void simpleCreditWithPaypal() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setOrderId("123456");
        credit.setOrderSource(OrderSourceType.ECOMMERCE);
        Paypal paypal = new Paypal();
        paypal.setPayerId("1234");
        credit.setPaypal(paypal);
        credit.setId("id");
        CreditResponse response = cnp.credit(credit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test
    public void simpleCreditWithCardAndSecondaryAmount() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setSecondaryAmount(20L);
        credit.setOrderId("12344");
        credit.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        credit.setCard(card);
        credit.setId("id");
        CreditResponse response = cnp.credit(credit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test
    public void simpleCreditWithTxnAndSecondaryAmount() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setSecondaryAmount(20L);
        credit.setCnpTxnId(1234L);
        credit.setId("id");
        CreditResponse response = cnp.credit(credit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test
    public void simpleCreditConflictWithTxnAndOrderId() throws Exception {
        Credit credit = new Credit();
        credit.setOrderId("12344");
        credit.setAmount(106L);
        credit.setSecondaryAmount(20L);
        credit.setCnpTxnId(1234L);
        credit.setId("id");
        try {
            cnp.credit(credit);
            fail("Cnp Txn and Order Id should conflict, fail to throw a exception");
        } catch (Exception e) {
            assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
        }
    }

    @Test
    public void paypalNotes() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setOrderId("12344");
        credit.setPayPalNotes("Hello");
        credit.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        credit.setCard(card);
        credit.setId("id");
        CreditResponse response = cnp.credit(credit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test
    public void processingInstructionAndAmexData() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(2000L);
        credit.setOrderId("12344");
        credit.setOrderSource(OrderSourceType.ECOMMERCE);
        ProcessingInstructions processinginstructions = new ProcessingInstructions();
        processinginstructions.setBypassVelocityCheck(true);
        credit.setProcessingInstructions(processinginstructions);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        credit.setCard(card);
        credit.setId("id");
        CreditResponse response = cnp.credit(credit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }
    
    @Test
    public void testCreditWithPin() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setSecondaryAmount(20L);
        credit.setCnpTxnId(1234L);
        credit.setId("id");
        credit.setPin("1234");
        CreditResponse response = cnp.credit(credit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

}
