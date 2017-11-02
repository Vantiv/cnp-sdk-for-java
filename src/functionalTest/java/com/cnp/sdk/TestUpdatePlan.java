package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.UpdatePlan;
import com.cnp.sdk.generate.UpdatePlanResponse;

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

    }


}
