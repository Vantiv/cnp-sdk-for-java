package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
 
public class EcheckCreditExample {
    public static void main(String[] args) {
	EcheckCredit echeckcredit = new EcheckCredit();
	echeckcredit.setId("id");
        //CnpTxnId from an earlier echeck sale
	echeckcredit.setCnpTxnId(123456789101112L);
  
        EcheckCreditResponse response = new CnpOnline().echeckCredit(echeckcredit);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The EcheckCreditExample does not give the right response");
    }
}
