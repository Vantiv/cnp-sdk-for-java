package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.generate.AccountUpdate;
import com.cnp.sdk.generate.AccountUpdateResponse;
import com.cnp.sdk.generate.CardType;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.cnp.sdk.generate.ObjectFactory;
import com.cnp.sdk.generate.UpdateCardValidationNumOnToken;
import com.cnp.sdk.generate.UpdateCardValidationNumOnTokenResponse;

public class TestUpdatedToken {
    private final long TIME_STAMP = System.currentTimeMillis();

    @Test
    public void testUpdatedToken() throws Exception {
        String requestFileName = "cnpSdk-testBatchFile_AU-" + TIME_STAMP + ".xml";
        CnpBatchFileRequest request = new CnpBatchFileRequest(
                requestFileName);

        Properties configFromFile = request.getConfig();

        // pre-assert the config file has required param values
        assertEquals("nufloprftp01.litle.com",
                configFromFile.getProperty("batchHost"));
        // assertEquals("15000", configFromFile.getProperty("batchPort"));

        CnpBatchRequest batch = request.createBatch(configFromFile.getProperty("merchantId"));

        // card
        CardType card = new CardType();
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        card.setType(MethodOfPaymentTypeEnum.VI);

        ObjectFactory objectFactory = new ObjectFactory();
        AccountUpdate accountUpdate = new AccountUpdate();
        accountUpdate.setReportGroup("Planets");
        accountUpdate.setId("12345");
        accountUpdate.setCustomerId("0987");
        accountUpdate.setOrderId("1234");
        accountUpdate.setCardOrToken(objectFactory.createCard(card));

        batch.addTransaction(accountUpdate);

        CnpBatchFileResponse fileResponse = request.sendToCnpSFTP();
        CnpBatchResponse batchResponse = fileResponse
                .getNextCnpBatchResponse();
        int txns = 0;
    }
}
