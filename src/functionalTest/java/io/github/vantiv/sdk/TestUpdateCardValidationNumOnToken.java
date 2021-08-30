package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import io.github.vantiv.sdk.generate.UpdateCardValidationNumOnToken;
import io.github.vantiv.sdk.generate.UpdateCardValidationNumOnTokenResponse;
import org.junit.BeforeClass;
import org.junit.Test;

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
		assertEquals("sandbox", response.getLocation());
	}
}
