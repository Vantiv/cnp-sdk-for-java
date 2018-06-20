package com.cnp.sdk;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.cnp.sdk.generate.RFRResponse;

public class TestCommunication {

	private Communication communication;

	@Before
	public void setup() throws Exception {
		communication = new Communication();
	}

	@Test
	public void testNeuterXml() {
		String xml = null;
		assertNull(communication.neuterXml(xml));

		xml = "";
		assertEquals("", communication.neuterXml(xml));

		xml = "<?xml version=1.0 encoding=UTF-8 standalone=yes?>" +
				"<cnpOnlineRequest merchantId=123456 merchantSdk=Java;11.3.0 version=11.3 xmlns=http://www.litle.com/schema>" +
				"<authentication>" +
				"<user>DummyUser</user>" +
				"<password>DummyPass</password>" +
				"</authentication>" +
				"<authorization reportGroup=Planets id=id>" +
				"<orderId>12344</orderId>" +
				"<amount>106</amount>" +
				"<orderSource>ecommerce</orderSource>" +
				"<card>" +
				"<type>VI</type>" +
				"<number>4100000000000000</number>" +
				"<track>dummy track data</track>" +
				"<expDate>1210</expDate>" +
				"</card>" +
				"<echeck>" +
				"<accType>Checking</accType>" +
				"<accNum>1234567890</accNum>" +
				"</echeck>" +
				"</authorization>" +
				"</cnpOnlineRequest>";
		String neuteredXml = "<?xml version=1.0 encoding=UTF-8 standalone=yes?>" +
				"<cnpOnlineRequest merchantId=123456 merchantSdk=Java;11.3.0 version=11.3 xmlns=http://www.litle.com/schema>" +
				"<authentication>" +
				"<user>NEUTERED</user>" +
				"<password>NEUTERED</password>" +
				"</authentication>" +
				"<authorization reportGroup=Planets id=id>" +
				"<orderId>12344</orderId>" +
				"<amount>106</amount>" +
				"<orderSource>ecommerce</orderSource>" +
				"<card>" +
				"<type>VI</type>" +
				"<number>NEUTERED</number>" +
				"<track>NEUTERED</track>" +
				"<expDate>1210</expDate>" +
				"</card>" +
				"<echeck>" +
				"<accType>Checking</accType>" +
				"<accNum>NEUTERED</accNum>" +
				"</echeck>" +
				"</authorization>" +
				"</cnpOnlineRequest>";
		assertEquals(neuteredXml, communication.neuterXml(xml));
	}

	@Test
	public void testGetBestProtocol() {
		assertEquals("TLSv1.2", com.cnp.sdk.Communication.getBestProtocol(new String[] {"TLSv1.1", "TLSv1.2"}));
	}



    @Test
    public void testSendCnpRequestFileToSFTP() throws IOException {
        Properties props = new Properties();
        props.setProperty("sftpPort", "abc");
        try {
            communication.sendCnpRequestFileToSFTP(new File("test.txt"), props);
            fail("Must throw exception");
        } catch (CnpBatchException e) {
            assertEquals("Exception parsing sftpPort from config", e.getMessage());
        }
    }

}

