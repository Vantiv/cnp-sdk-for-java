package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

//import com.cnp.sdk.generate.*;
import java.util.Properties;

import io.github.vantiv.sdk.generate.*;

import org.hamcrest.MatcherAssert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class TestQueryTransaction {

    private static CnpOnline cnp;
    private static Properties p;
    private static CnpOnline cnpOnlineMock;
    QueryTransaction queryTransaction;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
        p= Mockito.mock(Properties.class);
        cnpOnlineMock=Mockito.mock(CnpOnline.class);
    }

    @Test
    public void simpleQueryTransaction() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        //assertEquals("150", queryTransactionResponse.getResponse());
        assertEquals("Original transaction found",queryTransactionResponse.getMessage());
        assertEquals(1, queryTransactionResponse.getResultsMax10().getTransactionResponses().size());
        assertEquals("sandbox", queryTransactionResponse.getLocation());
    }

    @Test
    public void simpleQueryTransaction_showStatusOnly() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        queryTransaction.setReportGroup("default");
        queryTransaction.setShowStatusOnly(YesNoType.Y);

        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("150", queryTransactionResponse.getResponse());
        assertEquals("Original transaction found",queryTransactionResponse.getMessage());
        assertEquals(1, queryTransactionResponse.getResultsMax10().getTransactionResponses().size());
        assertEquals("sandbox", queryTransactionResponse.getLocation());
    }

    @Test
    public void simpleQueryTransaction_multipleResponses() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId2");
        queryTransaction.setOrigActionType(ActionTypeEnum.D);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("150", queryTransactionResponse.getResponse());
        assertEquals("Original transaction found",queryTransactionResponse.getMessage());
        assertEquals(2, queryTransactionResponse.getResultsMax10().getTransactionResponses().size());
        assertEquals("sandbox", queryTransactionResponse.getLocation());
    }

    @Test
    public void simpleQueryTransaction_transactionNotFound() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId0");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("151", queryTransactionResponse.getResponse());
        assertEquals("Original transaction not found",queryTransactionResponse.getMessage());
        assertNull(queryTransactionResponse.getResultsMax10());
        assertEquals("sandbox", queryTransactionResponse.getLocation());
    }

    @Test
    public void simpleQueryTransaction_transactionNotFoundWrongActionType() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId5");
        queryTransaction.setOrigActionType(ActionTypeEnum.R);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("151", queryTransactionResponse.getResponse());
        assertEquals("Original transaction not found",queryTransactionResponse.getMessage());
        assertNull(queryTransactionResponse.getResultsMax10());
        assertEquals("sandbox", queryTransactionResponse.getLocation());
    }

    @Test
    public void simpleQueryTransaction_queryTransactionUnavailaleResponse() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionUnavailableResponse queryTransactionResponse = (QueryTransactionUnavailableResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("152", queryTransactionResponse.getResponse());
        assertEquals("Original transaction found but response not yet available",queryTransactionResponse.getMessage());
        assertEquals("sandbox", queryTransactionResponse.getLocation());
    }
    @Test
    public void transactionFoundInSecondarySiteAndPrimarySiteUnavailable() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = new CnpOnline(getCnpProperty("https://payments.east.vantivprelive.com/vap/communicator/online1","https://www.testvantivcnp.com/sandbox/communicator/online")).queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        //assertEquals("150", queryTransactionResponse.getResponse());
        assertEquals("Original transaction found",queryTransactionResponse.getMessage());
        assertEquals(1, queryTransactionResponse.getResultsMax10().getTransactionResponses().size());
        assertEquals("sandbox", queryTransactionResponse.getLocation());
    }
    @Test
    public void queryTransaction_notFoundinBothSites() throws Exception {
        queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId5");
        queryTransaction.setOrigActionType(ActionTypeEnum.R);
        queryTransaction.setReportGroup("default");
        CommManager.reset();
        TransactionTypeWithReportGroup response = new CnpOnline(getCnpProperty("https://www.testvantivcnp.com/sandbox/communicator/online","https://www.testvantivcnp.com/sandbox/communicator/online")).queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("Original transaction not found", queryTransactionResponse.getMessage());
        assertEquals("151", queryTransactionResponse.getResponse());
    }

    Properties getCnpProperty(String url1,String url2) {
        Properties cnpProperty = new Properties();
        cnpProperty.setProperty("username", "SDKTEAM12");
        cnpProperty.setProperty("password", "V2b9F4k7");
        cnpProperty.setProperty("merchantId", "1288791");
        cnpProperty.setProperty("reportGroup", "Default Report Group");
         cnpProperty.setProperty("printxml", "true");
        cnpProperty.setProperty("proxyHost", "usproxy.dlb.corp.vantiv.com");
        cnpProperty.setProperty("proxyPort", "8080");
        cnpProperty.setProperty("multiSiteUrl1",url1);
        cnpProperty.setProperty("multiSiteUrl2",url2);
        return cnpProperty;
    }
}