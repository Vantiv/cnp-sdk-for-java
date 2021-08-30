package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import io.github.vantiv.sdk.generate.RFRResponse;
import org.junit.Before;
import org.junit.Test;

public class TestCnpRFRResponse {

	@Before
	public void before() throws Exception {
	}

	@Test
	public void testSetRFRResponse() throws Exception {
		RFRResponse rfrResponse = new RFRResponse();
		rfrResponse.setMessage("Hurrdurrburrrrrr");
		rfrResponse.setResponse("Nuh uh. :(");
		CnpRFRResponse cnpRFRResponse = new CnpRFRResponse(rfrResponse);
		assertEquals("Nuh uh. :(", cnpRFRResponse.getRFRResponseCode());
		assertEquals("Hurrdurrburrrrrr", cnpRFRResponse.getRFRResponseMessage());
	}

}
