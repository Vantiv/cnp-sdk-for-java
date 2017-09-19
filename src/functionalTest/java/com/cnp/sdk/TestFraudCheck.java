package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.AdvancedFraudChecksType;
import com.cnp.sdk.generate.AdvancedFraudResultsType;
import com.cnp.sdk.generate.FraudCheck;
import com.cnp.sdk.generate.FraudCheckResponse;

public class TestFraudCheck {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }
    
    @Test
    public void testFraudCheck() throws Exception {
        FraudCheck fraudCheck = new FraudCheck();
        fraudCheck.setId("1");
        AdvancedFraudChecksType advancedFraudChecks = new AdvancedFraudChecksType();
        advancedFraudChecks.setThreatMetrixSessionId("123");
        advancedFraudChecks.setCustomAttribute1("pass");
        advancedFraudChecks.setCustomAttribute2("42");
        advancedFraudChecks.setCustomAttribute3("5");
        fraudCheck.setAdvancedFraudChecks(advancedFraudChecks);
        FraudCheckResponse fraudCheckResponse = cnp.fraudCheck(fraudCheck);
        
        //System.out.println(fraudCheckResponse.getMessage());
        
        AdvancedFraudResultsType advancedFraudResultsType = fraudCheckResponse.getAdvancedFraudResults();
        assertEquals("pass", advancedFraudResultsType.getDeviceReviewStatus());
        assertEquals(new Integer(42), advancedFraudResultsType.getDeviceReputationScore());
        assertEquals(5, advancedFraudResultsType.getTriggeredRules().size());
    }
}
