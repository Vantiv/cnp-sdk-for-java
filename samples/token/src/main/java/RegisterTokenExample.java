package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
 
public class RegisterTokenExample {
    public static void main(String[] args) {
        RegisterTokenRequestType registerToken = new RegisterTokenRequestType();
	registerToken.setOrderId("12344");
	registerToken.setAccountNumber("1233456789103801");
	registerToken.setId("id");
	RegisterTokenResponse response = new CnpOnline().registerToken(registerToken);
 
        //Display Results 
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Token: " + response.getCnpToken());
        if(!response.getMessage().equals("Account number was successfully registered"))
        throw new RuntimeException(" The RegisterTokenExample does not give the right response");
    }
}
