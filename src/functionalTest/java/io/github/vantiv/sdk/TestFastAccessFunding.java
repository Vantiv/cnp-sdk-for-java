package io.github.vantiv.sdk;

import io.github.vantiv.sdk.generate.Address;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.CountryTypeEnum;
import io.github.vantiv.sdk.generate.FastAccessFunding;

import io.github.vantiv.sdk.generate.FastAccessFundingResponse;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;

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
        Address adr=new Address();
        adr.setAddressLine1("100 MAIN ST1");
        adr.setAddressLine2("100 MAIN ST2");
        adr.setAddressLine3("100 MAIN ST3");
        adr.setCity("home town");
        adr.setState("MA");
        adr.setZip("01851");
        adr.setCountry(CountryTypeEnum.US);
        fastAccessFunding.setCardholderAddress(adr);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4457010000000009");
        card.setExpDate("0114");
        fastAccessFunding.setCard(card);
        FastAccessFundingResponse response = cnp.fastAccessFunding(fastAccessFunding);
        assertEquals("Approved", response.getMessage());
        assertEquals("000", response.getResponse());
        assertEquals("sandbox", response.getLocation());
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