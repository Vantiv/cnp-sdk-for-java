package com.cnp.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.cnp.sdk.generate.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TestCnpBatchRequest {

    private static CnpBatchFileRequest cnpBatchFileRequest;
    private static CnpBatchRequest cnpBatchRequest;
    private Properties property;

    @Before
    public void before() throws Exception {
        property = new Properties();
        property.setProperty("username", "USERNAME");
        property.setProperty("password", "password");
        property.setProperty("maxAllowedTransactionsPerFile", "13");
        property.setProperty("maxTransactionsPerBatch", "11");
        property.setProperty("batchHost", "localhost");
        property.setProperty("batchPort", "");
        property.setProperty("batchTcpTimeout", "10000");
        property.setProperty("batchUseSSL", "false");
        property.setProperty("merchantId", "101");
        property.setProperty("proxyHost", "");
        property.setProperty("proxyPort", "");
        property.setProperty("batchRequestFolder", "test/unit/");
        property.setProperty("batchResponseFolder", "test/unit/");
        property.setProperty("sftpUsername", "");
        property.setProperty("sftpPassword", "");
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);

        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);
        cnpBatchRequest.setNumOfTxn(1);
    }

    @Test
    public void testGetNumberOfTransactions() throws FileNotFoundException,
            JAXBException {
        assertEquals(1, cnpBatchRequest.getNumberOfTransactions());
        cnpBatchRequest.addTransaction(createTestSale(100L, "100"));
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testIsFull() throws FileNotFoundException, JAXBException {
        assertTrue(!cnpBatchRequest.isFull());
        for (int i = 0; i < Integer.valueOf(property
                .getProperty("maxTransactionsPerBatch")) - 1; i++) {
            cnpBatchRequest.addTransaction(createTestSale(100L, "100"));
        }
        assertTrue(cnpBatchRequest.isFull());
    }

    @Test
    public void testVerifyFileThresholds() throws FileNotFoundException,
            JAXBException {

        for (int i = 0; i < Integer.valueOf(property
                .getProperty("maxTransactionsPerBatch")) - 2; i++) {
            cnpBatchRequest.addTransaction(createTestSale(100L, "100"));
        }
        assertEquals(cnpBatchRequest.verifyFileThresholds(),
                TransactionCodeEnum.SUCCESS);
        cnpBatchRequest.addTransaction(createTestSale(100L, "100"));
        assertEquals(cnpBatchRequest.verifyFileThresholds(),
                TransactionCodeEnum.BATCHFULL);

        CnpBatchRequest batchRequest2 = cnpBatchFileRequest
                .createBatch("102");
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        batchRequest2.setMarshaller(mockMarshaller);
        batchRequest2.setNumOfTxn(1);

        batchRequest2.addTransaction(createTestSale(100L, "100"));
        assertEquals(cnpBatchRequest.verifyFileThresholds(),
                TransactionCodeEnum.FILEFULL);
    }

    @Test
    public void testAddTransaction() throws FileNotFoundException,
            JAXBException {
        for (int i = 0; i < Integer.valueOf(property
                .getProperty("maxTransactionsPerBatch")) - 2; i++) {
            assertEquals(cnpBatchRequest.addTransaction(createTestSale(100L,
                    "100")), TransactionCodeEnum.SUCCESS);
        }
        assertEquals(
                cnpBatchRequest
                        .addTransaction(createTestEcheckPreNoteSale("100")),
                TransactionCodeEnum.BATCHFULL);

        boolean batchFullException = false;
        try {
            cnpBatchRequest.addTransaction(createTestSale(100L, "100"));
        } catch (CnpBatchException e) {
            batchFullException = true;
        }

        assertTrue(batchFullException);

        CnpBatchRequest batchRequest2 = cnpBatchFileRequest
                .createBatch("102");
        batchRequest2.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        batchRequest2.setMarshaller(mockMarshaller);

        assertEquals(batchRequest2.addTransaction(createTestSale(100L, "100")),
                TransactionCodeEnum.FILEFULL);

        boolean fileFullException = false;
        try {
            batchRequest2.addTransaction(createTestSale(100L, "100"));
        } catch (CnpBatchException e) {
            fileFullException = true;
        }

        assertTrue(fileFullException);
    }

    private Sale createTestSale(Long amount, String orderId) {
        Sale sale = new Sale();
        sale.setAmount(amount);
        sale.setOrderId(orderId);
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000002");
        card.setExpDate("1210");
        sale.setCard(card);
        sale.setReportGroup("test");
        return sale;
    }

    private EcheckPreNoteSale createTestEcheckPreNoteSale(String orderId) {
        EcheckPreNoteSale echeckPreNoteSale = new EcheckPreNoteSale();
        // In unit test, we don't fill all the required fields
        echeckPreNoteSale.setOrderId(orderId);
        return echeckPreNoteSale;
    }

    @Test
    public void testAddSale() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        Sale sale = new Sale();
        sale.setAmount(25L);
        sale.setSecondaryAmount(10L);
        sale.setApplepay(createApplepay());
        cnpBatchRequest.addTransaction(sale);
        assertEquals(25, cnpBatchRequest.getBatchRequest().getSaleAmount()
                .intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest().getNumSales()
                .intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddAuth() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        Authorization auth = new Authorization();
        auth.setAmount(25L);
        auth.setSecondaryAmount(10L);
        auth.setApplepay(createApplepay());
        cnpBatchRequest.addTransaction(auth);
        assertEquals(25, cnpBatchRequest.getBatchRequest().getAuthAmount()
                .intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest().getNumAuths()
                .intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddCredit() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        Credit credit = new Credit();
        credit.setAmount(25L);
        credit.setSecondaryAmount(10L);
        cnpBatchRequest.addTransaction(credit);
        assertEquals(25, cnpBatchRequest.getBatchRequest().getCreditAmount()
                .intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest().getNumCredits()
                .intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddRegisterTokenRequestType() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        RegisterTokenRequestType registerTokenRequest = new RegisterTokenRequestType();
        registerTokenRequest.setApplepay(createApplepay());
        cnpBatchRequest.addTransaction(registerTokenRequest);
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumTokenRegistrations().intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddCaptureGivenAuth() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        CaptureGivenAuth captureGivenAuth = new CaptureGivenAuth();
        captureGivenAuth.setAmount(25L);
        captureGivenAuth.setSecondaryAmount(10L);
        cnpBatchRequest.addTransaction(captureGivenAuth);
        assertEquals(25, cnpBatchRequest.getBatchRequest()
                .getCaptureGivenAuthAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumCaptureGivenAuths().intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddForceCapture() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        ForceCapture forceCapture = new ForceCapture();
        forceCapture.setAmount(25L);
        forceCapture.setSecondaryAmount(10L);
        cnpBatchRequest.addTransaction(forceCapture);
        assertEquals(25, cnpBatchRequest.getBatchRequest()
                .getForceCaptureAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumForceCaptures().intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddAuthReversal() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        AuthReversal authReversal = new AuthReversal();
        authReversal.setAmount(25L);
        cnpBatchRequest.addTransaction(authReversal);
        assertEquals(25, cnpBatchRequest.getBatchRequest()
                .getAuthReversalAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumAuthReversals().intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddCapture() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        Capture capture = new Capture();
        capture.setAmount(25L);
        cnpBatchRequest.addTransaction(capture);
        assertEquals(25, cnpBatchRequest.getBatchRequest().getCaptureAmount()
                .intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest().getNumCaptures()
                .intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddEcheckVerification() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        EcheckVerification echeckVerification = new EcheckVerification();
        echeckVerification.setAmount(25L);
        cnpBatchRequest.addTransaction(echeckVerification);
        assertEquals(25, cnpBatchRequest.getBatchRequest()
                .getEcheckVerificationAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumEcheckVerification().intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddEcheckCredit() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        EcheckCredit echeckCredit = new EcheckCredit();
        echeckCredit.setAmount(25L);
        echeckCredit.setSecondaryAmount(10L);
        cnpBatchRequest.addTransaction(echeckCredit);
        assertEquals(25, cnpBatchRequest.getBatchRequest()
                .getEcheckCreditAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumEcheckCredit().intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddEcheckRedeposit() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        EcheckRedeposit echeckRedeposit = new EcheckRedeposit();
        cnpBatchRequest.addTransaction(echeckRedeposit);
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumEcheckRedeposit().intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddEcheckSale() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        EcheckSale echeckSale = new EcheckSale();
        echeckSale.setAmount(25L);
        echeckSale.setSecondaryAmount(10L);
        cnpBatchRequest.addTransaction(echeckSale);
        assertEquals(25, cnpBatchRequest.getBatchRequest()
                .getEcheckSalesAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest().getNumEcheckSales()
                .intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddUpdateCardValidationNumOnToken() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        UpdateCardValidationNumOnToken updateCardValidationNumOnToken = new UpdateCardValidationNumOnToken();
        cnpBatchRequest.addTransaction(updateCardValidationNumOnToken);
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumUpdateCardValidationNumOnTokens().intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddAccountUpdate() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        cnpBatchRequest.getBatchRequest().setNumAccountUpdates(
                BigInteger.valueOf(1l));
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        AccountUpdate accountUpdate = new AccountUpdate();
        cnpBatchRequest.addTransaction(accountUpdate);
        assertEquals(2, cnpBatchRequest.getBatchRequest()
                .getNumAccountUpdates().intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddCustomerCredit() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        CustomerCredit customerCredit = new CustomerCredit();
        customerCredit.setAmount(25L);
        cnpBatchRequest.addTransaction(customerCredit);
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumCustomerCredit().intValue());
        assertEquals(25, cnpBatchRequest.getBatchRequest().getCustomerCreditAmount()
                .intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddCustomerDebit() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        CustomerDebit customerDebit = new CustomerDebit();
        customerDebit.setAmount(25L);
        cnpBatchRequest.addTransaction(customerDebit);
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumCustomerDebit().intValue());
        assertEquals(25, cnpBatchRequest.getBatchRequest().getCustomerDebitAmount()
                .intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddPayoutOrgCredit() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        PayoutOrgCredit payoutOrgCredit  = new PayoutOrgCredit();
        payoutOrgCredit.setAmount(25L);
        cnpBatchRequest.addTransaction(payoutOrgCredit);
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumPayoutOrgCredit().intValue());
        assertEquals(25, cnpBatchRequest.getBatchRequest().getPayoutOrgCreditAmount()
                .intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testAddPayoutOrgDebit() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        PayoutOrgDebit payoutOrgDebit  = new PayoutOrgDebit();
        payoutOrgDebit.setAmount(25L);
        cnpBatchRequest.addTransaction(payoutOrgDebit);
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumPayoutOrgDebit().intValue());
        assertEquals(25, cnpBatchRequest.getBatchRequest().getPayoutOrgDebitAmount()
                .intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testPFIFInstructionTxn() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        SubmerchantCredit submerchantCredit = new SubmerchantCredit();
        submerchantCredit.setFundingSubmerchantId("12345");
        submerchantCredit.setSubmerchantName("submerchant");
        submerchantCredit.setFundsTransferId("1234567");
        submerchantCredit.setAmount(106L);
        submerchantCredit.setAccountInfo(new EcheckTypeCtx());
        cnpBatchRequest.addTransaction(submerchantCredit);

        VendorCredit vendorCredit = new VendorCredit();
        vendorCredit.setFundingSubmerchantId("12345");
        vendorCredit.setVendorName("vendor");
        vendorCredit.setFundsTransferId("1234567");
        vendorCredit.setAmount(107L);
        vendorCredit.setAccountInfo(new EcheckTypeCtx());
        cnpBatchRequest.addTransaction(vendorCredit);

        PayFacCredit payFacCredit = new PayFacCredit();
        payFacCredit.setFundingSubmerchantId("12345");
        payFacCredit.setFundsTransferId("1234567");
        payFacCredit.setAmount(108L);
        cnpBatchRequest.addTransaction(payFacCredit);

        ReserveCredit reserveCredit = new ReserveCredit();
        reserveCredit.setFundingSubmerchantId("12345");
        reserveCredit.setFundsTransferId("1234567");
        reserveCredit.setAmount(109L);
        cnpBatchRequest.addTransaction(reserveCredit);

        PhysicalCheckCredit physicalCheckCredit = new PhysicalCheckCredit();
        physicalCheckCredit.setFundingSubmerchantId("12345");
        physicalCheckCredit.setFundsTransferId("1234567");
        physicalCheckCredit.setAmount(110L);
        cnpBatchRequest.addTransaction(physicalCheckCredit);

        SubmerchantDebit submerchantDebit = new SubmerchantDebit();
        submerchantDebit.setFundingSubmerchantId("12345");
        submerchantDebit.setSubmerchantName("submerchant");
        submerchantDebit.setFundsTransferId("1234567");
        submerchantDebit.setAmount(106L);
        submerchantDebit.setAccountInfo(new EcheckTypeCtx());
        cnpBatchRequest.addTransaction(submerchantDebit);

        VendorDebit vendorDebit = new VendorDebit();
        vendorDebit.setFundingSubmerchantId("12345");
        vendorDebit.setVendorName("vendor");
        vendorDebit.setFundsTransferId("1234567");
        vendorDebit.setAmount(107L);
        vendorDebit.setAccountInfo(new EcheckTypeCtx());
        cnpBatchRequest.addTransaction(vendorDebit);

        PayFacDebit payFacDebit = new PayFacDebit();
        payFacDebit.setFundingSubmerchantId("12345");
        payFacDebit.setFundsTransferId("1234567");
        payFacDebit.setAmount(108L);
        cnpBatchRequest.addTransaction(payFacDebit);

        ReserveDebit reserveDebit = new ReserveDebit();
        reserveDebit.setFundingSubmerchantId("12345");
        reserveDebit.setFundsTransferId("1234567");
        reserveDebit.setAmount(109L);
        cnpBatchRequest.addTransaction(reserveDebit);

        PhysicalCheckDebit physicalCheckDebit = new PhysicalCheckDebit();
        physicalCheckDebit.setFundingSubmerchantId("12345");
        physicalCheckDebit.setFundsTransferId("1234567");
        physicalCheckDebit.setAmount(110L);
        cnpBatchRequest.addTransaction(physicalCheckDebit);

        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumSubmerchantCredit().intValue());
        assertEquals(106, cnpBatchRequest.getBatchRequest()
                .getSubmerchantCreditAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumVendorCredit().intValue());
        assertEquals(107, cnpBatchRequest.getBatchRequest()
                .getVendorCreditAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumPayFacCredit().intValue());
        assertEquals(108, cnpBatchRequest.getBatchRequest()
                .getPayFacCreditAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumReserveCredit().intValue());
        assertEquals(109, cnpBatchRequest.getBatchRequest()
                .getReserveCreditAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumPhysicalCheckCredit().intValue());
        assertEquals(110, cnpBatchRequest.getBatchRequest()
                .getPhysicalCheckCreditAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumSubmerchantDebit().intValue());
        assertEquals(106, cnpBatchRequest.getBatchRequest()
                .getSubmerchantDebitAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest().getNumVendorDebit()
                .intValue());
        assertEquals(107, cnpBatchRequest.getBatchRequest()
                .getVendorDebitAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest().getNumPayFacDebit()
                .intValue());
        assertEquals(108, cnpBatchRequest.getBatchRequest()
                .getPayFacDebitAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumReserveDebit().intValue());
        assertEquals(109, cnpBatchRequest.getBatchRequest()
                .getReserveDebitAmount().intValue());
        assertEquals(1, cnpBatchRequest.getBatchRequest()
                .getNumPhysicalCheckDebit().intValue());
        assertEquals(110, cnpBatchRequest.getBatchRequest()
                .getPhysicalCheckDebitAmount().intValue());

        assertEquals(11, cnpBatchRequest.getNumberOfTransactions());
    }

    @Test(expected = CnpBatchException.class)
    public void testAddAUBlock_AU_side() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");

        AccountUpdate accountUpdate = new AccountUpdate();
        cnpBatchRequest.setNumOfTxn(1);
        cnpBatchRequest.getBatchRequest().setNumAccountUpdates(
                BigInteger.valueOf(1l));
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        cnpBatchRequest.addTransaction(accountUpdate);

        assertEquals(2, cnpBatchRequest.getBatchRequest()
                .getNumAccountUpdates().intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());

        Sale sale = new Sale();
        sale.setAmount(5L);

        cnpBatchRequest.addTransaction(sale);
    }

    @Test(expected = CnpBatchException.class)
    public void testAddAUBlock_Sale_side() {
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile", property);
        cnpBatchRequest = cnpBatchFileRequest.createBatch("101");
        Sale sale = new Sale();
        sale.setAmount(5L);

        cnpBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        cnpBatchRequest.setMarshaller(mockMarshaller);

        cnpBatchRequest.addTransaction(sale);
        assertEquals(1, cnpBatchRequest.getBatchRequest().getNumSales()
                .intValue());
        assertEquals(5, cnpBatchRequest.getBatchRequest().getSaleAmount()
                .intValue());
        assertEquals(2, cnpBatchRequest.getNumberOfTransactions());

        AccountUpdate accountUpdate = new AccountUpdate();

        cnpBatchRequest.addTransaction(accountUpdate);
    }

    private ApplepayType createApplepay() {
        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType
                .setApplicationData("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType
                .setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType
                .setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData("user");
        applepayType.setSignature("sign");
        applepayType.setVersion("1");
        return applepayType;
    }

}
