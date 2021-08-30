package io.github.vantiv.sdk.samples;
import io.github.vantiv.sdk.*;
import io.github.vantiv.sdk.generate.*;
 
public class CreditExample {
    public static void main(String[] args) {
        Credit credit = new Credit();
        //cnpTxnId contains the Cnp Transaction Id returned on the deposit
        credit.setCnpTxnId(100000000000000011L);
        credit.setId("id");
        CreditResponse response = new CnpOnline().credit(credit);
        //Display Results 
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The CreditExample does not give the right response");
    }
}
