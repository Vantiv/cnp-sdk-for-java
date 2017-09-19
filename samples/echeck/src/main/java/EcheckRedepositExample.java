package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
 
public class EcheckRedepositExample {
    public static void main(String[] args) {
	EcheckRedeposit echeckRedeposit = new EcheckRedeposit();
	echeckRedeposit.setId("id");
        //CnpTxnId from an earlier echeck sale
	echeckRedeposit.setCnpTxnId(123456789101112L);
  
        EcheckRedepositResponse response = new CnpOnline().echeckRedeposit(echeckRedeposit);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The EcheckRedepositExample does not give the right response");
    }
}
