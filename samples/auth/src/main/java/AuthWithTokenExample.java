package io.github.vantiv.sdk.samples;
import io.github.vantiv.sdk.*;
import io.github.vantiv.sdk.generate.*;
 
public class AuthWithTokenExample {
    public static void main(String[] args) {
        Authorization auth = new Authorization();
        auth.setOrderId("1");
        auth.setAmount(10000L);
        auth.setOrderSource(OrderSourceType.ECOMMERCE);
        CardTokenType token = new CardTokenType();
        token.setCardValidationNum("349");
        token.setExpDate("1214");
        token.setCnpToken("1111222233334000");
        token.setType(MethodOfPaymentTypeEnum.VI);
        auth.setToken(token);
        auth.setId("id"); 
 
        AuthorizationResponse response = new CnpOnline().authorize(auth);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
	// In your sample, you can ignore this 	
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The AuthWithTokenExample does not give the right response");
    }
}
