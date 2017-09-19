package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
 
public class ForceCaptureExample {
    public static void main(String[] args) {
        ForceCapture forceCapture = new ForceCapture();
        forceCapture.setAmount(106L);
        forceCapture.setOrderId("12344");
        forceCapture.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000000");
        card.setExpDate("1210");
        forceCapture.setCard(card);
        forceCapture.setId("id");
 
        ForceCaptureResponse response = new CnpOnline().forceCapture(forceCapture);
        //Display Results 
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The ForceCaptureExample does not give the right response");
    }
}
