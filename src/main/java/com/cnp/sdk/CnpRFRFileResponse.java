package com.cnp.sdk;

import java.io.File;

import javax.xml.bind.JAXBException;

/**
 * Container class holding the RFR response
 *
 */
public class CnpRFRFileResponse extends CnpFileResponse{
	/**
	 * This constructor initializes the CnpBatchResponseList to the Response values.
	 * @param xmlFile
	 * @throws JAXBException
	 */
	public CnpRFRFileResponse(File xmlFile) throws CnpBatchException{
		super(xmlFile);
	}

	public CnpRFRResponse getCnpRFRResponse(){
		CnpRFRResponse retObj = null;
		retObj = new CnpRFRResponse(super.responseFileParser);
		return retObj;
	}
}
