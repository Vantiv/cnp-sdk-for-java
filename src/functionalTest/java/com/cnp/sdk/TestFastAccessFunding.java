package com.cnp.sdk;

import com.cnp.sdk.generate.CardType;
import com.cnp.sdk.generate.FastAccessFunding;

import com.cnp.sdk.generate.FastAccessFundingResponse;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFastAccessFunding {
    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void fastAccessFunding() throws Exception{
        FastAccessFunding fastAccessFunding = new FastAccessFunding();
        fastAccessFunding.setId("id");
        fastAccessFunding.setReportGroup("Planets");
        fastAccessFunding.setFundingSubmerchantId("2111");
        fastAccessFunding.setSubmerchantName("001");
        fastAccessFunding.setFundsTransferId("1234567891111111");
        fastAccessFunding.setAmount(20L);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4457010000000009");
        card.setExpDate("0114");
        fastAccessFunding.setCard(card);
        FastAccessFundingResponse response = cnp.fastAccessFunding(fastAccessFunding);
        assertEquals("Approved", response.getMessage());
        assertEquals("000", response.getResponse());
    }

    @Ignore("Sandbox does not check for mixed names. Production does check.")
    @Test(expected = CnpOnlineException.class)
    public void TestFastAccessFundingMixedNames() throws Exception {
        final FastAccessFunding fastAccessFunding = new FastAccessFunding();
        fastAccessFunding.setId("id");
        fastAccessFunding.setReportGroup("Planets");
        fastAccessFunding.setFundingSubmerchantId("2111");
        fastAccessFunding.setCustomerName("001");
        fastAccessFunding.setFundsTransferId("1234567891111111");
        fastAccessFunding.setAmount(20L);

        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4457010000000009");
        card.setExpDate("0114");
        fastAccessFunding.setCard(card);

        cnp.fastAccessFunding(fastAccessFunding);
    }
}