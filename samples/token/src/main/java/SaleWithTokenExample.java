package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
 
public class SaleWithTokenExample {
    public static void main(String[] args) {
        Sale sale = new Sale();
        sale.setOrderId("1");
        sale.setAmount(10000L);
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        CardTokenType token = new CardTokenType();
        token.setCardValidationNum("349");
        token.setExpDate("1214");
        token.setCnpToken("1111222233334000");
        token.setType(MethodOfPaymentTypeEnum.VI);
        sale.setToken(token);
        sale.setId("id");
 
        SaleResponse response = new CnpOnline().sale(sale);
        //Display Results 
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
        if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The SaleWithTokenExample does not give the right response");
    }
}
