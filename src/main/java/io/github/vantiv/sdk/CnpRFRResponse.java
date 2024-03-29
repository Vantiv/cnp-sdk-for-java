package io.github.vantiv.sdk;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import io.github.vantiv.sdk.generate.RFRResponse;

/**
 * Wrapper class to initialize the batch Responses
 */
public class CnpRFRResponse {
	private RFRResponse rfrResponse;
	private ResponseFileParser responseFileParser;
	private JAXBContext jc;
	private Unmarshaller unmarshaller;

	CnpRFRResponse(RFRResponse rfrResponse) {
		setRFRResponse(rfrResponse);
	}

	public CnpRFRResponse(ResponseFileParser responseFileParser) throws CnpBatchException{
		this.responseFileParser = responseFileParser;
		String rfrResponseXML = "";

		try {
			rfrResponseXML = responseFileParser.getNextTag("RFRResponse");
			jc = JAXBContext.newInstance("io.github.vantiv.sdk.generate");
			unmarshaller = jc.createUnmarshaller();
			rfrResponse = (RFRResponse) unmarshaller.unmarshal(new StringReader(rfrResponseXML));
		} catch (JAXBException e) {
			throw new CnpBatchException("There was an exception while trying to unmarshall rfrResponse: " + rfrResponseXML, e);
		} catch (Exception e) {
			throw new CnpBatchException("There was an unknown error while parsing the response file.", e);
		}
	}

	void setRFRResponse(RFRResponse rfrResponse) {
		this.rfrResponse = rfrResponse;
	}

	RFRResponse getRFRResponse() {
		return this.rfrResponse;
	}

	public String getRFRResponseCode(){
	    return this.rfrResponse.getResponse();
	}

	public String getRFRResponseMessage(){
	    return this.rfrResponse.getMessage();

	}

}
