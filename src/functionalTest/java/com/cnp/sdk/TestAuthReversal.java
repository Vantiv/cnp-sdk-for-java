package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.AuthReversal;
import com.cnp.sdk.generate.AuthReversalResponse;

public class TestAuthReversal {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}
	
	@Test
	public void simpleAuthReversal() throws Exception{
		AuthReversal reversal = new AuthReversal();
		reversal.setCnpTxnId(12345678000L);
		reversal.setAmount(106L);
		reversal.setPayPalNotes("Notes");
		reversal.setId("id");
		
		AuthReversalResponse response = cnp.authReversal(reversal);
		assertEquals("Approved", response.getMessage());
	}

}
