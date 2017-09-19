package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
 
//Full Lifecycle
public class FullLifeCycleExample {
    public static void main(String[] args) {
        CnpOnline cnp = new CnpOnline();
 
        Authorization auth = new Authorization();
        auth.setOrderId("1");
        auth.setAmount(10010L);
        auth.setOrderSource(OrderSourceType.ECOMMERCE);
        Contact billToAddress = new Contact();
        billToAddress.setName("John Smith");
        billToAddress.setAddressLine1("1 Main St.");
        billToAddress.setCity("Burlington");
        billToAddress.setState("MA");
        billToAddress.setCountry(CountryTypeEnum.US);
        billToAddress.setZip("01803-3747");
        auth.setBillToAddress(billToAddress);
        CardType card = new CardType();
        card.setNumber("375001010000003");
        card.setExpDate("0112");
        card.setCardValidationNum("349");
        card.setType(MethodOfPaymentTypeEnum.AX);
        auth.setCard(card);
        auth.setId("id");
 
        AuthorizationResponse authResponse = cnp.authorize(auth);
        System.out.println("Response: " + authResponse.getResponse());
        System.out.println("Message: " + authResponse.getMessage());
        System.out.println("Cnp Transaction ID: " + authResponse.getCnpTxnId());
 
        Capture capture = new Capture();
        capture.setCnpTxnId(authResponse.getCnpTxnId());
        capture.setId("id");
        CaptureResponse captureResponse = cnp.capture(capture);  //Capture the Auth
        System.out.println("Response: " + captureResponse.getResponse());
        System.out.println("Message: " + captureResponse.getMessage());
        System.out.println("Cnp Transaction ID: " + captureResponse.getCnpTxnId());
 
        Credit credit = new Credit();
        credit.setCnpTxnId(captureResponse.getCnpTxnId());  
        credit.setId("id");
        CreditResponse creditResponse = cnp.credit(credit); //Refund the capture
        System.out.println("Response: " + creditResponse.getResponse());
        System.out.println("Message: " + creditResponse.getMessage());
        System.out.println("Cnp Transaction ID: " + creditResponse.getCnpTxnId());
	// In your sample, you can ignore this 	
	if(!creditResponse.getMessage().equals("Approved")||!captureResponse.getMessage().equals("Approved")||!authResponse.getMessage().equals("Approved"))
        throw new RuntimeException(" The AuthWithTokenExample does not give the right response");
  
        //TODO - Fix the void here
       /* Void credit = new Credit();
        capture.setCnpTxnId(captureResponse.getCnpTxnId());
        CreditResponse creditResponse = cnp.credit(credit); //Refund the capture
        System.out.println("Response: " + creditResponse.getResponse());
        System.out.println("Message: " + creditResponse.getMessage());
        System.out.println("Cnp Transaction ID: " + creditResponse.getCnpTxnId());*/
 
    }
}
