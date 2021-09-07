package io.github.vantiv.sdk;

//import com.cnp.sdk.generate.*;
import io.github.vantiv.sdk.generate.Void;
import io.github.vantiv.sdk.generate.VoidResponse;
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
        voidTxn.setCnpTxnId(102948757348543822L);
        voidTxn.setReportGroup("Planets");
        voidTxn.setId("new_ID");
        voidTxn.setCustomerId("my_customer");

        VoidResponse response = cnp.dovoid(voidTxn);
        assertEquals("822",response.getResponse());
        assertEquals("sandbox", response.getLocation());
    }
}
