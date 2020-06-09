package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.EcheckVoid;
import com.cnp.sdk.generate.EcheckVoidResponse;

public class TestEcheckVoid {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}

	@Test
	public void simpleEcheckVoid() throws Exception{
		EcheckVoid echeckvoid = new EcheckVoid();
		echeckvoid.setCnpTxnId(123456789101112L);
		echeckvoid.setId("id");
		EcheckVoidResponse response = cnp.echeckVoid(echeckvoid);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
}

