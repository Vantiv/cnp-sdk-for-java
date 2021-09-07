package io.github.vantiv.sdk;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import io.github.vantiv.sdk.generate.CreatePlan;
import io.github.vantiv.sdk.generate.CreatePlanResponse;
import io.github.vantiv.sdk.generate.IntervalTypeEnum;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestCreatePlan {

    private static CnpOnline cnp;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
    }

    @Test
    public void simpleCreatePlan() throws Exception {
       CreatePlan createPlan=new CreatePlan();
       createPlan.setPlanCode("Monthly");
       createPlan.setName("abc");
       createPlan.setIntervalType(IntervalTypeEnum.MONTHLY);
       createPlan.setAmount(1995l);
       createPlan.setNumberOfPayments(5);
       createPlan.setTrialNumberOfIntervals(2);
       CreatePlanResponse response=cnp.createPlan(createPlan);
      
         assertEquals("Approved", response.getMessage());
        assertNull(response.getLocation());
    }

}
