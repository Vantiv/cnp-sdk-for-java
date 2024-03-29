package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.github.vantiv.sdk.generate.ApplepayHeaderType;
import io.github.vantiv.sdk.generate.ApplepayType;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.Sale;

public class TestCnpBatchFileRequest {

    private CnpBatchFileRequest cnpBatchFileRequest;

    @Before
    public void before() throws Exception {
        Properties property = new Properties();
        property.setProperty("username", "PHXMLTEST");
        property.setProperty("password", "password");
        property.setProperty("maxAllowedTransactionsPerFile", "1000");
        property.setProperty("maxTransactionsPerBatch", "500");
        property.setProperty("batchHost", "localhost");
        property.setProperty("batchPort", "2104");
        property.setProperty("batchTcpTimeout", "10000");
        property.setProperty("batchUseSSL", "false");
        property.setProperty("merchantId", "101");
        property.setProperty("proxyHost", "");
        property.setProperty("proxyPort", "");
        property.setProperty("reportGroup", "test");
        property.setProperty("batchRequestFolder", "test/unit/");
        property.setProperty("batchResponseFolder", "test/unit/");
        property.setProperty("sftpUsername", "sftp");
        property.setProperty("sftpPassword", "password");
        cnpBatchFileRequest = new CnpBatchFileRequest("testFile.xml", property);
    }

    @Test
    public void testInitializeMembers() throws Exception {
        Properties configToPass = new Properties();

        configToPass.setProperty("username", "usr1");
        configToPass.setProperty("password", "pass");

        cnpBatchFileRequest.initializeMembers("testFile.xml", configToPass);

        assertEquals(cnpBatchFileRequest.getConfig().getProperty("username"), "usr1");
        assertEquals(cnpBatchFileRequest.getConfig().getProperty("password"), "pass");
    }

    @Test
    public void testCreateBatchAndGetNumberOfBatches() throws FileNotFoundException, JAXBException {
        assertEquals(cnpBatchFileRequest.getNumberOfBatches(), 0);

        CnpBatchRequest testBatch = cnpBatchFileRequest.createBatch("101");
        assertNotNull(testBatch);

        assertEquals(cnpBatchFileRequest.getNumberOfBatches(), 1);
    }

    @Test
    public void testGetNumberOfTransactionInFile() throws FileNotFoundException, JAXBException {
        assertEquals(cnpBatchFileRequest.getNumberOfTransactionInFile(), 0);

        CnpBatchRequest testBatch = cnpBatchFileRequest.createBatch("101");
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        testBatch.setMarshaller(mockMarshaller);
        testBatch.setNumOfTxn(1);
        testBatch.addTransaction(createTestSale(101L,"101"));
        testBatch.addTransaction(createTestSale(102L,"102"));
        testBatch.addTransaction(createTestSale(103L,"103"));

        CnpBatchRequest testBatch2 = cnpBatchFileRequest.createBatch("101");
        testBatch2.setMarshaller(mockMarshaller);
        testBatch2.setNumOfTxn(1);


        testBatch2.addTransaction(createTestSale(104L,"104"));
        testBatch2.addTransaction(createTestSale(105L,"105"));
        testBatch2.addTransaction(createTestSale(106L,"106"));

        assertEquals(cnpBatchFileRequest.getNumberOfTransactionInFile(), 8);
    }

    @Test
    public void testIsEmpty() throws FileNotFoundException, JAXBException{
        // right off from scratch, batchFileRequest should have no records.
        assertTrue(cnpBatchFileRequest.isEmpty());

        CnpBatchRequest testBatch = cnpBatchFileRequest.createBatch("101");
        testBatch.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        testBatch.setMarshaller(mockMarshaller);

        testBatch.addTransaction(createTestSale(101L,"101"));

        assertTrue(!cnpBatchFileRequest.isEmpty());
    }

    @Test
    public void testIsFull() throws FileNotFoundException, JAXBException{
        // right off from scratch, batchFileRequest should have no records and hence not full.
        Properties property = new Properties();
        property.setProperty("username", "PHXMLTEST");
        property.setProperty("password", "password");
        property.setProperty("maxAllowedTransactionsPerFile", "4");
        property.setProperty("maxTransactionsPerBatch", "4");
        property.setProperty("batchHost", "localhost");
        property.setProperty("batchPort", "2104");
        property.setProperty("batchTcpTimeout", "10000");
        property.setProperty("batchUseSSL", "false");
        property.setProperty("proxyHost", "");
        property.setProperty("proxyPort", "");
        property.setProperty("merchantId", "101");
        property.setProperty("batchRequestFolder", "test/unit/requestFolder/");
        property.setProperty("batchResponseFolder", "test/unit/responseFolder/");
        property.setProperty("sftpUsername","");
        property.setProperty("sftpPassword","");

        cnpBatchFileRequest = new CnpBatchFileRequest("testFile.xml", property);

        assertTrue(!cnpBatchFileRequest.isFull());

        CnpBatchRequest testBatch = cnpBatchFileRequest.createBatch("101");
        testBatch.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        testBatch.setMarshaller(mockMarshaller);


        testBatch.addTransaction(createTestSale(101L,"101"));
        testBatch.addTransaction(createTestSale(102L,"102"));
        testBatch.addTransaction(createTestSaleWithApplepayAndSecondaryAmount(103L, 10L, "user", "103"));

        assertTrue(cnpBatchFileRequest.isFull());
    }

    public Sale createTestSale(Long amount, String orderId){
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

    public Sale createTestSaleWithApplepayAndSecondaryAmount(Long amount, Long secAmount, String applepayData, String orderId){
        Sale sale = new Sale();
        sale.setAmount(amount);
        sale.setSecondaryAmount(secAmount);
        sale.setOrderId(orderId);
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType.setApplicationData("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData(applepayData);
        applepayType.setSignature("sign");
        applepayType.setVersion("1");
        sale.setApplepay(applepayType);
        sale.setReportGroup("test");
        return sale;
    }
}