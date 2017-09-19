package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
 
public class PaypageRegistrationIdToTokenExample {
    public static void main(String[] args) {
	RegisterTokenRequestType tokenRequest = new RegisterTokenRequestType();
	tokenRequest.setOrderId("12345");
	tokenRequest.setId("id");
        //The paypageRegistrationId is received in the form posted from your checkout page with paypage enabled
	tokenRequest.setPaypageRegistrationId("123456789012345678901324567890abcdefghi");
	RegisterTokenResponse tokenResponse = new CnpOnline().registerToken(tokenRequest);
        //Display Results 
        System.out.println("Token: " + tokenResponse.getCnpToken());
	System.out.println("Response: " + tokenResponse.getMessage());
        if(!tokenResponse.getMessage().equals("Account number was successfully registered"))
        throw new RuntimeException(" The PaypageRegistrationIdToTokenExample does not give the right response");
    }
}
