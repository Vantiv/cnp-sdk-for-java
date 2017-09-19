package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
import java.util.Calendar;
 
public class CaptureGivenAuthExample {
    public static void main(String[] args) {
        CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
	capturegivenauth.setAmount(106L);
	capturegivenauth.setOrderId("12344");
	AuthInformation authInfo = new AuthInformation();
	Calendar authDate = Calendar.getInstance();
	authDate.set(2002, Calendar.OCTOBER, 9);
	authInfo.setAuthDate(authDate);
	authInfo.setAuthCode("543216");
	authInfo.setAuthAmount(12345L);
	capturegivenauth.setAuthInformation(authInfo);
	capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
	CardType card = new CardType();
	card.setType(MethodOfPaymentTypeEnum.VI);
	card.setNumber("4100000000000000");
	card.setExpDate("1210");
	capturegivenauth.setCard(card);
	capturegivenauth.setId("id");
        CaptureGivenAuthResponse response = new CnpOnline().captureGivenAuth(capturegivenauth);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The CaptureGivenAuthExample does not give the right response");
    }
}
