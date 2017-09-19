package com.cnp.sdk;

import java.util.Calendar;

import com.cnp.sdk.CnpBatchFileRequest;
import com.cnp.sdk.CnpBatchRequest;
import com.cnp.sdk.generate.CardType;
import com.cnp.sdk.generate.Contact;
import com.cnp.sdk.generate.CountryTypeEnum;
import com.cnp.sdk.generate.CurrencyCodeEnum;
import com.cnp.sdk.generate.CustomerInfo;
import com.cnp.sdk.generate.CustomerInfo.CustomerType;
import com.cnp.sdk.generate.CustomerInfo.ResidenceStatus;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.cnp.sdk.generate.OrderSourceType;
import com.cnp.sdk.generate.Sale;

public class performanceTestMerchantSDKWhole {
	private static CnpBatchFileRequest cnpBatchFileRequest;

    static String merchantId = "07103229";

    public static void main(String[] args) throws Exception {
    	cnpBatchFileRequest = new CnpBatchFileRequest("testFile.xml");
    	CnpBatchRequest cnpBatchRequest = cnpBatchFileRequest.createBatch(merchantId);
    	String cnpTxnId = "82818409740294007";
    	String surcharge = "100000000";
    	String customerIncome = "100000";
    	Calendar date = Calendar.getInstance();
    	date.set(Calendar.YEAR, 1999);
    	date.set(Calendar.MONTH, 7);
    	date.set(Calendar.DAY_OF_MONTH, 26);
    	Contact ct = new Contact();

    	for (int i =0; i < 50 ; i++)
		{
    		Sale sale = new Sale();
    		sale.setReportGroup("1111111111111111111111111");
    		sale.setId("1234567890123456789012345");
    		sale.setCnpTxnId(Long.valueOf(cnpTxnId));
    		sale.setAmount(106L);
    		sale.setSurchargeAmount(Long.valueOf(surcharge));
    		sale.setOrderId("1234567890123456789012345");
    		sale.setOrderSource(OrderSourceType.ECOMMERCE);

    		CustomerInfo ci = new CustomerInfo();
    		ci.setSsn("033894657");
    		ci.setDob(date);
    		ci.setCustomerCheckingAccount(false);
    	    ci.setCustomerRegistrationDate(date);
            ci.setCustomerType(CustomerType.EXISTING);
    	    ci.setCustomerSavingAccount(true);
    	    ci.setCustomerWorkTelephone("789-999-0000");
    	    ci.setEmployerName("Johnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
    	    ci.setIncomeAmount(Long.valueOf(customerIncome));
    	    ci.setIncomeCurrency(CurrencyCodeEnum.CAD);
    	    ci.setResidenceStatus(ResidenceStatus.OWN);
    	    ci.setYearsAtEmployer(10);
    	    ci.setYearsAtResidence(10);

    	    sale.setCustomerInfo(ci);
    	    sale.setAllowPartialAuth(true);



    	    ct.setAddressLine1("aaaaaAAAAABBBBBGGGGGGGGGGKKKKKKKKKK");
    	    ct.setAddressLine2("aaaaaAAAAABBBBBGGGGGGGGGGKKKKKKKKKK");
    	    ct.setAddressLine3("aaaaaAAAAABBBBBGGGGGGGGGGKKKKKKKKKK");
    	    ct.setCity("aaaaaAAAAABBBBBGGGGGGGGGGKKKKKKKKKKXXXXX");
    	    ct.setCompanyName("AAAAABBBBBNNNNNMMMMMLLLLLLLLLLUUUUUTTTTT");
    	    ct.setCountry(CountryTypeEnum.AD);
    	    ct.setEmail("ssssssssssssssssssssssssssssssssssssssssssssssstest@emailcsdfafdasdf.com");
    	   /* ct.setFirstName(value);
    	    ct.setlas


    	    sale.setBillToAddress(value)

    	    */
    		CardType card = new CardType();
    		card.setType(MethodOfPaymentTypeEnum.VI);
    		card.setNumber("4100000000000002");
    		card.setExpDate("1210");
    		sale.setCard(card);
    		sale.setId(Integer.toString(i));
    		
    		cnpBatchRequest.addTransaction(sale);
		}

		cnpBatchFileRequest.sendToCnp();
 		System.out.println("done");
 	}

}

