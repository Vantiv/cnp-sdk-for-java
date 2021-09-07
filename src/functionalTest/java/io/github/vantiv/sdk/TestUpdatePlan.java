package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import io.github.vantiv.sdk.generate.UpdatePlan;
import io.github.vantiv.sdk.generate.UpdatePlanResponse;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestUpdatePlan {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleupdaatePlan() throws Exception {
       UpdatePlan updatePlan=new UpdatePlan();
       updatePlan.setActive(true);
       updatePlan.setPlanCode("Monthly");

       UpdatePlanResponse response=cnp.updatePlan(updatePlan);
         assertEquals("Approved", response.getMessage());
        assertNull(response.getLocation());

    }

}
