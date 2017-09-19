package com.cnp.sdk;


import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.CreatePlan;
import com.cnp.sdk.generate.CreatePlanResponse;
import com.cnp.sdk.generate.IntervalTypeEnum;

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

    }


}
