package io.github.vantiv.sdk.samples;
import io.github.vantiv.sdk.*;
import io.github.vantiv.sdk.generate.*;
 
public class VoidExample {
    public static void main(String[] args) {
        io.github.vantiv.sdk.generate.Void theVoid = new io.github.vantiv.sdk.generate.Void();
        theVoid.setId("id");
        //cnpTxnId contains the Cnp Transaction Id returned on the deposit
        theVoid.setCnpTxnId(100000000000000000L);
        VoidResponse response = new CnpOnline().dovoid(theVoid);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The VoidExample does not give the right response");
    }
} 

