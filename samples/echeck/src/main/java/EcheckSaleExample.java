package com.cnp.sdk.samples;
import com.cnp.sdk.*;
import com.cnp.sdk.generate.*;
 
public class EcheckSaleExample {
    public static void main(String[] args) {
	EcheckSale echecksale = new EcheckSale();
	echecksale.setAmount(123456L);
	echecksale.setOrderId("12345");
	echecksale.setOrderSource(OrderSourceType.ECOMMERCE);
	EcheckType echeck = new EcheckType();
	echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
	echeck.setAccNum("12345657890");
	echeck.setRoutingNum("123456789");
	echeck.setCheckNum("123455");
	echecksale.setEcheck(echeck);
	Contact contact = new Contact();
	contact.setName("Bob");
	contact.setCity("lowell");
	contact.setState("MA");
	contact.setEmail("sdksupport@cnp.com");
	echecksale.setBillToAddress(contact);
	echecksale.setId("id");
  
        EcheckSalesResponse response = new CnpOnline().echeckSale(echecksale);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Cnp Transaction ID: " + response.getCnpTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The EcheckSaleExample does not give the right response");
    }
}
