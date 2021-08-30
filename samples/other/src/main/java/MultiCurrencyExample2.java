package io.github.vantiv.sdk.samples;
import io.github.vantiv.sdk.*;
import io.github.vantiv.sdk.generate.*;
 
import java.util.Properties;
 
public class MultiCurrencyExample2 {
    public static void main(String[] args) {
        CnpOnline cnp = new CnpOnline();
 
        Authorization authorization = new Authorization();
        authorization.setReportGroup("Planets");
        authorization.setOrderId("12344");
        authorization.setAmount(106L);
        authorization.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000002");
        card.setExpDate("1210");
        authorization.setCard(card);
        authorization.setId("id");
        Contact obj=new Contact();
        obj.setCountry(CountryTypeEnum.values()[0]);
        CnpOnlineRequest overrides = new CnpOnlineRequest();
        if(obj.getCountry().name().equalsIgnoreCase("USA")) {
            overrides.setMerchantId("1001"); //configured in our system for USD
        } else if(obj.getCountry().name().equalsIgnoreCase("CA")) {
            overrides.setMerchantId("1002"); //configured in our system for CDN
        }
 
        AuthorizationResponse response = cnp.authorize(authorization, overrides);
        //Display Results 
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The MultiCurrencyExample does not give the right response");
    }
}
