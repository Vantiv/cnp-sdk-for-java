package io.github.vantiv.sdk.samples;
import io.github.vantiv.sdk.*;
import io.github.vantiv.sdk.generate.*;
 
import java.util.Properties;
 
public class MultiCurrencyExample {
    public static void main(String[] args) {
        CnpOnline usdCurrency = new CnpOnline(); //This will use the default merchant setup in .cnp_SDK_config.properties supporting purchases in USD
 
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
 
        AuthorizationResponse response = usdCurrency.authorize(authorization);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
 
        Properties cdnProps = new Properties();
        cdnProps.setProperty("merchantId","1002");
        cdnProps.setProperty("url","https://www.testvantivcnp.com/sandbox/new/sandbox/communicator/online");
        cdnProps.setProperty("username","username");
        cdnProps.setProperty("password","topsecret"); 
        cdnProps.setProperty("proxyHost","usproxy.dlb.corp.vantiv.com");
        cdnProps.setProperty("proxyPort","8080");      
        cdnProps.setProperty("timeout","5000");
        CnpOnline cdnCurrency = new CnpOnline(cdnProps); //Override the default merchant setup in .cnp_SDK_config.properties to force purchase in CDN
 
        AuthorizationResponse response2 = cdnCurrency.authorize(authorization);  //Perform the same authorization using CDN instead of USD
        //Display Results 
        System.out.println("Response: " + response2.getResponse());
        System.out.println("Message: " + response2.getMessage());
        System.out.println("Cnp Transaction ID: " + response2.getCnpTxnId());
 
        Properties yenProps = new Properties();
        yenProps.setProperty("merchantId","1003"); //Notice that 1003 is a different merchant.  In our system, they could be setup for YEN purchases
        yenProps.setProperty("url","https://www.testvantivcnp.com/sandbox/new/sandbox/communicator/online");
        yenProps.setProperty("username","username");
        yenProps.setProperty("password","topsecret");    
        yenProps.setProperty("proxyHost","usproxy.dlb.corp.vantiv.com");
        yenProps.setProperty("proxyPort","8080");     
        yenProps.setProperty("timeout","5000");
        CnpOnline yenCurrency = new CnpOnline(yenProps); //Override the default merchant setup in .cnp_SDK_config.properties to force purchase in YEN
        
        AuthorizationResponse response3 = yenCurrency.authorize(authorization);  //Perform the same authorization using YEN instead of USD
        //Display Results
        System.out.println("Response: " + response3.getResponse());
        System.out.println("Message: " + response3.getMessage());
        System.out.println("Cnp Transaction ID: " + response3.getCnpTxnId());
	if(!response.getMessage().equals("Approved")||!response2.getMessage().equals("Approved")||!response3.getMessage().equals("Approved"))
        throw new RuntimeException(" The MultiCurrencyExample does not give the right response");
 
    }
}

