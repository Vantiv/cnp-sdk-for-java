package io.github.vantiv.sdk;

import io.github.vantiv.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFinicityUrlRequest {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleFinicityUrlRequest() throws Exception {
        FinicityUrlRequest request = new FinicityUrlRequest();
        request.setId("url1");
        request.setReportGroup("XML10Mer1");
        request.setCustomerId("154646587");
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setPhoneNumber("1-801-984-4200");
        request.setEmail("myname@mycompany.com");
        FinicityUrlResponse response = cnp.finicityUrl(request);
        assertEquals(response.getMessage(), "000",response.getResponse());
        assertEquals("Approved", response.getMessage());
        assertEquals("sandbox", response.getLocation());
    }
}
