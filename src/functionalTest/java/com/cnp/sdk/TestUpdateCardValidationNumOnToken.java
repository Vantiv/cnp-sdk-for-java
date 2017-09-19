package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.UpdateCardValidationNumOnToken;
import com.cnp.sdk.generate.UpdateCardValidationNumOnTokenResponse;

public class TestUpdateCardValidationNumOnToken {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}
	
	@Test
	public void simple() throws Exception {
		UpdateCardValidationNumOnToken update = new UpdateCardValidationNumOnToken();
		update.setCnpToken("1111222233334444");
		update.setCardValidationNum("123");
		update.setId("id");
		
		UpdateCardValidationNumOnTokenResponse response = cnp.updateCardValidationNumOnToken(update);
		assertEquals(response.getMessage(), "805",response.getResponse());
	}
}
