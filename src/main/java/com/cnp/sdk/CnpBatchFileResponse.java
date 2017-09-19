package com.cnp.sdk;

import java.io.File;

import javax.xml.bind.JAXBException;


public class CnpBatchFileResponse extends CnpFileResponse{


	/**
	 * This constructor initializes the CnpBatchResponseList to the Response values.
	 * @param xmlFile
	 * @throws JAXBException
	 */

	public CnpBatchFileResponse(File xmlFile) throws CnpBatchException{
		super(xmlFile);
	}

	/**
	 * Retrieves the response object for the next batch in the response file
	 * @return
	 */
	public CnpBatchResponse getNextCnpBatchResponse(){
		CnpBatchResponse retObj = null;
		retObj = new CnpBatchResponse(super.responseFileParser);
		return retObj;
	}
}
