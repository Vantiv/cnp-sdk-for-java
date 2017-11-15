package com.cnp.sdk;

import com.cnp.sdk.CnpBatchFileRequest;
import com.cnp.sdk.CnpBatchRequest;
import com.cnp.sdk.generate.CardType;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.cnp.sdk.generate.OrderSourceType;
import com.cnp.sdk.generate.Sale;

public class performanceTestMerchantSDKLite {
	private static CnpBatchFileRequest cnpBatchFileRequest;

    static String merchantId = "07103229";

    public static void main(String[] args) throws Exception {
    	cnpBatchFileRequest = new CnpBatchFileRequest("testFile.xml");
    	CnpBatchRequest cnpBatchRequest = cnpBatchFileRequest.createBatch(merchantId);
    	for (int i =0; i < 50 ; i++)
		{
    		Sale sale = new Sale();
    		sale.setAmount(106L);
    		sale.setOrderId("12344");
    		sale.setOrderSource(OrderSourceType.ECOMMERCE);
    		CardType card = new CardType();
    		card.setType(MethodOfPaymentTypeEnum.VI);
    		card.setNumber("4100000000000002");
    		card.setExpDate("1210");
    		sale.setCard(card);
    		sale.setId("id");

    		cnpBatchRequest.addTransaction(sale);
		}

		cnpBatchFileRequest.sendToCnpSFTP();
 		System.out.println("done");
 	}

}

