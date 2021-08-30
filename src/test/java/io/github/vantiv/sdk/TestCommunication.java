package io.github.vantiv.sdk;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestCommunication {

	private Communication communication;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

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
				"<cardValidationNum>1234</cardValidationNum>" +
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
				"<cardValidationNum>NEUTERED</cardValidationNum>" +
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
		Assert.assertEquals("TLSv1.2", Communication.getBestProtocol(new String[] {"TLSv1.1", "TLSv1.2"}));
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


    @Test
	public void testPrintXmlOutput(){
		String xml = "<?xml version=1.0 encoding=UTF-8 standalone=yes?>" +
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
		String neuteredXml = "Request XML: <?xml version=1.0 encoding=UTF-8 standalone=yes?>" +
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
				"</cnpOnlineRequest>\n";

		//chang output to allow us to test the output
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));

		//test that the data going out doesnt get neutered
		assertEquals(communication.printXml(xml, true), xml);
		//test that the output does in fact get neutered
		assertEquals(outContent.toString(), neuteredXml);

		//reset output
		System.setOut(originalOut);
		System.setErr(originalErr);
	}
}

