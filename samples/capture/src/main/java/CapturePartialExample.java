package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
 
public class CapturePartialExample {
    public static void main(String[] args) {
        Capture capture = new Capture();
        //cnpTxnId contains the Cnp Transaction Id returned on the authorization 
        capture.setCnpTxnId(100000000000000011L);
        capture.setAmount(1200L); //Capture $12 dollars of a previous authorization
        capture.setId("id");
        CaptureResponse response = new CnpOnline().capture(capture);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The CapturePartialExample does not give the right response");
    }
}

