package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.CnpTransactionInterface;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.Sale;
import io.github.vantiv.sdk.generate.SaleResponse;

public class TestBatchFileEncryption {

    private Properties config;
    private String preliveStatus;

    @Before
    public void setup() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(new Configuration().location()));
        String encUsername = properties.getProperty("encUsername");
        String encPassword = properties.getProperty("encPassword");
        String encSftpUsername = properties.getProperty("encSftpUsername");
        String encSftpPassword = properties.getProperty("encSftpPassword");
        String encMerchantId = properties.getProperty("encMerchantId");

        config = new Properties();
        config.setProperty("username", encUsername);
        config.setProperty("password", encPassword);
        config.setProperty("sftpUsername", encSftpUsername);
        config.setProperty("sftpPassword", encSftpPassword);
        config.setProperty("useEncryption", "true");
        config.setProperty("merchantId", encMerchantId);
        
        preliveStatus = System.getenv("preliveStatus");
        if (preliveStatus == null) {
            System.out.println("preliveStatus environment variable is not defined. Defaulting to down.");
            preliveStatus = "down";
        }
    }

    /**
     * NOTE: below tests are commented out until PGP key is renewed
     *


    @Test
    public void testSendToCnpSFTP_WithPreviouslyCreatedFile()
            throws Exception {

        Assume.assumeFalse(preliveStatus.equalsIgnoreCase("down"));
        
        String requestFileName = "cnpSdk-testBatchFile-fileConfigSFTP.xml";
        CnpBatchFileRequest request = new CnpBatchFileRequest(
                requestFileName, config);

        // request file is being set in the constructor
        assertNotNull(request.getFile());

        Properties configFromFile = request.getConfig();

        // pre-assert the config file has required param values
        assertEquals("payments.vantivprelive.com",
                configFromFile.getProperty("batchHost"));
        assertEquals("15000", configFromFile.getProperty("batchPort"));

        String workingDirRequests = configFromFile
                .getProperty("batchRequestFolder");
        prepDir(workingDirRequests);

        String workingDirResponses = configFromFile
                .getProperty("batchResponseFolder");
        prepDir(workingDirResponses);

        prepareTestRequest(request);

        // This should have generated the file
        request.prepareForDelivery();

        // Make sure the file exists
        File requestFile = request.getFile();
        assertTrue(requestFile.exists());
        assertTrue(requestFile.length() > 0);

        CnpBatchFileRequest request2 = new CnpBatchFileRequest(
                requestFileName, config);

        CnpBatchFileResponse response = request2.sendToCnpSFTP(true);

        // Assert response matches what was requested
        assertJavaApi(request2, response);

        // Make sure files were created correctly
        assertGeneratedFiles(workingDirRequests, workingDirResponses,
                requestFileName, request2, response);
    }

    @Test
    public void testSendOnlyToCnpSFTP_WithPreviouslyCreatedFile()
            throws Exception {

        Assume.assumeFalse(preliveStatus.equalsIgnoreCase("down"));
        
        // --- Prepare the batch file ---
        String requestFileName = "cnpSdk-testBatchFile-fileConfigSFTP.xml";
        CnpBatchFileRequest request1 = new CnpBatchFileRequest(
                requestFileName, config);

        // request file is being set in the constructor
        assertNotNull(request1.getFile());

        Properties configFromFile = request1.getConfig();

        // pre-assert the config file has required param values
        assertEquals("payments.vantivprelive.com",
                configFromFile.getProperty("batchHost"));
        assertEquals("15000", configFromFile.getProperty("batchPort"));

        String workingDirRequests = configFromFile
                .getProperty("batchRequestFolder");
        prepDir(workingDirRequests);

        String workingDirResponses = configFromFile
                .getProperty("batchResponseFolder");
        prepDir(workingDirResponses);

        prepareTestRequest(request1);

        // This should have generated the file
        request1.prepareForDelivery();

        // Make sure the file exists
        File requestFile1 = request1.getFile();
        assertTrue(requestFile1.exists());
        assertTrue(requestFile1.length() > 0);

        // --- Send the batch to Cnp's SFTP ---

        // Move request file to temporary location
        File requestFile2 = File.createTempFile("cnp", ".xml");
        copyFile(requestFile1, requestFile2);

        Properties configForRequest2 = (Properties) configFromFile.clone();
        configForRequest2.setProperty("batchRequestFolder", requestFile2
                .getParentFile().getCanonicalPath());

        CnpBatchFileRequest request2 = new CnpBatchFileRequest(
                requestFile2.getName(), configForRequest2);
        request2.sendOnlyToCnpSFTP(true);

        // --- Retrieve response ---
        CnpBatchFileRequest request3 = new CnpBatchFileRequest(
                requestFile2.getName(), configForRequest2);
        CnpBatchFileResponse response = request3.retrieveOnlyFromCnpSFTP();

        // Assert response matches what was requested
        assertJavaApi(request3, response);

        // Make sure files were created correctly
        assertGeneratedFiles(requestFile2.getParentFile().getCanonicalPath(),
                workingDirResponses, requestFile2.getName(), request3, response);
    }

    @Test
    public void testSendToCnpSFTP_WithFileConfig() throws Exception {

        Assume.assumeFalse(preliveStatus.equalsIgnoreCase("down"));
        
        String requestFileName = "cnpSdk-testBatchFile-fileConfigSFTP.xml";
        CnpBatchFileRequest request = new CnpBatchFileRequest(
                requestFileName, config);

        // request file is being set in the constructor
        assertNotNull(request.getFile());

        Properties configFromFile = request.getConfig();

        // pre-assert the config file has required param values
        assertEquals("payments.vantivprelive.com",
                configFromFile.getProperty("batchHost"));
        assertEquals("15000", configFromFile.getProperty("batchPort"));

        String workingDirRequests = configFromFile
                .getProperty("batchRequestFolder");
        prepDir(workingDirRequests);

        String workingDirResponses = configFromFile
                .getProperty("batchResponseFolder");
        prepDir(workingDirResponses);

        prepareTestRequest(request);

        /* call method under test */
    /**
        CnpBatchFileResponse response = request.sendToCnpSFTP();

        // assert response can be processed through Java API
        assertJavaApi(request, response);

        // assert request and response files were created properly
        assertGeneratedFiles(workingDirRequests, workingDirResponses,
                requestFileName, request, response);
    }
     */

    private void prepareTestRequest(CnpBatchFileRequest request) {

        Properties configFromFile = request.getConfig();

        CnpBatchRequest batchRequest1 = request.createBatch(configFromFile.getProperty("merchantId"));
        Sale sale11 = new Sale();
        sale11.setReportGroup("reportGroup11");
        sale11.setOrderId("orderId11");
        sale11.setAmount(1099L);
        sale11.setOrderSource(OrderSourceType.ECOMMERCE);
        sale11.setId("id");

        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4457010000000009");
        card.setExpDate("0114");
        card.setPin("1234");
        sale11.setCard(card);

        batchRequest1.addTransaction(sale11);
    }

    private void assertJavaApi(CnpBatchFileRequest request,
                               CnpBatchFileResponse response) {
        assertNotNull(response);
        assertNotNull(response.getCnpSessionId());
        assertEquals("0", response.getResponse());
        assertEquals("Valid Format", response.getMessage());
        //TODO: uncomment this assertion when prelive responds with XML v11.0
//        assertEquals(Versions.XML_VERSION, response.getVersion());

        CnpBatchResponse batchResponse1 = response
                .getNextCnpBatchResponse();
        assertNotNull(batchResponse1);
        assertNotNull(batchResponse1.getCnpBatchId());
        Properties configFromFile = request.getConfig();
        assertEquals(configFromFile.getProperty("merchantId"), batchResponse1.getMerchantId());

        CnpTransactionInterface txnResponse = batchResponse1
                .getNextTransaction();
        SaleResponse saleResponse11 = (SaleResponse) txnResponse;
        assertEquals("000", saleResponse11.getResponse());
        assertEquals("Approved", saleResponse11.getMessage());
        assertNotNull(saleResponse11.getCnpTxnId());
        assertEquals("orderId11", saleResponse11.getOrderId());
        assertEquals("reportGroup11", saleResponse11.getReportGroup());

        // assertNull("expected no more than one transaction in batchResponse1",
        // batchResponse1.getNextTransaction());
        //
        // assertNull("expected no more than one batch in file",
        // response.getNextCnpBatchResponse());
    }

    private void assertGeneratedFiles(String workingDirRequests,
                                      String workingDirResponses, String requestFileName,
                                      CnpBatchFileRequest request, CnpBatchFileResponse response)
            throws Exception {
        File fRequest = request.getFile();
        assertEquals(workingDirRequests + File.separator + requestFileName,
                fRequest.getAbsolutePath());
        assertTrue(fRequest.exists());
        assertTrue(fRequest.length() > 0);

        File fResponse = response.getFile();
        assertEquals(workingDirResponses + File.separator + requestFileName,
                fResponse.getAbsolutePath());
        assertTrue(fResponse.exists());
        assertTrue(fResponse.length() > 0);

        // assert contents of the response file by reading it through the Java
        // API again
        CnpBatchFileResponse responseFromFile = new CnpBatchFileResponse(
                fResponse);
        assertJavaApi(request, responseFromFile);
    }

    private void prepDir(String dirName) {
        File fRequestDir = new File(dirName);
        fRequestDir.mkdirs();
    }

    private void copyFile(File source, File destination) throws IOException {
        FileInputStream sourceIn = null;
        FileOutputStream destOut = null;
        try {
            sourceIn = new FileInputStream(source);
            destOut = new FileOutputStream(destination);

            byte[] buffer = new byte[2048];
            int bytesRead = -1;
            while ((bytesRead = sourceIn.read(buffer)) != -1) {
                destOut.write(buffer, 0, bytesRead);
            }
        } finally {
            if (sourceIn != null) {
                sourceIn.close();
            }
            if (destOut != null) {
                destOut.close();
            }
        }
    }
}