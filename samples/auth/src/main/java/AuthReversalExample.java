package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
 
public class AuthReversalExample {
    public static void main(String[] args) {
        AuthReversal authReversal = new AuthReversal();
        authReversal.setId("Id"); 
        //cnpTxnId contains the Cnp Transaction Id returned on the auth
        authReversal.setCnpTxnId(100000000000000000L);
        AuthReversalResponse response = new CnpOnline().authReversal(authReversal);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());

	// In your sample, you can ignore this
        if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The AuthReversalExample does not give the right response");
       
    }
}


