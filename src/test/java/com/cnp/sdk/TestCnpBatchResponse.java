package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.cnp.sdk.CnpBatchResponse;
import com.cnp.sdk.generate.BatchResponse;
import com.cnp.sdk.generate.CardType;
import com.cnp.sdk.generate.MethodOfPaymentTypeEnum;
import com.cnp.sdk.generate.OrderSourceType;
import com.cnp.sdk.generate.Sale;

public class TestCnpBatchResponse {

	File file;

	@Before
	public void before() throws Exception {
	}

	@Test
	public void testSetBatchResponse() throws Exception {
		BatchResponse batchResponse = new BatchResponse();
		batchResponse.setId("101");
		batchResponse.setCnpBatchId(562L);
		batchResponse.setMerchantId("101");
		CnpBatchResponse cnpBatchResponse = new CnpBatchResponse(batchResponse);
		assertEquals("101", cnpBatchResponse.getBatchResponse().getId());
		assertEquals(562L, cnpBatchResponse.getBatchResponse().getCnpBatchId());
		assertEquals("101", cnpBatchResponse.getBatchResponse().getMerchantId());
	}

	public Sale createTestSale(Long amount, String orderId){
		Sale sale = new Sale();
		sale.setAmount(amount);
		sale.setOrderId(orderId);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setReportGroup("test");
		return sale;
	}
}
