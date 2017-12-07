package com.cnp.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cnp.sdk.CnpOnline;
import com.cnp.sdk.generate.EcheckAccountTypeEnum;
import com.cnp.sdk.generate.EcheckRedeposit;
import com.cnp.sdk.generate.EcheckRedepositResponse;
import com.cnp.sdk.generate.EcheckTokenType;
import com.cnp.sdk.generate.EcheckType;

public class TestEcheckRedeposit {

	private static CnpOnline cnp;

	@BeforeClass
	public static void beforeClass() throws Exception {
		cnp = new CnpOnline();
	}
	
	@Test
	public void simpleEcheckRedeposit() throws Exception{
		EcheckRedeposit echeckredeposit = new EcheckRedeposit();
		echeckredeposit.setCnpTxnId(123456L);
		echeckredeposit.setId("id");
		EcheckRedepositResponse response = cnp.echeckRedeposit(echeckredeposit);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void echeckRedepositWithEcheck() throws Exception{
		EcheckRedeposit echeckredeposit = new EcheckRedeposit();
		echeckredeposit.setCnpTxnId(123456L);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
	    echeckredeposit.setId("id");
		
		echeckredeposit.setEcheck(echeck);
		EcheckRedepositResponse response = cnp.echeckRedeposit(echeckredeposit);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void echeckRedepositWithEcheckToken() throws Exception{
		EcheckRedeposit echeckredeposit = new EcheckRedeposit();
		echeckredeposit.setCnpTxnId(123456L);
		EcheckTokenType echeckToken = new EcheckTokenType();
		echeckToken.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeckToken.setCnpToken("1234565789012");
		echeckToken.setRoutingNum("123456789");
		echeckToken.setCheckNum("123455");
	    echeckredeposit.setId("id");
		
		echeckredeposit.setEcheckToken(echeckToken);
		EcheckRedepositResponse response = cnp.echeckRedeposit(echeckredeposit);
		assertEquals("Approved", response.getMessage());
	}

}
