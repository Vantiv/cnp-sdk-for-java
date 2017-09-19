package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
 
public class CaptureExample {
    public static void main(String[] args) {
        Capture capture = new Capture();
        //cnpTxnId contains the Cnp Transaction Id returned on the authorization
        capture.setCnpTxnId(100000000000000011L);
        capture.setId("id");
        CaptureResponse response = new CnpOnline().capture(capture);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The CaptureExample does not give the right response");
    }
}



