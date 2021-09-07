package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//import com.cnp.sdk.generate.*;
import io.github.vantiv.sdk.generate.TranslateToLowValueTokenRequestType;
import io.github.vantiv.sdk.generate.TranslateToLowValueTokenResponse;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTranslateToLowValueTokenRequest {
    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void TestSimpleTranslateToLowValueTokenRequest() throws Exception{
        TranslateToLowValueTokenRequestType translateToLowValueTokenRequest = new TranslateToLowValueTokenRequestType();
        translateToLowValueTokenRequest.setOrderId("123456789");
        translateToLowValueTokenRequest.setToken("qwe7895sdffd78598dsed8");
        translateToLowValueTokenRequest.setId("Xlate1");
        translateToLowValueTokenRequest.setReportGroup("iQ Report Group");
        translateToLowValueTokenRequest.setCustomerId("Customer Id");

        TranslateToLowValueTokenResponse translateToLowValueTokenResponse = cnp.TranslateToLowValueTokenRequest(translateToLowValueTokenRequest);
        assertEquals("803", translateToLowValueTokenResponse.getResponse());
        assertEquals("Valid Token", translateToLowValueTokenResponse.getMessage());
        assertEquals("sandbox", translateToLowValueTokenResponse.getLocation());
    }
}
