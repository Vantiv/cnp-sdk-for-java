package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.github.vantiv.sdk.generate.BatchResponse;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.Sale;

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
		batchResponse.setNumAccountUpdates(BigInteger.valueOf(4));
		CnpBatchResponse cnpBatchResponse = new CnpBatchResponse(batchResponse);
		Assert.assertEquals("101", cnpBatchResponse.getBatchResponse().getId());
		Assert.assertEquals(562L, cnpBatchResponse.getBatchResponse().getCnpBatchId());
		Assert.assertEquals("101", cnpBatchResponse.getBatchResponse().getMerchantId());
		Assert.assertEquals(562L, cnpBatchResponse.getBatchResponse().getCnpBatchId());
		Assert.assertEquals(BigInteger.valueOf(4), cnpBatchResponse.getBatchResponse().getNumAccountUpdates());
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
