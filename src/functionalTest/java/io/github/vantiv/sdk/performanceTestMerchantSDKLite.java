package io.github.vantiv.sdk;

import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.Sale;

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

