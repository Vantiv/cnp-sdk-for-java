package io.github.vantiv.sdk;

//import com.cnp.sdk.generate.*;
import io.github.vantiv.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestVendor {
    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void testVendorCredit() throws Exception{
        VendorCredit vcredit = new VendorCredit();
        vcredit.setReportGroup("vendorCredit");
        vcredit.setId("111");
        vcredit.setFundingSubmerchantId("vendorCredit");
        vcredit.setVendorName("Vendor101");
        vcredit.setFundsTransferId("1001");
        vcredit.setAmount(1512l);
        Address adr=new Address();
        adr.setAddressLine1("100 MAIN ST1");
        adr.setAddressLine2("100 MAIN ST2");
        adr.setAddressLine3("100 MAIN ST3");
        adr.setCity("home town");
        adr.setState("MA");
        adr.setZip("01851");
        adr.setCountry(CountryTypeEnum.US);
        vcredit.setVendorAddress(adr);

        EcheckTypeCtx echeck = new EcheckTypeCtx();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        vcredit.setAccountInfo(echeck);

        VendorCreditResponse response = cnp.vendorCredit(vcredit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test
    public void testVendorCreditWithFundingCustomerId() throws Exception{
        VendorCredit vcredit = new VendorCredit();
        vcredit.setReportGroup("vendorCredit");
        vcredit.setId("111");
        vcredit.setFundingCustomerId("vendorDebit");
        vcredit.setVendorName("Vendor101");
        vcredit.setFundsTransferId("1001");
        vcredit.setAmount(1512l);

        EcheckTypeCtx echeck = new EcheckTypeCtx();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        vcredit.setAccountInfo(echeck);

        VendorCreditResponse response = cnp.vendorCredit(vcredit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test
    public void testVendorDebit() throws Exception {
        VendorDebit vdebit = new VendorDebit();
        vdebit.setReportGroup("vendorCredit");
        vdebit.setId("111");
        vdebit.setFundingSubmerchantId("vendorCredit");
        vdebit.setVendorName("Vendor101");
        vdebit.setFundsTransferId("1001");
        vdebit.setAmount(1512l);
        Address adr=new Address();
        adr.setAddressLine1("100 MAIN ST1");
        adr.setAddressLine2("100 MAIN ST2");
        adr.setAddressLine3("100 MAIN ST3");
        adr.setCity("home town");
        adr.setState("MA");
        adr.setZip("01851");
        adr.setCountry(CountryTypeEnum.US);
        vdebit.setVendorAddress(adr);

        EcheckTypeCtx echeck = new EcheckTypeCtx();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        vdebit.setAccountInfo(echeck);

        VendorDebitResponse response = cnp.vendorDebit(vdebit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }

    @Test
    public void testVendorDebitWithFundingCustomerId() throws Exception {
        VendorDebit vdebit = new VendorDebit();
        vdebit.setReportGroup("vendorDebit");
        vdebit.setId("111");
        vdebit.setFundingCustomerId("vendorDebit");
        vdebit.setVendorName("Vendor101");
        vdebit.setFundsTransferId("1001");
        vdebit.setAmount(1512l);

        EcheckTypeCtx echeck = new EcheckTypeCtx();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("123456789012");
        echeck.setRoutingNum("114567895");
        echeck.setCcdPaymentInformation("paymentInfo");

        vdebit.setAccountInfo(echeck);

        VendorDebitResponse response = cnp.vendorDebit(vdebit);
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }
}
