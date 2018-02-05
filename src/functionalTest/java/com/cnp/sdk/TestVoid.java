package com.cnp.sdk;

import com.cnp.sdk.generate.*;
import com.cnp.sdk.generate.Void;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestVoid {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleVoid() throws Exception {
        Void voidTxn = new Void();
        voidTxn.setCnpTxnId(102948757348543643L);
        voidTxn.setReportGroup("Planets");
        voidTxn.setId("new_ID");
        voidTxn.setCustomerId("my_customer");

        VoidResponse response = cnp.dovoid(voidTxn);
        assertEquals("643",response.getResponse());
    }
}
