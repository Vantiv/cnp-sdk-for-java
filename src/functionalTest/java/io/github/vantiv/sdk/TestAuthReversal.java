package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.AuthReversal;
import io.github.vantiv.sdk.generate.AuthReversalResponse;
import io.github.vantiv.sdk.generate.AdditionalCOFData;
import io.github.vantiv.sdk.generate.FrequencyOfMITEnum;
import io.github.vantiv.sdk.generate.IdentityBundle;
import io.github.vantiv.sdk.generate.PaymentTypeEnum;

import java.math.BigInteger;

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
		assertEquals("sandbox", response.getLocation());
	}
	@Test
	public void simpleAuthReversalWithAdditionalCOFData() throws Exception{
		AuthReversal reversal = new AuthReversal();
		reversal.setCnpTxnId(12345678000L);
		reversal.setAmount(106L);
		reversal.setPayPalNotes("Notes");
		reversal.setId("id");
		AdditionalCOFData data = new AdditionalCOFData();
		data.setUniqueId("56655678D");
		data.setTotalPaymentCount("35");
		data.setFrequencyOfMIT(FrequencyOfMITEnum.ANNUALLY);
		data.setPaymentType(PaymentTypeEnum.FIXED_AMOUNT);
		data.setValidationReference("asd123");
		data.setSequenceIndicator(BigInteger.valueOf(12));
		reversal.setAdditionalCOFData(data);
		AuthReversalResponse response = cnp.authReversal(reversal);
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}

	//v12.41 changes to test identityBundle
	@Test
	public void authReversalWithIdentityBundle() throws Exception {
		AuthReversal reversal = new AuthReversal();
		reversal.setCnpTxnId(12345678000L);
		reversal.setAmount(106L);
		reversal.setPayPalNotes("Notes");
		reversal.setId("id");
		IdentityBundle identityBundle = new IdentityBundle();
		identityBundle.setMerchantId("12222");
		identityBundle.setEntityId("222222");
		identityBundle.setEntityReference("32222");
		identityBundle.setResourceId("422222");
		identityBundle.setResourceReference("52222");
		identityBundle.setCommandId("6222");
		identityBundle.setCommandReference("72222");
		identityBundle.setOrderReference("82222");
		reversal.setIdentityBundle(identityBundle);
		AuthReversalResponse response = cnp.authReversal(reversal);
		assertEquals(response.getMessage(), "000", response.getResponse());
		assertEquals("Approved", response.getMessage());
		assertEquals("sandbox", response.getLocation());
	}
}